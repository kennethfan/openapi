package io.github.kennethfan.openapi.gateway.web.interceptor;

import io.github.kennethfan.openapi.gateway.anno.ResourceAuth;
import io.github.kennethfan.openapi.gateway.context.ContextUtil;
import io.github.kennethfan.openapi.gateway.enums.ResourceCodeEnum;
import io.github.kennethfan.openapi.gateway.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ApiAuthInterceptor implements HandlerInterceptor, WebMvcConfigurer {

    @Autowired
    private AuthService authService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration registration = registry.addInterceptor(this);
        registration.addPathPatterns("/api/**"); //所有路径都被拦截
    }

    /**
     * 权限校验
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ResourceAuth resourceAuth = AnnotationUtil.getAnnotation((HandlerMethod) handler, ResourceAuth.class);
        if (resourceAuth == null) {
            return true;
        }

        ResourceCodeEnum resourceCode = resourceAuth.value();
        String tenantId = ContextUtil.getTenant();
        if (authService.allow(tenantId, resourceCode.name())) {
            return true;
        }

        RestInterceptorUtil.respError(response, HttpStatus.UNAUTHORIZED, "系统未授权");
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
