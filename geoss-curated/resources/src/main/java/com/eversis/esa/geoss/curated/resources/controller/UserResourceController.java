package com.eversis.esa.geoss.curated.resources.controller;

import com.eversis.esa.geoss.curated.resources.domain.UserResource;
import com.eversis.esa.geoss.curated.resources.model.UserResourceModel;
import com.eversis.esa.geoss.curated.resources.service.UserResourceService;

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

import jakarta.validation.Valid;

/**
 * The type User resource controller.
 */
@Log4j2
@RequiredArgsConstructor
@BasePathAwareController("/userResources")
@ResponseBody
@Tag(name = "userResources")
public class UserResourceController {

    private final UserResourceService userResourceService;

    /**
     * Find all user resources page.
     *
     * @param page the page
     * @param size the size
     * @return the page
     */
    @PreAuthorize("hasAnyRole('RESOURCE_READER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Page<UserResource> findAllUserResources(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Find all user resources page: {}, size: {}", page, size);
        return userResourceService.findAllUserResources(PageRequest.of(page, size));
    }

    /**
     * Find user resource user resource.
     *
     * @param userResourceId the user resource id
     * @return the user resource
     */
    @PreAuthorize("hasAnyRole('RESOURCE_READER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{userResourceId}")
    public UserResource findUserResource(@PathVariable long userResourceId) {
        log.info("Find user resource");
        return userResourceService.findUserResource(userResourceId);
    }

    /**
     * Find user resources page.
     *
     * @param userId the user id
     * @param page the page
     * @param size the size
     * @return the page
     */
    @PreAuthorize("hasAnyRole('RESOURCE_READER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/user/{userId}")
    public Page<UserResource> findUserResources(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Find user resources");
        return userResourceService.findAllUserResources(userId, PageRequest.of(page, size));
    }

    /**
     * Create user resource.
     *
     * @param userResourceDto the user resource dto
     */
    @PreAuthorize("hasAnyRole('RESOURCE_WRITER', 'ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createUserResource(@RequestBody @Valid UserResourceModel userResourceDto) {
        log.info("Create user resource");
        userResourceService.createUserResource(userResourceDto);
    }

    /**
     * Update user resource.
     *
     * @param userResourceId the user resource id
     * @param userResourceDto the user resource dto
     */
    @PreAuthorize("hasAnyRole('RESOURCE_WRITER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{userResourceId}")
    public void updateUserResource(
            @PathVariable long userResourceId,
            @RequestBody @Valid UserResourceModel userResourceDto) {
        log.info("Update user resource");
        userResourceService.updateUserResource(userResourceId, userResourceDto);
    }

    /**
     * Remove user resource.
     *
     * @param userResourceId the user resource id
     */
    @PreAuthorize("hasAnyRole('RESOURCE_REMOVER', 'ADMIN')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping("/{userResourceId}")
    public void removeUserResource(@PathVariable long userResourceId) {
        log.info("Remove user resource");
        userResourceService.removeUserResource(userResourceId);
    }

    /**
     * Delete user resource.
     *
     * @param userResourceId the user resource id
     */
    @PreAuthorize("hasAnyRole('RESOURCE_DELETER', 'ADMIN')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping("/delete/{userResourceId}")
    public void deleteUserResource(@PathVariable long userResourceId) {
        log.info("Delete user resource");
        userResourceService.deleteUserResource(userResourceId);
    }

    /**
     * Restore user resource.
     *
     * @param userResourceId the user resource id
     */
    @PreAuthorize("hasAnyRole('RESOURCE_REMOVER', 'ADMIN')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping("/restore/{userResourceId}")
    public void restoreUserResource(@PathVariable long userResourceId) {
        log.info("Restore user resource");
        userResourceService.restoreUserResource(userResourceId);
    }

}
