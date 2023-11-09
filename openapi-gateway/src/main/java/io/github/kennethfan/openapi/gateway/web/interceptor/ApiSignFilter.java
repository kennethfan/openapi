package io.github.kennethfan.openapi.gateway.web.interceptor;

import io.github.kennethfan.openapi.gateway.context.ContextUtil;
import io.github.kennethfan.openapi.gateway.exception.AuthException;
import io.github.kennethfan.openapi.gateway.model.auth.TenantConfig;
import io.github.kennethfan.openapi.client.client.ClientHelper;
import io.github.kennethfan.openapi.gateway.service.AuthService;
import io.github.kennethfan.openapi.gateway.web.interceptor.wrapper.AuthHttpServletRequestWrapper;
import io.github.kennethfan.openapi.gateway.web.interceptor.wrapper.AuthHttpServletResponseWrapper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static io.github.kennethfan.openapi.client.client.ClientAuthConstants.*;

@WebFilter(urlPatterns = "/api/*")
public class ApiSignFilter implements Filter {
    @Autowired
    private AuthService authService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        String appKey = httpServletRequest.getHeader(APP_KEY);
        String sign = httpServletRequest.getHeader(AUTHORIZATION_KEY);
        String nonceStr = httpServletRequest.getHeader(NONCE_STR_KEY);
        String timestamp = httpServletRequest.getHeader(TIMESTAMP_KEY);
        if (StringUtils.isBlank(appKey) || StringUtils.isBlank(sign)) {
            noOkResp((HttpServletResponse) response, HttpStatus.UNAUTHORIZED);
            return;
        }

        TenantConfig tenantConfig;
        ClientHelper clientHelper;
        try {
            tenantConfig = authService.getTenantConfigByAppKey(appKey);
            clientHelper = authService.getClientHelperByTenantConfig(tenantConfig);
        } catch (AuthException e) {
            noOkResp((HttpServletResponse) response, HttpStatus.UNAUTHORIZED, e.getMessage());
            return;
        }

        String body = null;
        if (httpServletRequest.getContentLength() > 0) {
            body = new String(IOUtils.toByteArray(httpServletRequest.getInputStream()), request.getCharacterEncoding());
        }
        String verifySign = clientHelper.generateSign(httpServletRequest.getRequestURL().toString(), body, nonceStr, timestamp);
        if (!StringUtils.equals(sign, verifySign)) {
            noOkResp((HttpServletResponse) response, HttpStatus.UNAUTHORIZED);
            return;
        }

        ContextUtil.setTenant(tenantConfig.getTenantId());

        String postRequestBody = null;
        if (StringUtils.isNotBlank(body)) {
            JSONObject bodyMap = JSON.parseObject(body);
            postRequestBody = clientHelper.decryptBody((String) bodyMap.get(ENCRYPT_BODY_KEY));
        }

        AuthHttpServletRequestWrapper requestWrapper = new AuthHttpServletRequestWrapper(httpServletRequest, postRequestBody);
        AuthHttpServletResponseWrapper responseWrapper = new AuthHttpServletResponseWrapper((HttpServletResponse) response);
        chain.doFilter(requestWrapper, responseWrapper);

        ContextUtil.clearTenant();

        if (responseWrapper.getStatus() != HttpStatus.OK.value()) {
            response.getOutputStream().write(responseWrapper.getResponseData());
            response.getOutputStream().flush();
            return;
        }

        String respBody = new String(responseWrapper.getResponseData(), responseWrapper.getCharacterEncoding());
        String encryptRespBody = clientHelper.encryptBody(respBody);

        Map<String, String> encryptBodyMap = new HashMap<>();
        encryptBodyMap.put(ENCRYPT_BODY_KEY, encryptRespBody);
        String postBody = JSON.toJSONString(encryptBodyMap);

        nonceStr = UUID.randomUUID().toString();
        long respTs = System.currentTimeMillis();
        String responseSign = clientHelper.generateSign(requestWrapper.getRequestURL().toString(), postBody, nonceStr, respTs);

        ((HttpServletResponse) response).addHeader(NONCE_STR_KEY, nonceStr);
        ((HttpServletResponse) response).addHeader(TIMESTAMP_KEY, String.valueOf(respTs));
        ((HttpServletResponse) response).addHeader(AUTHORIZATION_KEY, responseSign);
        response.getWriter().write(postBody);
        response.getWriter().flush();
    }

    @Override
    public void destroy() {
    }

    private void noOkResp(HttpServletResponse response, HttpStatus httpStatus) throws IOException {
        noOkResp(response, httpStatus, httpStatus.getReasonPhrase());
    }

    private void noOkResp(HttpServletResponse response, HttpStatus httpStatus, String msg) throws IOException {
        response.setStatus(httpStatus.value());
        response.getOutputStream().write(msg.getBytes(StandardCharsets.UTF_8));
        response.getOutputStream().flush();
        return;
    }
}
