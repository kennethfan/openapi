package io.github.kennethfan.openapi.gateway.web.interceptor;

import io.github.kennethfan.openapi.gateway.anno.NoneTenantRequired;
import io.github.kennethfan.openapi.gateway.context.ContextUtil;
import org.apache.commons.lang3.StringUtils;
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
public class InnerApiAuthInterceptor implements HandlerInterceptor, WebMvcConfigurer {

    private static final String TENANT_KEY = "tenantId";

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration registration = registry.addInterceptor(this);
        registration.addPathPatterns("/inner-api/**"); //所有路径都被拦截
    }

    /**
     * 内部api直接使用tenant参数，不需要做appkey校验等
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // TODO 登录验证

        if (handler instanceof HandlerMethod && AnnotationUtil.hasAnnotation((HandlerMethod) handler, NoneTenantRequired.class)) {
            return true;
        }

        String tenant = request.getParameter(TENANT_KEY);
        if (StringUtils.isBlank(tenant)) {
            RestInterceptorUtil.respError(response, HttpStatus.UNAUTHORIZED);
            return false;
        }

        ContextUtil.setTenant(tenant);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ContextUtil.clearTenant();
    }
}
