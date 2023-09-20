package com.eversis.esa.geoss.curated.extensions.service;

import jakarta.validation.constraints.NotNull;

import com.eversis.esa.geoss.curated.extensions.domain.UserExtension;
import com.eversis.esa.geoss.curated.extensions.model.UserExtensionModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The interface User extension service.
 */
public interface UserExtensionService {

    /**
     * Find all user extensions page.
     *
     * @param pageable the pageable
     * @return the page
     */
    Page<UserExtension> findAllUserExtensions(@NotNull Pageable pageable);

    /**
     * Find user extension user extension.
     *
     * @param userExtensionId the user extension id
     * @return the user extension
     */
    UserExtension findUserExtension(long userExtensionId);

    /**
     * Find user extensions page.
     *
     * @param userId the user id
     * @param pageable the pageable
     * @return the page
     */
    Page<UserExtension> findUserExtensions(String userId, @NotNull Pageable pageable);

    /**
     * Create user extension.
     *
     * @param userExtensionDto the user extension dto
     */
    void createUserExtension(UserExtensionModel userExtensionDto);

    /**
     * Update user extension.
     *
     * @param userExtensionId the user extension id
     * @param userExtensionDto the user extension dto
     */
    void updateUserExtension(long userExtensionId, UserExtensionModel userExtensionDto);

    /**
     * Remove user extension.
     *
     * @param userExtensionId the user extension id
     */
    void removeUserExtension(long userExtensionId);

    /**
     * Delete user extension.
     *
     * @param userExtensionId the user extension id
     */
    void deleteUserExtension(long userExtensionId);

    /**
     * Restore user extension.
     *
     * @param userExtensionId the user extension id
     */
    void restoreUserExtension(long userExtensionId);

}
