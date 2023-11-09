package io.github.kennethfan.openapi.gateway.service.impl;

import io.github.kennethfan.openapi.gateway.dao.auth.TenantConfigDao;
import io.github.kennethfan.openapi.gateway.enums.TenantConfigErrorEnum;
import io.github.kennethfan.openapi.gateway.enums.TenantStatusEnum;
import io.github.kennethfan.openapi.gateway.exception.TenantConfigException;
import io.github.kennethfan.openapi.gateway.model.auth.TenantConfig;
import io.github.kennethfan.openapi.client.encrypt.RsaKeyGenerator;
import io.github.kennethfan.openapi.gateway.service.TenantService;
import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TenantServiceImpl implements TenantService {

    @Autowired
    private TenantConfigDao tenantConfigDao;

    @Override
    public TenantConfig getByTenantId(String tenantId) {
        return tenantConfigDao.getByTenantId(tenantId);
    }

    @Override
    public void addTenantConfig(TenantConfig tenantConfig) {
        TenantConfig exist = tenantConfigDao.getByTenantId(tenantConfig.getTenantId());
        if (exist != null) {
            throw new TenantConfigException(TenantConfigErrorEnum.TENANT_CONFIG_EXISTS);
        }

        tenantConfig.setAppKey(tenantConfig.getTenantId());

        RsaKeyGenerator rsaKeyGenerator = RsaKeyGenerator.newInstance();

        tenantConfig.setPublicKey(rsaKeyGenerator.dumpPublicKey());
        tenantConfig.setPrivateKey(rsaKeyGenerator.dumpPrivateKey());
        tenantConfig.setSecret(MD5.create().digestHex(UUID.randomUUID().toString()));
        tenantConfig.setStatus(StringUtils.defaultString(tenantConfig.getStatus(), TenantStatusEnum.VALID.name()));

        tenantConfigDao.save(tenantConfig);
    }

    @Override
    public void updateTenantConfig(TenantConfig tenantConfig) {
        TenantConfig exist = tenantConfigDao.getByTenantId(tenantConfig.getTenantId());
        if (exist == null) {
            throw new TenantConfigException(TenantConfigErrorEnum.TENANT_CONFIG_NOT_EXISTS);
        }

        exist.setTenantName(tenantConfig.getTenantName());
        exist.setTenantDesc(tenantConfig.getTenantDesc());
        if (StringUtils.isNotBlank(tenantConfig.getStatus())) {
            exist.setStatus(tenantConfig.getStatus());
        }
        exist.setExpireTime(tenantConfig.getExpireTime());

        tenantConfigDao.updateById(exist);
    }

    @Override
    public void validTenantConfig(String tenantId) {
        if (StringUtils.isBlank(tenantId)) {
            throw new IllegalArgumentException("tenantId is blank");
        }

        TenantConfig tenantConfig = this.getByTenantId(tenantId);
        if (tenantConfig == null) {
            throw new TenantConfigException(TenantConfigErrorEnum.TENANT_CONFIG_NOT_EXISTS);
        }

        tenantConfig.setStatus(TenantStatusEnum.VALID.name());
        tenantConfigDao.updateById(tenantConfig);
    }

    @Override
    public void invalidTenantConfig(String tenantId) {
        if (StringUtils.isBlank(tenantId)) {
            throw new IllegalArgumentException("tenantId is blank");
        }

        TenantConfig tenantConfig = this.getByTenantId(tenantId);
        if (tenantConfig == null) {
            throw new TenantConfigException(TenantConfigErrorEnum.TENANT_CONFIG_NOT_EXISTS);
        }

        tenantConfig.setStatus(TenantStatusEnum.INVALID.name());
        tenantConfigDao.updateById(tenantConfig);
    }

    @Override
    public long countByCondition(Wrapper<TenantConfig> wrapper) {
        return tenantConfigDao.count(wrapper);
    }

    @Override
    public IPage<TenantConfig> pageByCondition(Wrapper<TenantConfig> wrapper, Page<TenantConfig> page) {
        return tenantConfigDao.page(page, wrapper);
    }
}
