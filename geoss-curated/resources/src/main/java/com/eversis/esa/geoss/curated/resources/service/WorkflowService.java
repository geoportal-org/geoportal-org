package com.eversis.esa.geoss.curated.resources.service;

/**
 * The interface Workflow service.
 */
public interface WorkflowService {

    /**
     * Approve user resource.
     *
     * @param userResourceId the user resource id
     */
    void approveUserResource(long userResourceId);

    /**
     * Deny user resource.
     *
     * @param userResourceId the user resource id
     */
    void denyUserResource(long userResourceId);

    /**
     * Pending user resource.
     *
     * @param userResourceId the user resource id
     */
    void pendingUserResource(long userResourceId);

    /**
     * Delete user resource.
     *
     * @param userResourceId the user resource id
     */
    void deleteUserResource(long userResourceId);
}
