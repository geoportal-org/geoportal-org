package com.eversis.esa.geoss.curated.extensions.controller;

import com.eversis.esa.geoss.curated.extensions.domain.UserExtension;
import com.eversis.esa.geoss.curated.extensions.model.UserExtensionModel;
import com.eversis.esa.geoss.curated.extensions.service.UserExtensionService;

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
 * The type User extension controller.
 */
@Log4j2
@RequiredArgsConstructor
@BasePathAwareController("/userExtensions")
@ResponseBody
@Tag(name = "userExtensions")
public class UserExtensionController {

    private final UserExtensionService userExtensionService;

    /**
     * Find all user extensions page.
     *
     * @param page the page
     * @param size the size
     * @return the page
     */
    @PreAuthorize("hasAnyRole('EXTENSION_READER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Page<UserExtension> findAllUserExtensions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Find all user extensions page: {}, size: {}", page, size);
        return userExtensionService.findAllUserExtensions(PageRequest.of(page, size));
    }

    /**
     * Find user extension user extension.
     *
     * @param userExtensionId the user extension id
     * @return the user extension
     */
    @PreAuthorize("hasAnyRole('EXTENSION_READER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{userExtensionId}")
    public UserExtension findUserExtension(@PathVariable long userExtensionId) {
        log.info("Find user extension");
        return userExtensionService.findUserExtension(userExtensionId);
    }

    /**
     * Find user extensions page.
     *
     * @param userId the user id
     * @param page the page
     * @param size the size
     * @return the page
     */
    @PreAuthorize("hasAnyRole('EXTENSION_READER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/user/{userId}")
    public Page<UserExtension> findUserExtensions(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Find user extensions");
        return userExtensionService.findUserExtensions(userId, PageRequest.of(page, size));
    }

    /**
     * Create user extension.
     *
     * @param userExtensionDto the user extension dto
     */
    @PreAuthorize("hasAnyRole('EXTENSION_WRITER', 'ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createUserExtension(@RequestBody @Valid UserExtensionModel userExtensionDto) {
        log.info("Create user extension");
        userExtensionService.createUserExtension(userExtensionDto);
    }

    /**
     * Update user extension.
     *
     * @param userExtensionId the user extension id
     * @param userExtensionDto the user extension dto
     */
    @PreAuthorize("hasAnyRole('EXTENSION_WRITER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{userExtensionId}")
    public void updateUserExtension(
            @PathVariable long userExtensionId,
            @RequestBody @Valid UserExtensionModel userExtensionDto) {
        log.info("Update user extension");
        userExtensionService.updateUserExtension(userExtensionId, userExtensionDto);
    }

    /**
     * Remove user extension.
     *
     * @param userExtensionId the user extension id
     */
    @PreAuthorize("hasAnyRole('EXTENSION_REMOVER', 'ADMIN')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping("/{userExtensionId}")
    public void removeUserExtension(@PathVariable long userExtensionId) {
        log.info("Remove user extension");
        userExtensionService.removeUserExtension(userExtensionId);
    }

    /**
     * Delete user resource.
     *
     * @param userExtensionId the user extension id
     */
    @PreAuthorize("hasAnyRole('EXTENSION_DELETER', 'ADMIN')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping("/delete/{userExtensionId}")
    public void deleteUserResource(@PathVariable long userExtensionId) {
        log.info("Delete user extension");
        userExtensionService.deleteUserExtension(userExtensionId);
    }

    /**
     * Restore user extension.
     *
     * @param userExtensionId the user extension id
     */
    @PreAuthorize("hasAnyRole('EXTENSION_REMOVER', 'ADMIN')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping("/restore/{userExtensionId}")
    public void restoreUserExtension(@PathVariable long userExtensionId) {
        log.info("Restore user extension");
        userExtensionService.restoreUserExtension(userExtensionId);
    }

}
