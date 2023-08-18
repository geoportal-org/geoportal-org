package com.eversis.esa.geoss.curated.resources.controller;

import com.eversis.esa.geoss.curated.resources.service.WorkflowService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@BasePathAwareController("/workflow")
@ResponseBody
@Tag(name = "workflow")
public class WorkflowController {

    private final WorkflowService workflowService;

    /**
     * Pending user resource.
     *
     * @param userResourceId the user resource id
     */
    @PreAuthorize("hasAnyRole('RESOURCE_WRITER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/pending/{userResourceId}")
    public void pendingUserResource(@PathVariable long userResourceId, @RequestHeader String host) {
        log.info("Pending user resource");
        workflowService.pendingUserResource(userResourceId, host);
    }

    /**
     * Approve user resource.
     *
     * @param userResourceId the user resource id
     */
    @PreAuthorize("hasAnyRole('RESOURCE_REVIEWER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/approve/{userResourceId}")
    public void approveUserResource(@PathVariable long userResourceId, @RequestHeader String host) {
        log.info("Approve user resource");
        workflowService.approveUserResource(userResourceId, host);
    }

    /**
     * Deny user resource.
     *
     * @param userResourceId the user resource id
     */
    @PreAuthorize("hasAnyRole('RESOURCE_REVIEWER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/deny/{userResourceId}")
    public void denyUserResource(@PathVariable long userResourceId, @RequestHeader String host) {
        log.info("Deny user resource");
        workflowService.denyUserResource(userResourceId, host);
    }

    /**
     * Delete user resource.
     *
     * @param userResourceId the user resource id
     */
    @PreAuthorize("hasAnyRole('RESOURCE_REVIEWER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/delete/{userResourceId}")
    public void deleteUserResource(@PathVariable long userResourceId, @RequestHeader String host) {
        log.info("Delete user resource");
        workflowService.deleteUserResource(userResourceId, host);
    }

}
