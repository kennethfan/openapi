package io.github.kennethfan.openapi.gateway.web.interceptor.wrapper;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class RRServletInputStreamWrapper extends ServletInputStream {

    private InputStream inputStream;

    public RRServletInputStreamWrapper(byte[] data) {
        super();
        this.inputStream = new ByteArrayInputStream(data);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setReadListener(ReadListener readListener) {

    }

    @Override
    public int read() throws IOException {
        return this.inputStream.read();
    }
}
