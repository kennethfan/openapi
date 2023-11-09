package io.github.kennethfan.openapi.gateway.context;

import com.alibaba.ttl.TransmittableThreadLocal;

public class ContextUtil {
    public static final ThreadLocal<String> TENANT_CONTEXT = new TransmittableThreadLocal<>();

    /**
     * 设置tenant
     *
     * @param tenant
     */
    public static void setTenant(String tenant) {
        TENANT_CONTEXT.set(tenant);
    }

    /**
     * 获取tenant
     */
    public static String getTenant() {
        return TENANT_CONTEXT.get();
    }

    /**
     * 清理tenant
     */
    public static void clearTenant() {
        TENANT_CONTEXT.remove();
    }
}
