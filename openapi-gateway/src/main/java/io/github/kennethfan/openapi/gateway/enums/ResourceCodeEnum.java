package io.github.kennethfan.openapi.gateway.enums;

import lombok.Getter;

@Getter
public enum ResourceCodeEnum {

    /**
     * 停车券
     */
    PARKING_COUPON("停车券"),

    /**
     * 链券通
     */
    LQT("链券通"),
    ;

    private String desc;

    ResourceCodeEnum(String desc) {
        this.desc = desc;
    }
}
