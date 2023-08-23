package com.eversis.esa.geoss.curated.relations.controller;

import com.eversis.esa.geoss.curated.relations.service.WorkflowRelationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The type Workflow controller.
 */
@Log4j2
@BasePathAwareController("/workflow/relation")
@ResponseBody
@Tag(name = "workflow")
public class WorkflowRelationController {

    private final WorkflowRelationService workflowService;

    /**
     * Instantiates a new Workflow controller.
     *
     * @param workflowService the workflow service
     */
    public WorkflowRelationController(WorkflowRelationService workflowService) {
        this.workflowService = workflowService;
    }

    /**
     * Pending user relation.
     *
     * @param userRelationId the user relation id
     * @param host the host
     */
    @PreAuthorize("hasAnyRole('RELATION_WRITER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/pending/{userRelationId}")
    public void pendingUserRelation(@PathVariable long userRelationId, @RequestHeader String host) {
        log.info("Pending user relation");
        workflowService.pendingUserRelation(userRelationId, host);
    }

    /**
     * Approve user relation.
     *
     * @param userRelationId the user relation id
     * @param host the host
     */
    @PreAuthorize("hasAnyRole('RELATION_WRITER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/approve/{userRelationId}")
    public void approveUserRelation(@PathVariable long userRelationId, @RequestHeader String host) {
        log.info("Approve user relation");
        workflowService.approveUserRelation(userRelationId, host);
    }

    /**
     * Deny user relation.
     *
     * @param userRelationId the user relation id
     * @param host the host
     */
    @PreAuthorize("hasAnyRole('RELATION_REVIEWER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/deny/{userRelationId}")
    public void denyUserRelation(@PathVariable long userRelationId, @RequestHeader String host) {
        log.info("Deny user relation");
        workflowService.denyUserRelation(userRelationId, host);
    }

    /**
     * Delete user relation.
     *
     * @param userRelationId the user relation id
     * @param host the host
     */
    @PreAuthorize("hasAnyRole('RELATION_REVIEWER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/delete/{userRelationId}")
    public void deleteUserRelation(@PathVariable long userRelationId, @RequestHeader String host) {
        log.info("Delete user relation");
        workflowService.deleteUserRelation(userRelationId, host);
    }

}
