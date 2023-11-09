package io.github.kennethfan.openapi.gateway.web.vo.auth;

import io.github.kennethfan.openapi.gateway.model.auth.TenantConfig;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class TenantAuthInfo {

    private String appKey;

    private String secret;

    public static TenantAuthInfo fromDO(TenantConfig tenantConfig) {
        if (tenantConfig == null) {
            return null;
        }

        return new TenantAuthInfo()
                .setAppKey(tenantConfig.getAppKey())
                .setSecret(tenantConfig.getSecret());
    }
}
