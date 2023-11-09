package io.github.kennethfan.openapi.gateway.service;

import java.util.Collection;
import java.util.Set;

public interface TenantAuthService {

    void addTenantAuth(String tenantId, Collection<String> resourceCodeList);

    Set<String> getAllowedResourceCodeList(String tenantId);
}
