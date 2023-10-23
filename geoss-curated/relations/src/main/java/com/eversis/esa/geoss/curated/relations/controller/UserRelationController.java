package com.eversis.esa.geoss.curated.relations.controller;

import com.eversis.esa.geoss.curated.relations.domain.UserRelation;
import com.eversis.esa.geoss.curated.relations.model.UserRelationModel;
import com.eversis.esa.geoss.curated.relations.service.UserRelationService;

import io.swagger.v3.oas.annotations.tags.Tag;
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
 * The type User relation controller.
 */
@Log4j2
@BasePathAwareController("/userRelations")
@ResponseBody
@Tag(name = "userRelations")
public class UserRelationController {

    private final UserRelationService userRelationService;

    /**
     * Instantiates a new User relation controller.
     *
     * @param userRelationService the user relation service
     */
    public UserRelationController(UserRelationService userRelationService) {
        this.userRelationService = userRelationService;
    }

    /**
     * Find all user relations page.
     *
     * @param page the page
     * @param size the size
     * @return the page
     */
    @PreAuthorize("hasAnyRole('RELATION_READER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Page<UserRelation> findAllUserRelations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Find all user relations page: {}, size: {}", page, size);
        return userRelationService.findAllUserRelations(PageRequest.of(page, size));
    }

    /**
     * Find user relation.
     *
     * @param userRelationId the user relation id
     * @return the user relation
     */
    @PreAuthorize("hasAnyRole('RELATION_READER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{userRelationId}")
    public UserRelation findUserRelation(@PathVariable long userRelationId) {
        log.info("Find user relation");
        return userRelationService.findUserRelation(userRelationId);
    }

    /**
     * Find user relations page.
     *
     * @param userId the user id
     * @param page the page
     * @param size the size
     * @return the page
     */
    @PreAuthorize("hasAnyRole('RELATION_READER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/user/{userId}")
    public Page<UserRelation> findUserRelations(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Find user relations");
        return userRelationService.findAllUserRelations(userId, PageRequest.of(page, size));
    }

    /**
     * Create user relation.
     *
     * @param userRelationDto the user relation dto
     */
    @PreAuthorize("hasAnyRole('RELATION_WRITER', 'ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createUserRelation(@RequestBody @Valid UserRelationModel userRelationDto) {
        log.info("Create user relation");
        userRelationService.createUserRelation(userRelationDto);
    }

    /**
     * Update user relation.
     *
     * @param userRelationId the user relation id
     * @param userRelationDto the user relation dto
     */
    @PreAuthorize("hasAnyRole('RELATION_WRITER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{userRelationId}")
    public void updateUserRelation(
            @PathVariable long userRelationId,
            @RequestBody @Valid UserRelationModel userRelationDto) {
        log.info("Update user relation");
        userRelationService.updateUserRelation(userRelationId, userRelationDto);
    }

    /**
     * Remove user relation.
     *
     * @param userRelationId the user relation id
     */
    @PreAuthorize("hasAnyRole('RELATION_REMOVER', 'ADMIN')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping("/{userRelationId}")
    public void removeUserRelation(@PathVariable long userRelationId) {
        log.info("Remove user relation");
        userRelationService.removeUserRelation(userRelationId);
    }

    /**
     * Delete user relation.
     *
     * @param userRelationId the user relation id
     */
    @PreAuthorize("hasAnyRole('RELATION_DELETER', 'ADMIN')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping("/delete/{userRelationId}")
    public void deleteUserRelation(@PathVariable long userRelationId) {
        log.info("Delete user relation");
        userRelationService.deleteUserRelation(userRelationId);
    }

    /**
     * Restore user relation.
     *
     * @param userRelationId the user relation id
     */
    @PreAuthorize("hasAnyRole('RELATION_REMOVER', 'ADMIN')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping("/restore/{userRelationId}")
    public void restoreUserRelation(@PathVariable long userRelationId) {
        log.info("Restore user relation");
        userRelationService.restoreUserRelation(userRelationId);
    }

}
