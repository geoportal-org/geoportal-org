package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.service.mapper;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.model.AccessPolicy;

/**
 * The type Access policy mapper.
 */
public class AccessPolicyMapper {

    private AccessPolicyMapper() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Map access policy access policy.
     *
     * @param domainAccessPolicy the domain access policy
     * @return the access policy
     */
    public static AccessPolicy mapAccessPolicy(
            com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.AccessPolicy domainAccessPolicy) {
        AccessPolicy dbAccessPolicy = new AccessPolicy(domainAccessPolicy.getValue());
        dbAccessPolicy.setName(domainAccessPolicy.getName());
        return dbAccessPolicy;
    }

}
