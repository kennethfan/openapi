package io.github.kennethfan.openapi.gateway.enums;

import lombok.Getter;

@Getter
public enum TenantConfigErrorEnum {

    TENANT_CONFIG_NOT_EXISTS("系统未创建"),
    TENANT_CONFIG_EXISTS("系统已存在"),

    ;

    private String msg;

    TenantConfigErrorEnum(String msg) {
        this.msg = msg;
    }
}
