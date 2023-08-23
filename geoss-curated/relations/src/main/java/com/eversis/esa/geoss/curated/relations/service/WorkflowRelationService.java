package com.eversis.esa.geoss.curated.relations.service;

/**
 * The interface Workflow service.
 */
public interface WorkflowRelationService {

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

}
