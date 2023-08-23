package com.eversis.esa.geoss.curated.relations.service;

import jakarta.validation.constraints.NotNull;

import com.eversis.esa.geoss.curated.relations.domain.UserRelation;
import com.eversis.esa.geoss.curated.relations.model.UserRelationModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The interface User relation service.
 */
public interface UserRelationService {

    /**
     * Find all user relations page.
     *
     * @param pageable the pageable
     * @return the page
     */
    Page<UserRelation> findAllUserRelations(@NotNull Pageable pageable);

    /**
     * Find user relation.
     *
     * @param userRelationId the user relation id
     * @return the user relation
     */
    UserRelation findUserRelation(long userRelationId);

    /**
     * Find all user relations page.
     *
     * @param userId the user id
     * @param pageable the pageable
     * @return the page
     */
    Page<UserRelation> findAllUserRelations(String userId, Pageable pageable);

    /**
     * Create user relation.
     *
     * @param userRelationDto the user relation dto
     */
    void createUserRelation(UserRelationModel userRelationDto);

    /**
     * Update user relation.
     *
     * @param userRelationId the user relation id
     * @param userRelationDto the user relation dto
     */
    void updateUserRelation(long userRelationId, UserRelationModel userRelationDto);

    /**
     * Remove user relation.
     *
     * @param userRelationId the user relation id
     */
    void removeUserRelation(long userRelationId);

    /**
     * Delete user relation.
     *
     * @param userRelationId the user relation id
     */
    void deleteUserRelation(long userRelationId);

    /**
     * Restore user relation.
     *
     * @param userRelationId the user relation id
     */
    void restoreUserRelation(long userRelationId);

    /**
     * Approve user resource user relation.
     *
     * @param userRelationId the user relation id
     * @return the user relation
     */
    UserRelation approveUserResource(long userRelationId);

    /**
     * Deny user relation.
     *
     * @param userRelationId the user relation id
     */
    void denyUserRelation(long userRelationId);

    /**
     * Pending user relation.
     *
     * @param userRelationId the user relation id
     */
    void pendingUserRelation(long userRelationId);

}
