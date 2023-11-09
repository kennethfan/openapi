package io.github.kennethfan.openapi.gateway.web.vo.auth;

import io.github.kennethfan.openapi.gateway.enums.TenantStatusEnum;
import io.github.kennethfan.openapi.gateway.model.auth.TenantConfig;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TenantBaseShowVO extends TenantBaseVO {

    private String statusDesc;

    public static TenantBaseShowVO fromDO(TenantConfig tenantConfig) {
        if (tenantConfig == null) {
            return null;
        }

        TenantBaseShowVO vo = new TenantBaseShowVO();

        vo.fillFromDO(tenantConfig);

        vo.setStatusDesc(TenantStatusEnum.getDesc(tenantConfig.getStatus()));
        return vo;
    }
}
