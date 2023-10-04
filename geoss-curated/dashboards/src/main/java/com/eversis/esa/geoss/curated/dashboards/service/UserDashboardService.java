package com.eversis.esa.geoss.curated.dashboards.service;

import jakarta.validation.constraints.NotNull;

import com.eversis.esa.geoss.curated.dashboards.domain.UserDashboard;
import com.eversis.esa.geoss.curated.dashboards.model.UserDashboardModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The interface User dashboard service.
 */
public interface UserDashboardService {

    /**
     * Find all user dashboards page.
     *
     * @param pageable the pageable
     * @return the page
     */
    Page<UserDashboard> findAllUserDashboards(@NotNull Pageable pageable);

    /**
     * Find user dashboard user dashboard.
     *
     * @param userDashboardId the user dashboard id
     * @return the user dashboard
     */
    UserDashboard findUserDashboard(long userDashboardId);

    /**
     * Find user dashboards page.
     *
     * @param userId the user id
     * @param pageable the pageable
     * @return the page
     */
    Page<UserDashboard> findUserDashboards(String userId, @NotNull Pageable pageable);

    /**
     * Create user dashboard.
     *
     * @param userDashboardModel the user dashboard model
     */
    void createUserDashboard(UserDashboardModel userDashboardModel);

    /**
     * Update user dashboard.
     *
     * @param userDashboardId the user dashboard id
     * @param userDashboardModel the user dashboard model
     */
    void updateUserDashboard(long userDashboardId, UserDashboardModel userDashboardModel);

    /**
     * Remove user dashboard.
     *
     * @param userDashboardId the user dashboard id
     */
    void removeUserDashboard(long userDashboardId);

    /**
     * Delete user dashboard.
     *
     * @param userDashboardId the user dashboard id
     */
    void deleteUserDashboard(long userDashboardId);

    /**
     * Restore user dashboard.
     *
     * @param userDashboardId the user dashboard id
     */
    void restoreUserDashboard(long userDashboardId);

    /**
     * Approve user dashboard user dashboard.
     *
     * @param userDashboardId the user dashboard id
     * @return the user dashboard
     */
    UserDashboard approveUserDashboard(long userDashboardId);

    /**
     * Deny user dashboard.
     *
     * @param userDashboardId the user dashboard id
     */
    void denyUserDashboard(long userDashboardId);

    /**
     * Pending user dashboard.
     *
     * @param userDashboardId the user dashboard id
     */
    void pendingUserDashboard(long userDashboardId);

}
