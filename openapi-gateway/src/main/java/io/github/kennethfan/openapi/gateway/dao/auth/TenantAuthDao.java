package io.github.kennethfan.openapi.gateway.dao.auth;

import io.github.kennethfan.openapi.gateway.model.auth.TenantAuth;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

public interface TenantAuthDao extends IService<TenantAuth> {

    Set<String> getAllowedResourceCodes(String tenantId);

    List<TenantAuth> getListByTenantId(String tenantId);

    void  updateAllowedResourceCodes(List<TenantAuth> tenantAuthList);
}
