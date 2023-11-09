package io.github.kennethfan.openapi.gateway.web.vo.auth;

import io.github.kennethfan.openapi.gateway.model.auth.TenantConfig;
import io.github.kennethfan.openapi.gateway.util.DateUtil;
import io.github.kennethfan.openapi.gateway.enums.TenantStatusEnum;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Setter
@Getter
public class TenantBaseVO {

    /***
     * 租户id
     */
    private String tenantId;

    private String tenantName;

    private String tenantDesc;

    /**
     * @see TenantStatusEnum
     */
    private String status;

    /**
     * 过期时间 YYYY-MM-DD HH:ii:ss
     */
    private String expireTime;

    public TenantConfig toDO() {
        TenantConfig tenantConfig = new TenantConfig();

        tenantConfig.setTenantId(tenantId);
        tenantConfig.setTenantName(tenantName);
        tenantConfig.setTenantDesc(tenantDesc);

        tenantConfig.setStatus(status);
        if (StringUtils.isNotBlank(expireTime)) {
            tenantConfig.setExpireTime(DateUtil.datetimeStrToTimestamp(expireTime));
        }

        return tenantConfig;
    }

    protected void fillFromDO(TenantConfig tenantConfig) {
        if (tenantConfig == null) {
            return;
        }

        this.setTenantId(tenantConfig.getTenantId());
        this.setTenantName(tenantConfig.getTenantName());
        this.setTenantDesc(tenantConfig.getTenantDesc());

        this.setStatus(tenantConfig.getStatus());

        if (tenantConfig.getExpireTime() != null) {
            this.setExpireTime(DateUtil.timestampToDatetimeStr(tenantConfig.getExpireTime()));
        }
    }

}
