package com.eversis.esa.geoss.curated.dashboards.controller;

import jakarta.validation.Valid;

import com.eversis.esa.geoss.curated.dashboards.domain.UserDashboard;
import com.eversis.esa.geoss.curated.dashboards.model.UserDashboardModel;
import com.eversis.esa.geoss.curated.dashboards.service.UserDashboardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The type User dashboard controller.
 */
@Log4j2
@RequiredArgsConstructor
@BasePathAwareController("/userDashboards")
@ResponseBody
@Tag(name = "userDashboards")
public class UserDashboardController {

    private final UserDashboardService userDashboardService;

    /**
     * Find all user dashboards page.
     *
     * @param page the page
     * @param size the size
     * @return the page
     */
    @PreAuthorize("hasAnyRole('DASHBOARD_READER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Page<UserDashboard> findAllUserDashboards(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Find all user dashboards page: {}, size: {}", page, size);
        return userDashboardService.findAllUserDashboards(PageRequest.of(page, size));
    }

    /**
     * Find user dashboard user dashboard.
     *
     * @param userDashboardId the user dashboard id
     * @return the user dashboard
     */
    @PreAuthorize("hasAnyRole('DASHBOARD_READER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{userDashboardId}")
    public UserDashboard findUserDashboard(@PathVariable long userDashboardId) {
        log.info("Find user dashboard");
        return userDashboardService.findUserDashboard(userDashboardId);
    }

    /**
     * Find user dashboards page.
     *
     * @param userId the user id
     * @param page the page
     * @param size the size
     * @return the page
     */
    @PreAuthorize("hasAnyRole('DASHBOARD_READER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/user/{userId}")
    public Page<UserDashboard> findUserDashboards(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Find user dashboards");
        return userDashboardService.findUserDashboards(userId, PageRequest.of(page, size));
    }

    /**
     * Create user dashboard.
     *
     * @param userDashboardModel the user dashboard model
     */
    @PreAuthorize("hasAnyRole('DASHBOARD_WRITER', 'ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createUserDashboard(@RequestBody @Valid UserDashboardModel userDashboardModel) {
        log.info("Create user dashboard");
        userDashboardService.createUserDashboard(userDashboardModel);
    }

    /**
     * Update user dashboard.
     *
     * @param userDashboardId the user dashboard id
     * @param userDashboardModel the user dashboard model
     */
    @PreAuthorize("hasAnyRole('DASHBOARD_WRITER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{userDashboardId}")
    public void updateUserDashboard(
            @PathVariable long userDashboardId,
            @RequestBody @Valid UserDashboardModel userDashboardModel) {
        log.info("Update user dashboard");
        userDashboardService.updateUserDashboard(userDashboardId, userDashboardModel);
    }

    /**
     * Remove user dashboard.
     *
     * @param userDashboardId the user dashboard id
     */
    @PreAuthorize("hasAnyRole('DASHBOARD_REMOVER', 'ADMIN')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping("/{userDashboardId}")
    public void removeUserDashboard(@PathVariable long userDashboardId) {
        log.info("Remove user dashboard");
        userDashboardService.removeUserDashboard(userDashboardId);
    }

    /**
     * Delete user dashboard.
     *
     * @param userDashboardId the user dashboard id
     */
    @PreAuthorize("hasAnyRole('DASHBOARD_DELETER', 'ADMIN')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping("/delete/{userDashboardId}")
    public void deleteUserDashboard(@PathVariable long userDashboardId) {
        log.info("Delete user dashboard");
        userDashboardService.deleteUserDashboard(userDashboardId);
    }

    /**
     * Restore user dashboard.
     *
     * @param userDashboardId the user dashboard id
     */
    @PreAuthorize("hasAnyRole('DASHBOARD_REMOVER', 'ADMIN')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping("/restore/{userDashboardId}")
    public void restoreUserDashboard(@PathVariable long userDashboardId) {
        log.info("Restore user dashboard");
        userDashboardService.restoreUserDashboard(userDashboardId);
    }

}
