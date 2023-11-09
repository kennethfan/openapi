package io.github.kennethfan.openapi.gateway.web.vo.auth;

import io.github.kennethfan.openapi.gateway.enums.ResourceCodeEnum;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class ResourceVO {

    private String name;

    private String desc;

    public static ResourceVO newInstance(ResourceCodeEnum resourceCodeEnum) {
        ResourceVO vo = new ResourceVO();
        vo.setName(resourceCodeEnum.name());
        vo.setDesc(resourceCodeEnum.getDesc());

        return vo;
    }
}
