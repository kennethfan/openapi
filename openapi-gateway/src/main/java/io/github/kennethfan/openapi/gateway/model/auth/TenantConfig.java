package io.github.kennethfan.openapi.gateway.model.auth;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.github.kennethfan.openapi.gateway.enums.TenantStatusEnum;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@TableName("t_tenant_config")
@Setter
@Getter
public class TenantConfig implements Serializable {

    private static final long serialVersionUID = -6027344237643724051L;

    private Integer id;
    /***
     * 租户id
     */
    private String tenantId;

    private String tenantName;

    private String tenantDesc;

    private String appKey;

    private String secret;

    private String publicKey;

    private String privateKey;

    /**
     * @see TenantStatusEnum
     */
    private String status;

    private Long expireTime;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateTime;
}
