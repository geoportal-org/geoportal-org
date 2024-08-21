package com.eversis.esa.geoss.curated.resources.service.impl;

import com.eversis.esa.geoss.curated.resources.domain.AccessPolicy;
import com.eversis.esa.geoss.curated.resources.model.AccessPolicyModel;
import com.eversis.esa.geoss.curated.resources.repository.AccessPolicyRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * The type Access policy service impl test.
 */
@ContextConfiguration(classes = {AccessPolicyServiceImpl.class})
@ExtendWith(SpringExtension.class)
class AccessPolicyServiceImplTest {

    @MockBean
    private AccessPolicyRepository accessPolicyRepository;

    @Autowired
    private AccessPolicyServiceImpl accessPolicyServiceImpl;

    /**
     * Test get or create access policy.
     */
    @Test
    void testGetOrCreateAccessPolicy() {
        AccessPolicy accessPolicy = new AccessPolicy();
        accessPolicy.setCode("open");
        accessPolicy.setId(1L);
        accessPolicy.setIsCustom(0);
        accessPolicy.setName("open");
        Optional<AccessPolicy> ofResult = Optional.of(accessPolicy);
        when(accessPolicyRepository.findByCode(Mockito.<String>any())).thenReturn(ofResult);

        AccessPolicyModel accessPolicy2 = new AccessPolicyModel();
        accessPolicy2.setCode("open");
        accessPolicy2.setName("open");
        AccessPolicy actualOrCreateAccessPolicy = accessPolicyServiceImpl.getOrCreateAccessPolicy(accessPolicy2);
        verify(accessPolicyRepository).findByCode(Mockito.<String>any());
        assertSame(accessPolicy, actualOrCreateAccessPolicy);
    }

    /**
     * Test get or create access policy 2.
     */
    @Test
    void testGetOrCreateAccessPolicy2() {
        AccessPolicy accessPolicy = new AccessPolicy();
        accessPolicy.setCode("open");
        accessPolicy.setId(1L);
        accessPolicy.setIsCustom(1);
        accessPolicy.setName("open");
        when(accessPolicyRepository.save(Mockito.<AccessPolicy>any())).thenReturn(accessPolicy);
        Optional<AccessPolicy> emptyResult = Optional.empty();
        when(accessPolicyRepository.findByCode(Mockito.<String>any())).thenReturn(emptyResult);

        AccessPolicyModel accessPolicy2 = new AccessPolicyModel();
        accessPolicy2.setCode("open");
        accessPolicy2.setName("open");
        AccessPolicy actualOrCreateAccessPolicy = accessPolicyServiceImpl.getOrCreateAccessPolicy(accessPolicy2);
        verify(accessPolicyRepository).findByCode(Mockito.<String>any());
        verify(accessPolicyRepository).save(Mockito.<AccessPolicy>any());
        assertSame(accessPolicy, actualOrCreateAccessPolicy);
    }

}
