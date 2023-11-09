package io.github.kennethfan.openapi.gateway.exception;

import io.github.kennethfan.openapi.gateway.enums.AuthErrorEnum;

public class AuthException extends RuntimeException {

    public AuthException(String message) {
        super(message);
    }

    public AuthException(AuthErrorEnum authErrorEnum) {
        this(authErrorEnum.getMsg());
    }
}
