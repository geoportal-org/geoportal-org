package com.eversis.esa.geoss.curated.resources.service;

import com.eversis.esa.geoss.curated.resources.domain.AccessPolicy;
import com.eversis.esa.geoss.curated.resources.model.AccessPolicyModel;

/**
 * The interface Access policy service.
 */
public interface AccessPolicyService {

    /**
     * Gets or create access policy.
     *
     * @param accessPolicy the access policy
     * @return the or create access policy
     */
    AccessPolicy getOrCreateAccessPolicy(AccessPolicyModel accessPolicy);

}
