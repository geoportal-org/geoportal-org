package com.eversis.esa.geoss.curated.workflow.controller;

import com.eversis.esa.geoss.curated.workflow.service.WorkflowService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
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
     * @param host the host
     */
    @PreAuthorize("hasAnyRole('RESOURCE_WRITER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/resource/pending/{userResourceId}")
    public void pendingUserResource(@PathVariable long userResourceId, @RequestHeader String host) {
        log.info("Pending user resource");
        workflowService.pendingUserResource(userResourceId, host);
    }

    /**
     * Approve user resource.
     *
     * @param userResourceId the user resource id
     * @param host the host
     */
    @PreAuthorize("hasAnyRole('RESOURCE_REVIEWER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/resource/approve/{userResourceId}")
    public void approveUserResource(@PathVariable long userResourceId, @RequestHeader String host) {
        log.info("Approve user resource");
        workflowService.approveUserResource(userResourceId, host);
    }

    /**
     * Deny user resource.
     *
     * @param userResourceId the user resource id
     * @param host the host
     */
    @PreAuthorize("hasAnyRole('RESOURCE_REVIEWER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/resource/deny/{userResourceId}")
    public void denyUserResource(@PathVariable long userResourceId, @RequestHeader String host) {
        log.info("Deny user resource");
        workflowService.denyUserResource(userResourceId, host);
    }

    /**
     * Delete user resource.
     *
     * @param userResourceId the user resource id
     * @param host the host
     */
    @PreAuthorize("hasAnyRole('RESOURCE_REVIEWER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/resource/delete/{userResourceId}")
    public void deleteUserResource(@PathVariable long userResourceId, @RequestHeader String host) {
        log.info("Delete user resource");
        workflowService.deleteUserResource(userResourceId, host);
    }

    /**
     * Pending user relation.
     *
     * @param userRelationId the user relation id
     * @param host the host
     */
    @PreAuthorize("hasAnyRole('RELATION_WRITER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/relation/pending/{userRelationId}")
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
    @PreAuthorize("hasAnyRole('RELATION_REVIEWER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/relation/approve/{userRelationId}")
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
    @PostMapping("/relation/deny/{userRelationId}")
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
    @DeleteMapping("/relation/delete/{userRelationId}")
    public void deleteUserRelation(@PathVariable long userRelationId, @RequestHeader String host) {
        log.info("Delete user relation");
        workflowService.deleteUserRelation(userRelationId, host);
    }

    /**
     * Pending user extension.
     *
     * @param userExtensionId the user extension id
     * @param host the host
     */
    @PreAuthorize("hasAnyRole('EXTENSION_WRITER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/extension/pending/{userExtensionId}")
    public void pendingUserExtension(@PathVariable long userExtensionId, @RequestHeader String host) {
        log.info("Pending user extension");
        workflowService.pendingUserExtension(userExtensionId, host);
    }

    /**
     * Approve user extension.
     *
     * @param userExtensionId the user extension id
     * @param host the host
     */
    @PreAuthorize("hasAnyRole('EXTENSION_REVIEWER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/extension/approve/{userExtensionId}")
    public void approveUserExtension(@PathVariable long userExtensionId, @RequestHeader String host) {
        log.info("Approve user extension");
        workflowService.approveUserExtension(userExtensionId, host);
    }

    /**
     * Deny user extension.
     *
     * @param userExtensionId the user extension id
     * @param host the host
     */
    @PreAuthorize("hasAnyRole('EXTENSION_REVIEWER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/extension/deny/{userExtensionId}")
    public void denyUserExtension(@PathVariable long userExtensionId, @RequestHeader String host) {
        log.info("Deny user extension");
        workflowService.denyUserExtension(userExtensionId, host);
    }

    /**
     * Delete user extension.
     *
     * @param userExtensionId the user extension id
     * @param host the host
     */
    @PreAuthorize("hasAnyRole('EXTENSION_REVIEWER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/extension/delete/{userExtensionId}")
    public void deleteUserExtension(@PathVariable long userExtensionId, @RequestHeader String host) {
        log.info("Delete user extension");
        workflowService.deleteUserExtension(userExtensionId, host);
    }

    /**
     * Pending user dashboard.
     *
     * @param userDashboardId the user dashboard id
     * @param host the host
     */
    @PreAuthorize("hasAnyRole('DASHBOARD_WRITER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/dashboard/pending/{userDashboardId}")
    public void pendingUserDashboard(@PathVariable long userDashboardId, @RequestHeader String host) {
        log.info("Pending user dashboard");
        workflowService.pendingUserDashboard(userDashboardId, host);
    }

    /**
     * Approve user dashboard.
     *
     * @param userDashboardId the user dashboard id
     * @param host the host
     */
    @PreAuthorize("hasAnyRole('DASHBOARD_REVIEWER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/dashboard/approve/{userDashboardId}")
    public void approveUserDashboard(@PathVariable long userDashboardId, @RequestHeader String host) {
        log.info("Approve user dashboard");
        workflowService.approveUserDashboard(userDashboardId, host);
    }

    /**
     * Deny user dashboard.
     *
     * @param userDashboardId the user dashboard id
     * @param host the host
     */
    @PreAuthorize("hasAnyRole('DASHBOARD_REVIEWER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/dashboard/deny/{userDashboardId}")
    public void denyUserDashboard(@PathVariable long userDashboardId, @RequestHeader String host) {
        log.info("Deny user dashboard");
        workflowService.denyUserDashboard(userDashboardId, host);
    }

    /**
     * Delete user dashboard.
     *
     * @param userDashboardId the user dashboard id
     * @param host the host
     */
    @PreAuthorize("hasAnyRole('DASHBOARD_REVIEWER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/dashboard/delete/{userDashboardId}")
    public void deleteUserDashboard(@PathVariable long userDashboardId, @RequestHeader String host) {
        log.info("Delete user dashboard");
        workflowService.deleteUserDashboard(userDashboardId, host);
    }

    /**
     * Delete entry.
     *
     * @param entryId the entry id
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping("/entry/delete/{entryId}")
    public void deleteEntry(@PathVariable long entryId) {
        log.info("Delete entry");
        workflowService.deleteEntry(entryId);
    }

    /**
     * Delete entry extension.
     *
     * @param entryExtensionId the entry extension id
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping("/extension/delete/{entryExtensionId}")
    public void deleteEntryExtension(@PathVariable long entryExtensionId) {
        log.info("Delete entry extension");
        workflowService.deleteEntryExtension(entryExtensionId);
    }

}
