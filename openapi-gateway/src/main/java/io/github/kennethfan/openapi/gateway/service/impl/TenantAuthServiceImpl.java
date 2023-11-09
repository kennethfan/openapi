package io.github.kennethfan.openapi.gateway.service.impl;

import io.github.kennethfan.openapi.gateway.dao.auth.TenantAuthDao;
import io.github.kennethfan.openapi.gateway.enums.DeletedEnum;
import io.github.kennethfan.openapi.gateway.model.auth.TenantAuth;
import io.github.kennethfan.openapi.gateway.service.TenantAuthService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TenantAuthServiceImpl implements TenantAuthService {

    @Autowired
    private TenantAuthDao tenantAuthDao;

    @Override
    public void addTenantAuth(String tenantId, Collection<String> resourceCodeList) {
        Set<String> newCodeList = new HashSet<>(resourceCodeList);
        List<TenantAuth> tenantAuthList = tenantAuthDao.getListByTenantId(tenantId);
        tenantAuthList.forEach(o -> {
            o.setDeleted(newCodeList.remove(o.getResourceCode()) ? DeletedEnum.NO.getCode() : DeletedEnum.YES.getCode());
        });
        newCodeList.forEach(o -> {
            TenantAuth tenantAuth = new TenantAuth();
            tenantAuth.setTenantId(tenantId);
            tenantAuth.setResourceCode(o);
            tenantAuth.setDeleted(DeletedEnum.NO.getCode());

            tenantAuthList.add(tenantAuth);
        });


        tenantAuthDao.updateAllowedResourceCodes(tenantAuthList);
    }


    @Override
    public Set<String> getAllowedResourceCodeList(String tenantId) {
        if (StringUtils.isBlank(tenantId)) {
            return new HashSet<>();
        }

        return tenantAuthDao.getAllowedResourceCodes(tenantId);
    }
}
