package io.github.kennethfan.openapi.gateway.service;

import io.github.kennethfan.openapi.gateway.model.auth.TenantConfig;
import io.github.kennethfan.openapi.client.client.ClientHelper;

public interface AuthService {

    TenantConfig getTenantConfigByAppKey(String appKey);

    ClientHelper getClientHelperByTenantConfig(TenantConfig tenantConfig);

    boolean allow(String tenantId, String resourceCode);
}
