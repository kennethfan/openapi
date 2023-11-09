package io.github.kennethfan.openapi.gateway.dao.auth.impl;

import io.github.kennethfan.openapi.gateway.dao.auth.TenantConfigDao;
import io.github.kennethfan.openapi.gateway.dao.auth.mapper.TenantConfigMapper;
import io.github.kennethfan.openapi.gateway.model.auth.TenantConfig;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

@Repository
public class TenantConfigDaoImpl extends ServiceImpl<TenantConfigMapper, TenantConfig> implements TenantConfigDao {
    @Override
    public TenantConfig getByTenantId(String tenantId) {
        LambdaQueryWrapper<TenantConfig> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(TenantConfig::getTenantId, tenantId);
        return this.getOne(wrapper);
    }

    @Override
    public TenantConfig getByAppKey(String appKey) {
        LambdaQueryWrapper<TenantConfig> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(TenantConfig::getAppKey, appKey);
        return this.getOne(wrapper);
    }
}
