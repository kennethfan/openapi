package io.github.kennethfan.openapi.gateway.service;

import io.github.kennethfan.openapi.gateway.model.auth.TenantConfig;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public interface TenantService {

    /**
     * 查询详情
     *
     * @param tenantId
     * @return
     */
    TenantConfig getByTenantId(String tenantId);

    /**
     * 添加系统
     *
     * @param tenantConfig
     */
    void addTenantConfig(TenantConfig tenantConfig);

    /**
     * 添加系统
     *
     * @param tenantConfig
     */
    void updateTenantConfig(TenantConfig tenantConfig);

    /**
     * 启用
     *
     * @param tenantId
     */
    void validTenantConfig(String tenantId);

    /**
     * 禁用
     *
     * @param tenantId
     */
    void invalidTenantConfig(String tenantId);

    /**
     * 获取总数
     *
     * @param wrapper
     * @return
     */
    long countByCondition(Wrapper<TenantConfig> wrapper);

    /**
     * 分页查询
     *
     * @param wrapper
     * @param limit
     * @param offset
     * @return
     */
    IPage<TenantConfig> pageByCondition(Wrapper<TenantConfig> wrapper, Page<TenantConfig> page);
}
