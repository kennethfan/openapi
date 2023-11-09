package io.github.kennethfan.openapi.gateway.service.impl;

import io.github.kennethfan.openapi.gateway.dao.auth.TenantAuthDao;
import io.github.kennethfan.openapi.gateway.dao.auth.TenantConfigDao;
import io.github.kennethfan.openapi.gateway.enums.AuthErrorEnum;
import io.github.kennethfan.openapi.gateway.enums.TenantStatusEnum;
import io.github.kennethfan.openapi.gateway.exception.AuthException;
import io.github.kennethfan.openapi.gateway.model.auth.TenantConfig;
import io.github.kennethfan.openapi.client.client.ClientHelper;
import io.github.kennethfan.openapi.client.encrypt.RsaEncryptor;
import io.github.kennethfan.openapi.gateway.service.AuthService;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private TenantConfigDao tenantConfigDao;

    @Autowired
    private TenantAuthDao tenantAuthDao;


    @Override
    public TenantConfig getTenantConfigByAppKey(String appKey) {
        return tenantConfigDao.getByAppKey(appKey);
    }

    @Override
    public ClientHelper getClientHelperByTenantConfig(TenantConfig tenantConfig) {
        if (tenantConfig == null) {
            throw new AuthException(AuthErrorEnum.TENANT_NOT_EXISTS);
        }

        if (StringUtils.equals(TenantStatusEnum.INVALID.name(), tenantConfig.getStatus())) {
            throw new AuthException(AuthErrorEnum.TENANT_INVALID);
        }

        if (StringUtils.equals(TenantStatusEnum.EXPIRED.name(), tenantConfig.getStatus())) {
            throw new AuthException(AuthErrorEnum.TENANT_EXPIRE);
        }

        if (tenantConfig.getExpireTime() != null && System.currentTimeMillis() > tenantConfig.getExpireTime()) {
            throw new AuthException(AuthErrorEnum.TENANT_EXPIRE);
        }

        return new ClientHelper(tenantConfig.getAppKey(), tenantConfig.getSecret(), RsaEncryptor.fromPublicKey(tenantConfig.getPublicKey()));
    }

    @Override
    public boolean allow(String tenantId, String resourceCode) {
        if (StringUtils.isBlank(tenantId)) {
            return false;
        }

        return tenantAuthDao.getAllowedResourceCodes(tenantId)
                .contains(resourceCode);
    }
}
