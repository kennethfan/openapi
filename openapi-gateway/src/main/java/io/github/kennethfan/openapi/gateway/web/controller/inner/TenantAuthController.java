package io.github.kennethfan.openapi.gateway.web.controller.inner;


import io.github.kennethfan.openapi.gateway.anno.NoneTenantRequired;
import io.github.kennethfan.openapi.gateway.context.ContextUtil;
import io.github.kennethfan.openapi.gateway.enums.ResourceCodeEnum;
import io.github.kennethfan.openapi.client.model.response.ResponseData;
import io.github.kennethfan.openapi.gateway.service.TenantAuthService;
import io.github.kennethfan.openapi.gateway.web.vo.auth.ResourceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/inner-api/tenant-auth/", produces = "application/json; charset=utf-8")
public class TenantAuthController {

    @Autowired
    private TenantAuthService tenantAuthService;

    @NoneTenantRequired
    @GetMapping("all")
    public ResponseData<List<ResourceVO>> getAllResources() {
        return ResponseData.success(Arrays.stream(ResourceCodeEnum.values())
                .map(ResourceVO::newInstance)
                .collect(Collectors.toList()));
    }

    @PostMapping("add")
    public ResponseData<Boolean> add(@RequestBody List<String> resourceCodeList) {
        tenantAuthService.addTenantAuth(ContextUtil.getTenant(), resourceCodeList);

        return ResponseData.success(Boolean.TRUE);
    }

    @GetMapping("list-allowed")
    public ResponseData<List<ResourceVO>> getAllowedResources() {
        Set<String> allowedList = tenantAuthService.getAllowedResourceCodeList(ContextUtil.getTenant());
        return ResponseData.success(Arrays.stream(ResourceCodeEnum.values())
                .filter(o -> allowedList.contains(o.name()))
                .map(ResourceVO::newInstance)
                .collect(Collectors.toList()));
    }
}
