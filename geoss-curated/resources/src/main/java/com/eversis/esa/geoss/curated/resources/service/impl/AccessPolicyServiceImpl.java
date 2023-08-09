package com.eversis.esa.geoss.curated.resources.service.impl;

import com.eversis.esa.geoss.curated.resources.domain.AccessPolicy;
import com.eversis.esa.geoss.curated.resources.mapper.AccessPolicyMapper;
import com.eversis.esa.geoss.curated.resources.model.AccessPolicyModel;
import com.eversis.esa.geoss.curated.resources.repository.AccessPolicyRepository;
import com.eversis.esa.geoss.curated.resources.service.AccessPolicyService;

import org.springframework.stereotype.Service;

/**
 * The type Access policy service.
 */
@Service
public class AccessPolicyServiceImpl implements AccessPolicyService {

    private final AccessPolicyRepository accessPolicyRepository;

    /**
     * Instantiates a new Access policy service.
     *
     * @param accessPolicyRepository the access policy repository
     */
    public AccessPolicyServiceImpl(AccessPolicyRepository accessPolicyRepository) {
        this.accessPolicyRepository = accessPolicyRepository;
    }

    @Override
    public AccessPolicy getOrCreateAccessPolicy(AccessPolicyModel accessPolicy) {
        return accessPolicyRepository.findByCode(accessPolicy.getCode())
                .orElseGet(() -> accessPolicyRepository.save(AccessPolicyMapper.mapAccessPolicy(accessPolicy)));
    }

}
