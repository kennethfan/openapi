package io.github.kennethfan.openapi.gateway.enums;

import lombok.Getter;

@Getter
public enum AuthErrorEnum {
    /**
     * 租户不存在
     */
    TENANT_NOT_EXISTS("系统未授权，请联系相关人员授权"),
    /**
     * 租户失效
     */
    TENANT_INVALID("系统授权已失效，请联系相关人员处理"),
    /**
     * 租户过期
     */
    TENANT_EXPIRE("系统授权已过期，请联系相关人员处理");

    private String msg;

    AuthErrorEnum(String msg) {
        this.msg = msg;
    }
}
