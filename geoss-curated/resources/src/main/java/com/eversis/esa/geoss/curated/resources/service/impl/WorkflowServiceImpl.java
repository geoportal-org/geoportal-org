package com.eversis.esa.geoss.curated.resources.service.impl;

import com.eversis.esa.geoss.curated.resources.domain.UserResource;
import com.eversis.esa.geoss.curated.resources.elasticsearch.service.ElasticsearchService;
import com.eversis.esa.geoss.curated.resources.service.UserResourceService;
import com.eversis.esa.geoss.curated.resources.service.WorkflowService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 * The type Workflow service.
 */
@Log4j2
@Service
@Transactional(readOnly = true)
@Validated
public class WorkflowServiceImpl implements WorkflowService {

    private final UserResourceService userResourceService;

    private final ElasticsearchService elasticsearchService;

    /**
     * Instantiates a new Workflow service.
     *
     * @param userResourceService the user resource service
     * @param elasticsearchService the elasticsearch service
     */
    public WorkflowServiceImpl(UserResourceService userResourceService, ElasticsearchService elasticsearchService) {
        this.userResourceService = userResourceService;
        this.elasticsearchService = elasticsearchService;
    }

    @Transactional
    @Override
    public void approveUserResource(long userResourceId) {
        log.info("Workflow approving user resource with id {}", userResourceId);
        UserResource userResource = userResourceService.approveUserResource(userResourceId);
        elasticsearchService.indexEntry(userResource.getEntry());
        log.info("Workflow approved user resource.");
    }

    @Override
    public void denyUserResource(long userResourceId) {
        log.info("Workflow denying user resource with id {}", userResourceId);
        userResourceService.denyUserResource(userResourceId);
        log.info("Workflow denied user resource.");
    }

    @Override
    public void pendingUserResource(long userResourceId) {
        log.info("Workflow pending user resource with id {}", userResourceId);
        userResourceService.pendingUserResource(userResourceId);
        log.info("Workflow pending user resource.");
    }

    @Transactional
    @Override
    public void deleteUserResource(long userResourceId) {
        log.info("Workflow delete user resource with id {}", userResourceId);
        UserResource userResource = userResourceService.findUserResource(userResourceId);
        elasticsearchService.removeEntryFromIndex(userResource.getEntry());
        userResourceService.deleteUserResource(userResourceId);
        log.info("Workflow delete user resource.");
    }

}
