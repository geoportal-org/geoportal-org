package com.eversis.esa.geoss.curated.resources.controller;

import com.eversis.esa.geoss.curated.resources.domain.AccessPolicy;
import com.eversis.esa.geoss.curated.resources.repository.AccessPolicyRepository;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

/**
 * The type Access policy controller.
 */
@Log4j2
@BasePathAwareController("/accessPolicy")
@ResponseBody
@Tag(name = "accessPolicy")
public class AccessPolicyController {

    /**
     * The Access policy repository.
     */
    private final AccessPolicyRepository accessPolicyRepository;

    /**
     * Instantiates a new Access policy controller.
     *
     * @param accessPolicyRepository the access policy repository
     */
    public AccessPolicyController(AccessPolicyRepository accessPolicyRepository) {
        this.accessPolicyRepository = accessPolicyRepository;
    }

    /**
     * Gets access policy values.
     *
     * @return the access policy values
     */
    @PreAuthorize("hasAnyRole('RESOURCE_WRITER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<AccessPolicy> getAccessPolicyValues() {
        log.info("Getting access policy values...");
        return accessPolicyRepository.findByIsCustomOrderByName(0);
    }

}
