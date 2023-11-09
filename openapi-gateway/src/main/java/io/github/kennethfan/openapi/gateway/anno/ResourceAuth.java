package io.github.kennethfan.openapi.gateway.anno;

import io.github.kennethfan.openapi.gateway.enums.ResourceCodeEnum;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ResourceAuth {
    ResourceCodeEnum value();
}
