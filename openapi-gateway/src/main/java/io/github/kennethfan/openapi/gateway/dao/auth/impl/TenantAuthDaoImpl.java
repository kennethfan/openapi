package io.github.kennethfan.openapi.gateway.dao.auth.impl;

import io.github.kennethfan.openapi.gateway.dao.auth.TenantAuthDao;
import io.github.kennethfan.openapi.gateway.dao.auth.mapper.TenantAuthMapper;
import io.github.kennethfan.openapi.gateway.enums.DeletedEnum;
import io.github.kennethfan.openapi.gateway.model.auth.TenantAuth;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class TenantAuthDaoImpl extends ServiceImpl<TenantAuthMapper, TenantAuth> implements TenantAuthDao {

    @Override
    public Set<String> getAllowedResourceCodes(String tenantId) {
        LambdaQueryWrapper<TenantAuth> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(TenantAuth::getTenantId, tenantId);

        return Optional.ofNullable(this.list(wrapper))
                .orElse(new ArrayList<>())
                .stream()
                .filter(o -> !DeletedEnum.deleted(o.getDeleted()))
                .map(TenantAuth::getResourceCode)
                .collect(Collectors.toSet());
    }

    @Override
    public List<TenantAuth> getListByTenantId(String tenantId) {
        LambdaQueryWrapper<TenantAuth> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(TenantAuth::getTenantId, tenantId);
        return this.list(wrapper);
    }

    @Override
    public void updateAllowedResourceCodes(List<TenantAuth> tenantAuthList) {
        this.saveOrUpdateBatch(tenantAuthList);
    }
}
