package io.github.kennethfan.openapi.gateway.web.controller.inner;

import io.github.kennethfan.openapi.gateway.anno.NoneTenantRequired;
import io.github.kennethfan.openapi.gateway.context.ContextUtil;
import io.github.kennethfan.openapi.gateway.model.auth.TenantConfig;
import io.github.kennethfan.openapi.client.model.response.PagedData;
import io.github.kennethfan.openapi.client.model.response.ResponseData;
import io.github.kennethfan.openapi.gateway.service.TenantService;
import io.github.kennethfan.openapi.gateway.web.vo.auth.TenantAuthInfo;
import io.github.kennethfan.openapi.gateway.web.vo.auth.TenantBaseShowVO;
import io.github.kennethfan.openapi.gateway.web.vo.auth.TenantBaseVO;
import io.github.kennethfan.openapi.gateway.web.vo.auth.TenantSearchCondition;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/inner-api/tenant/", produces = "application/json; charset=utf-8")
public class TenantController {

    @Autowired
    private TenantService tenantService;

    @NoneTenantRequired
    @PostMapping("add")
    public ResponseData<Boolean> addTenant(@RequestBody TenantBaseVO tenantVO) {
        TenantConfig tenantConfig = tenantVO.toDO();
        tenantService.addTenantConfig(tenantConfig);

        return ResponseData.success(true);
    }

    @GetMapping("base-info")
    public ResponseData<TenantBaseShowVO> getTenantBaseInfo() {
        TenantConfig tenantConfig = tenantService.getByTenantId(ContextUtil.getTenant());
        return ResponseData.success(TenantBaseShowVO.fromDO(tenantConfig));
    }

    @NoneTenantRequired
    @PostMapping("page")
    public PagedData<TenantBaseShowVO> getTenantPage(@RequestBody TenantSearchCondition condition) {
        LambdaQueryWrapper<TenantConfig> queryWrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(condition.getTenantId())) {
            queryWrapper.eq(TenantConfig::getTenantId, condition.getTenantId());
        }
        if (StringUtils.isNotBlank(condition.getTenantName())) {
            queryWrapper.like(TenantConfig::getTenantName, condition.getTenantName());
        }
        if (StringUtils.isNotBlank(condition.getStatus())) {
            queryWrapper.eq(TenantConfig::getStatus, condition.getStatus());
        }

        long total = tenantService.countByCondition(queryWrapper);
        if (total <= condition.offset()) {
            return PagedData.success(condition, total, new ArrayList<>());
        }

        IPage<TenantConfig> resultPage = tenantService.pageByCondition(queryWrapper, new Page<>(condition.fixedPageNo(), condition.limit()));
        List<TenantBaseShowVO> recordList = Optional.ofNullable(resultPage.getRecords())
                .orElse(new ArrayList<>())
                .stream()
                .map(TenantBaseShowVO::fromDO)
                .collect(Collectors.toList());

        return PagedData.success(condition, total, recordList);
    }

    @NoneTenantRequired
    @PostMapping("update")
    public ResponseData<Boolean> updateTenant(@RequestBody TenantBaseVO tenantVO) {
        TenantConfig tenantConfig = tenantVO.toDO();
        tenantService.updateTenantConfig(tenantConfig);

        return ResponseData.success(true);
    }

    @PostMapping("valid")
    public ResponseData<Boolean> validTenant() {
        tenantService.validTenantConfig(ContextUtil.getTenant());

        return ResponseData.success(true);
    }

    @PostMapping("invalid")
    public ResponseData<Boolean> invalidTenant() {
        tenantService.invalidTenantConfig(ContextUtil.getTenant());

        return ResponseData.success(true);
    }

    @GetMapping("auth-info")
    public ResponseData<TenantAuthInfo> getTenantAuthInfo() {
        TenantConfig tenantConfig = tenantService.getByTenantId(ContextUtil.getTenant());
        return ResponseData.success(TenantAuthInfo.fromDO(tenantConfig));
    }

    @GetMapping(value = "download-private-key", produces = "application/octet-stream")
    public void getPrivateKey(HttpServletResponse response) throws IOException {
        TenantConfig tenantConfig = tenantService.getByTenantId(ContextUtil.getTenant());
        if (tenantConfig == null) {
            response.setHeader("content-length", "0");
            return;
        }

        response.setHeader("Content-Disposition", "attachement;filename=private.pem");

        String privateKey = tenantConfig.getPrivateKey();
        byte[] bytes = privateKey.getBytes(StandardCharsets.UTF_8);
        response.setHeader("content-length", String.valueOf(bytes.length));

        try (OutputStream outputStream = response.getOutputStream()) {
            outputStream.write(bytes);
            outputStream.flush();
        }
    }
}
