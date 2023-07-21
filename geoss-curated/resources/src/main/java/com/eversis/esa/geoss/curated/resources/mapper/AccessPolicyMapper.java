package com.eversis.esa.geoss.curated.resources.mapper;

import com.eversis.esa.geoss.curated.resources.domain.AccessPolicy;
import com.eversis.esa.geoss.curated.resources.model.AccessPolicyModel;

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
    public static AccessPolicy mapAccessPolicy(AccessPolicyModel domainAccessPolicy) {
        AccessPolicy dbAccessPolicy = new AccessPolicy(domainAccessPolicy.getCode());
        dbAccessPolicy.setName(domainAccessPolicy.getName());
        dbAccessPolicy.setIsCustom(1);
        return dbAccessPolicy;
    }

}
