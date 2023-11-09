package io.github.kennethfan.openapi.gateway.util;

import io.github.kennethfan.openapi.client.model.response.ResponseData;
import org.springframework.http.HttpStatus;

public class ResponseUtil {
    public static <E> ResponseData<E> error(HttpStatus httpStatus) {
        return ResponseData.error(httpStatus.value(), httpStatus.getReasonPhrase());
    }

    public static <E> ResponseData<E> error(HttpStatus httpStatus, String msg) {
        return ResponseData.error(httpStatus.value(), msg);
    }
}
