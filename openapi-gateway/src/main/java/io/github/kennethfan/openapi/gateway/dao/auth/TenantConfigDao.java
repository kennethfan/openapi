package io.github.kennethfan.openapi.gateway.dao.auth;

import io.github.kennethfan.openapi.gateway.model.auth.TenantConfig;
import com.baomidou.mybatisplus.extension.service.IService;

public interface TenantConfigDao extends IService<TenantConfig> {

    TenantConfig getByTenantId(String tenantId);

    TenantConfig getByAppKey(String appKey);
}
