package io.github.kennethfan.openapi.gateway.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public enum TenantStatusEnum {

    VALID("生效中"),

    INVALID("未生效"),

    EXPIRED("已到期"),
    ;

    private String desc;

    TenantStatusEnum(String desc) {
        this.desc = desc;
    }

    public static String getDesc(String code) {

        if (StringUtils.isBlank(code)) {
            return null;
        }

        for (TenantStatusEnum element : TenantStatusEnum.values()) {
            if (StringUtils.equalsIgnoreCase(code, element.name())) {
                return element.getDesc();
            }
        }

        return null;
    }
}
