package io.github.kennethfan.openapi.gateway.web.interceptor;

import org.springframework.web.method.HandlerMethod;

import java.lang.annotation.Annotation;

public class AnnotationUtil {

    /**
     * 判断是否有注解
     *
     * @param handlerMethod
     * @param annotationType
     * @param <A>
     * @return
     */
    public static <A extends Annotation> boolean hasAnnotation(HandlerMethod handlerMethod, Class<A> annotationType) {
        return getAnnotation(handlerMethod, annotationType) != null;
    }

    public static <A extends Annotation> A getAnnotation(HandlerMethod handlerMethod, Class<A> annotationType) {

        A anno = handlerMethod.getMethodAnnotation(annotationType);
        if (anno == null) {
            anno = handlerMethod.getMethod().getDeclaringClass().getAnnotation(annotationType);
        }

        return anno;
    }
}
