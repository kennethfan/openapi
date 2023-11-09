package io.github.kennethfan.openapi.gateway.web.vo.auth;

import io.github.kennethfan.openapi.client.model.request.PageCondition;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TenantSearchCondition extends PageCondition {

    private String tenantId;

    private String tenantName;

    private String status;
}
