package io.github.kennethfan.openapi.gateway.exception;

import io.github.kennethfan.openapi.gateway.enums.TenantConfigErrorEnum;

public class TenantConfigException extends RuntimeException{

    public TenantConfigException(String message) {
        super(message);
    }

    public TenantConfigException(TenantConfigErrorEnum errorEnum) {
        this(errorEnum.getMsg());
    }
}
