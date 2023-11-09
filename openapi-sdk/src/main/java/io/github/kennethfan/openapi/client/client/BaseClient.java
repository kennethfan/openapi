package io.github.kennethfan.openapi.client.client;

import io.github.kennethfan.openapi.client.encrypt.RsaEncryptor;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.Method;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


@Slf4j
public class BaseClient {

    /**
     * okhttp instance
     */
    private OkHttpClient httpClient;

    /**
     * 客户端验签
     */
    private ClientHelper clientHelper;

    public BaseClient(ClientConfig clientConfig) {
        String fileContent = FileUtil.readString(clientConfig.getKeyPath(), CharsetUtil.UTF_8);
        this.clientHelper = new ClientHelper(clientConfig.getAppKey(), clientConfig.getSecret(), RsaEncryptor.fromPrivateKey(fileContent));

        final long timeout = 30000L;
        this.httpClient = (new OkHttpClient.Builder())
                .connectTimeout(timeout, TimeUnit.MILLISECONDS)
                .writeTimeout(timeout, TimeUnit.MILLISECONDS)
                .readTimeout(timeout, TimeUnit.MILLISECONDS).build();
    }

    /**
     * 添加header
     *
     * @param url
     * @param body
     * @param header
     * @param builder
     */
    private void addHeader(String url, String body, Map<String, Object> header, okhttp3.Request.Builder builder) {
        Map<String, Object> headerCopy = new HashMap<>(Optional.ofNullable(header).orElse(new HashMap<>()));
        this.clientHelper.addAuthorization(url, body, headerCopy);
        headerCopy.forEach((k, v) -> {
            builder.addHeader(k, v.toString());
        });
    }

    private <E> okhttp3.Request.Builder postBuilder(String url, E body, Map<String, Object> header) {
        okhttp3.Request.Builder builder = new okhttp3.Request.Builder();
        builder.url(url);
        String bodyStr = body == null ? null : JSON.toJSONString(body);
        addHeader(url, bodyStr, header, builder);

        builder.header("Content-type", "application/json; charset=UTF-8");
        MediaType mediaType = MediaType.parse("application/json; charset=UTF-8");
        RequestBody requestBody = RequestBody.create(bodyStr, mediaType);
        builder.post(requestBody);

        return builder;
    }

    public <B, R> R post(String host, String uri, B body, Map<String, Object> header, Class<R> clazz) throws IOException {
        RequestContext<B> context = RequestContext.post(host, uri, header, body);
        String responseBody = send(context);

        return JSON.parseObject(responseBody, clazz);
    }

    public <B, R> R post(String host, String uri, B body, Map<String, Object> header, TypeReference<R> typeReference) throws IOException {
        RequestContext<B> context = RequestContext.post(host, uri, header, body);
        String responseBody = send(context);

        return JSON.parseObject(responseBody, typeReference);
    }

    public <R> R get(String host, String uri, Map<String, Object> header, Class<R> clazz) throws IOException {
        RequestContext<Void> context = RequestContext.get(host, uri, header);
        String responseBody = send(context);

        return JSON.parseObject(responseBody, clazz);
    }

    public <R> R get(String host, String uri, Map<String, Object> header, TypeReference<R> typeReference) throws IOException {
        RequestContext<Void> context = RequestContext.get(host, uri, header);
        String responseBody = send(context);

        return JSON.parseObject(responseBody, typeReference);
    }


    private <B> String send(RequestContext<B> context) throws IOException {
        okhttp3.Request.Builder builder = new okhttp3.Request.Builder();

        String url = context.getHost() + context.getUri();
        builder.url(context.getHost() + context.getUri());

        String postBodyStr = null;
        String bodyStr = null;
        if (Method.POST.equals(context.getMethod())) {
            builder.header("Content-type", "application/json; charset=UTF-8");
            MediaType mediaType = MediaType.parse("application/json; charset=UTF-8");

            bodyStr = JSON.toJSONString(context.getBody());
            String encryptBodyStr = this.clientHelper.encryptBody(bodyStr);

            Map<String, String> bodyMap = new HashMap<>();
            bodyMap.put(ClientAuthConstants.ENCRYPT_BODY_KEY, encryptBodyStr);
            postBodyStr = JSON.toJSONString(bodyMap);
            RequestBody requestBody = RequestBody.create(postBodyStr, mediaType);
            builder.post(requestBody);
        }

        Map<String, Object> headerCopy = new HashMap<>(Optional.ofNullable(context.getHeader()).orElse(new HashMap<>()));
        this.clientHelper.addAuthorization(url, postBodyStr, headerCopy);
        headerCopy.forEach((k, v) -> builder.addHeader(k, String.valueOf(v)));


        Request request = builder.build();
        Call call = this.httpClient.newCall(request);
        Response response = null;
        String responseBodyStr = null;
        try {
            response = call.execute();
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                responseBodyStr = responseBody.string();
            }
        } catch (IOException e) {
            throw new RuntimeException(url + " request error. ", e);
        } finally {
            log.info("BaseClient > URL : {}\n\tRequest : {}\n\tResponse : {}", url, bodyStr, responseBodyStr);
        }

        int code = response.code();
        if (code != 200) {
            throw new RuntimeException(url + ", " + responseBodyStr);
        }

        String sign = response.header(ClientAuthConstants.AUTHORIZATION_KEY);
        String nonceStr = response.header(ClientAuthConstants.NONCE_STR_KEY);
        String timestamp = response.header(ClientAuthConstants.TIMESTAMP_KEY);
        String verifySign = this.clientHelper.generateSign(url, responseBodyStr, nonceStr, timestamp);

        if (!StringUtils.equals(sign, verifySign)) {
            throw new RuntimeException(url + ", sign verify failed");
        }

        JSONObject responseBodyObject = JSON.parseObject(responseBodyStr);

        return this.clientHelper.decryptBody((String) responseBodyObject.get(ClientAuthConstants.ENCRYPT_BODY_KEY));
    }
}

