package io.github.kennethfan.openapi.gateway.web.advice;

import io.github.kennethfan.openapi.gateway.exception.AuthException;
import io.github.kennethfan.openapi.gateway.exception.TenantConfigException;
import io.github.kennethfan.openapi.client.model.response.ResponseData;
import io.github.kennethfan.openapi.gateway.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice(annotations = RestController.class)
@Slf4j
@Order(1)
public class ErrorAdvice {

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.OK)
    public <T> ResponseData<T> error(HttpServletRequest request, Throwable exception) {
        log.error("{}{}{} error ", request.getRequestURI(),
                StringUtils.isBlank(request.getQueryString()) ? StringUtils.EMPTY : "?",
                StringUtils.defaultString(request.getQueryString(), StringUtils.EMPTY),
                exception);
        if (exception instanceof AuthException) {
            return ResponseUtil.error(HttpStatus.UNAUTHORIZED, exception.getMessage());
        }

        if (exception instanceof TenantConfigException) {
            return ResponseUtil.error(HttpStatus.BAD_REQUEST, exception.getMessage());
        }

        if (exception instanceof IllegalArgumentException) {
            return ResponseUtil.error(HttpStatus.BAD_REQUEST, exception.getMessage());
        }
        if (exception instanceof MissingServletRequestParameterException) {
            return ResponseUtil.error(HttpStatus.BAD_REQUEST);
        }

        return ResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
