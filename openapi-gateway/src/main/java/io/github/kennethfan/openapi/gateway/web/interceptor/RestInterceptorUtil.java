package io.github.kennethfan.openapi.gateway.web.interceptor;

import io.github.kennethfan.openapi.gateway.util.ResponseUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class RestInterceptorUtil {

    private RestInterceptorUtil() {
    }

    public static void respError(HttpServletResponse response, HttpStatus httpStatus) throws IOException {
        respError(response, httpStatus, httpStatus.getReasonPhrase());
    }

    public static void respError(HttpServletResponse response, HttpStatus httpStatus, String msg) throws IOException {
        response.setStatus(HttpStatus.OK.value());
        response.setHeader("Content-Type", "application/json; charset=utf-8");

        response.getOutputStream().write(JSON.toJSONString(ResponseUtil.error(httpStatus, msg)).getBytes(StandardCharsets.UTF_8));
        response.getOutputStream().flush();
    }
}
