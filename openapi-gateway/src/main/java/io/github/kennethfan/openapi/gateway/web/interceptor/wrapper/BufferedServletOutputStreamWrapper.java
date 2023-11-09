package io.github.kennethfan.openapi.gateway.web.interceptor.wrapper;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.IOException;
import java.io.OutputStream;

public class BufferedServletOutputStreamWrapper extends ServletOutputStream {


    private OutputStream outputStream;

    public BufferedServletOutputStreamWrapper(OutputStream outputStream) {
        super();
        this.outputStream = outputStream;
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {

    }

    @Override
    public void write(int b) throws IOException {
        this.outputStream.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        super.write(b, 0, b.length);
    }
}
