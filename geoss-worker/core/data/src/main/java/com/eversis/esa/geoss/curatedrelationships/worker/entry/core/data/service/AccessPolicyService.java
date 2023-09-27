package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.service;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.model.AccessPolicy;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.repository.AccessPolicyRepository;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.service.mapper.AccessPolicyMapper;

import org.springframework.stereotype.Service;

/**
 * The type Access policy service.
 */
@Service
public class AccessPolicyService {

    private final AccessPolicyRepository accessPolicyRepository;

    /**
     * Instantiates a new Access policy service.
     *
     * @param accessPolicyRepository the access policy repository
     */
    public AccessPolicyService(AccessPolicyRepository accessPolicyRepository) {
        this.accessPolicyRepository = accessPolicyRepository;
    }

    /**
     * Gets or create access policy.
     *
     * @param accessPolicy the access policy
     * @return the or create access policy
     */
    public AccessPolicy getOrCreateAccessPolicy(
            com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.AccessPolicy accessPolicy) {
        return accessPolicyRepository.findByCode(accessPolicy.getValue())
                .orElseGet(() -> accessPolicyRepository.save(AccessPolicyMapper.mapAccessPolicy(accessPolicy)));
    }

}
