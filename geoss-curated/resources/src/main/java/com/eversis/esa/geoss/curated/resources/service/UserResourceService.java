package com.eversis.esa.geoss.curated.resources.service;

import com.eversis.esa.geoss.curated.resources.domain.UserResource;
import com.eversis.esa.geoss.curated.resources.model.UserResourceModel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import jakarta.validation.constraints.NotNull;

/**
 * The interface User resource service.
 */
public interface UserResourceService {

    /**
     * Find all user resources page.
     *
     * @param pageable the pageable
     * @return the page
     */
    Page<UserResource> findAllUserResources(@NotNull Pageable pageable);

    /**
     * Find all user resources page.
     *
     * @param userId the user id
     * @param pageable the pageable
     * @return the page
     */
    Page<UserResource> findAllUserResources(String userId, @NotNull Pageable pageable);

    /**
     * Find user resource user resource.
     *
     * @param userResourceId the user resource id
     * @return the user resource
     */
    UserResource findUserResource(long userResourceId);

    /**
     * Create user resource.
     *
     * @param userResourceDto the user resource dto
     */
    void createUserResource(UserResourceModel userResourceDto);

    /**
     * Update user resource.
     *
     * @param userResourceId the user resource id
     * @param userResourceDto the user resource dto
     */
    void updateUserResource(long userResourceId, UserResourceModel userResourceDto);

    /**
     * Remove user resource.
     *
     * @param userResourceId the user resource id
     */
    void removeUserResource(long userResourceId);

    /**
     * Delete user resource.
     *
     * @param userResourceId the user resource id
     */
    void deleteUserResource(long userResourceId);

    /**
     * Restore user resource.
     *
     * @param userResourceId the user resource id
     */
    void restoreUserResource(long userResourceId);

    /**
     * Approve user resource user resource.
     *
     * @param userResourceId the user resource id
     * @return the user resource
     */
    UserResource approveUserResource(long userResourceId);

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

}
