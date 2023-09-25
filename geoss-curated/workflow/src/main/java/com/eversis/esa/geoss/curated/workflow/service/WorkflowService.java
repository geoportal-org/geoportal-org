package com.eversis.esa.geoss.curated.workflow.service;

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

    /**
     * Pending user relation.
     *
     * @param userRelationId the user relation id
     * @param host the host
     */
    void pendingUserRelation(long userRelationId, String host);

    /**
     * Approve user relation.
     *
     * @param userRelationId the user relation id
     * @param host the host
     */
    void approveUserRelation(long userRelationId, String host);

    /**
     * Deny user relation.
     *
     * @param userRelationId the user relation id
     * @param host the host
     */
    void denyUserRelation(long userRelationId, String host);

    /**
     * Delete user relation.
     *
     * @param userRelationId the user relation id
     * @param host the host
     */
    void deleteUserRelation(long userRelationId, String host);

    /**
     * Pending user extension.
     *
     * @param userExtensionId the user extension id
     * @param host the host
     */
    void pendingUserExtension(long userExtensionId, String host);

    /**
     * Approve user extension.
     *
     * @param userExtensionId the user extension id
     * @param host the host
     */
    void approveUserExtension(long userExtensionId, String host);

    /**
     * Deny user extension.
     *
     * @param userExtensionId the user extension id
     * @param host the host
     */
    void denyUserExtension(long userExtensionId, String host);

    /**
     * Delete user extension.
     *
     * @param userExtensionId the user extension id
     * @param host the host
     */
    void deleteUserExtension(long userExtensionId, String host);

}
