package io.github.kennethfan.openapi.client.client;

import io.github.kennethfan.openapi.client.encrypt.RsaEncryptor;
import cn.hutool.core.util.URLUtil;
import cn.hutool.crypto.digest.MD5;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
public class ClientHelper {

    private String appKey;

    private String secret;

    private RsaEncryptor rsaEncryptor;

    public void addAuthorization(String url, String body, Map<String, Object> header) {
        String nonceStr = UUID.randomUUID().toString();
        long timestamp = System.currentTimeMillis();

        String sign = generateSign(url, body, nonceStr, timestamp);
        header.put(ClientAuthConstants.AUTHORIZATION_KEY, sign);
        header.put(ClientAuthConstants.APP_KEY, this.appKey);
        header.put(ClientAuthConstants.TIMESTAMP_KEY, timestamp);
        header.put(ClientAuthConstants.NONCE_STR_KEY, nonceStr);
    }

    public String generateSign(String url, String body, String nonceStr, long timestamp) {
        return generateSign(url, body, nonceStr, String.valueOf(timestamp));
    }

    public String generateSign(String url, String body, String nonceStr, String timestamp) {
        String msg = buildMessage(url, body);
        String waitSignStr = this.appKey +
                this.secret +
                msg +
                nonceStr +
                timestamp;

        return MD5.create().digestHex(waitSignStr);
    }

    private String buildMessage(String url, String body) {
        return URLUtil.encode(url) + StringUtils.defaultString(body, StringUtils.EMPTY);
    }

    /**
     * 加密数据
     *
     * @param body
     * @return
     */
    public String encryptBody(String body) {
        return Base64.getEncoder().encodeToString(rsaEncryptor.encrypt(body.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * 解密数据
     *
     * @param body
     * @return
     */
    public String decryptBody(String body) {
        return new String(rsaEncryptor.decrypt(Base64.getDecoder().decode(body.getBytes(StandardCharsets.UTF_8))), StandardCharsets.UTF_8);
    }
}
