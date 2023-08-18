package com.eversis.esa.geoss.curated.resources.service;

/**
 * The interface Workflow service.
 */
public interface WorkflowService {

    /**
     * Approve user resource.
     *
     * @param userResourceId the user resource id
     * @param host the host
     */
    void approveUserResource(long userResourceId, String host);

    /**
     * Deny user resource.
     *
     * @param userResourceId the user resource id
     * @param host the host
     */
    void denyUserResource(long userResourceId, String host);

    /**
     * Pending user resource.
     *
     * @param userResourceId the user resource id
     * @param host the host
     */
    void pendingUserResource(long userResourceId, String host);

    /**
     * Delete user resource.
     *
     * @param userResourceId the user resource id
     * @param host the host
     */
    void deleteUserResource(long userResourceId, String host);

}
