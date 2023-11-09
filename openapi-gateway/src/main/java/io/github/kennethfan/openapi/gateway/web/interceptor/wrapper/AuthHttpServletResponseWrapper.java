package io.github.kennethfan.openapi.gateway.web.interceptor.wrapper;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;

public class AuthHttpServletResponseWrapper extends HttpServletResponseWrapper {

    private ByteArrayOutputStream buffer;
    private ServletOutputStream outputStream;
    private PrintWriter writer;

    public AuthHttpServletResponseWrapper(HttpServletResponse response) throws UnsupportedEncodingException {
        super(response);

        this.buffer = new ByteArrayOutputStream();
        this.outputStream = new BufferedServletOutputStreamWrapper(buffer);
        this.writer = new PrintWriter(new OutputStreamWriter(buffer, this.getCharacterEncoding()));
    }


    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return this.outputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return this.writer;
    }

    @Override
    public void flushBuffer() throws IOException {
        if (outputStream != null) {
            outputStream.flush();
        }
        if (writer != null) {
            writer.flush();
        }
    }

    @Override
    public void reset() {
        buffer.reset();
    }

    public byte[] getResponseData() throws IOException {
        flushBuffer();
        return buffer.toByteArray();
    }
}
