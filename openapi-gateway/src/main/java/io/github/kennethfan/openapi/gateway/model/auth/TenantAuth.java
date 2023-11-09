package io.github.kennethfan.openapi.gateway.model.auth;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.github.kennethfan.openapi.gateway.enums.DeletedEnum;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@TableName("t_tenant_auth")
@Getter
@Setter
public class TenantAuth implements Serializable {

    private static final long serialVersionUID = -4508151051053112110L;

    private Integer id;

    private String tenantId;

    private String resourceCode;

    /**
     * @see DeletedEnum
     */
    private Integer deleted;

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
