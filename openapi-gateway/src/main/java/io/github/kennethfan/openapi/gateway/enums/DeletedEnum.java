package io.github.kennethfan.openapi.gateway.enums;

import lombok.Getter;

@Getter
public enum DeletedEnum {

    YES(1),

    NO(0),
    ;

    private final int code;

    DeletedEnum(int code) {
        this.code = code;
    }

    /**
     * 是否删除
     *
     * @param code
     * @return
     */
    public static boolean deleted(Integer code) {
        return code != null && YES.getCode() == code;
    }
}
