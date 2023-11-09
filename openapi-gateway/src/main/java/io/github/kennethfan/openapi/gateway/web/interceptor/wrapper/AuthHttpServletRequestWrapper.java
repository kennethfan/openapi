package io.github.kennethfan.openapi.gateway.web.interceptor.wrapper;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class AuthHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private byte[] body;

    public AuthHttpServletRequestWrapper(HttpServletRequest request, String data) {
        super(request);
        body = data == null ? new byte[0] : data.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public ServletInputStream getInputStream() {
        return new RRServletInputStreamWrapper(body);
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }
}
