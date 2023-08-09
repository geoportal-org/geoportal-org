package com.eversis.esa.geoss.curated.resources.service.impl;

import com.eversis.esa.geoss.curated.resources.domain.Status;
import com.eversis.esa.geoss.curated.resources.domain.UserResource;
import com.eversis.esa.geoss.curated.resources.mapper.UserResourcesMapper;
import com.eversis.esa.geoss.curated.resources.model.UserResourceModel;
import com.eversis.esa.geoss.curated.resources.repository.UserResourceRepository;
import com.eversis.esa.geoss.curated.resources.service.UserResourceService;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;

/**
 * The type User resource service.
 */
@Log4j2
@Service
@Transactional(readOnly = true)
@Validated
public class UserResourceServiceImpl implements UserResourceService {

    private final UserResourceRepository userResourceRepository;

    private final UserResourcesMapper userResourcesMapper;

    /**
     * Instantiates a new User resource service.
     *
     * @param userResourceRepository the user resource repository
     * @param userResourcesMapper the user resources mapper
     */
    public UserResourceServiceImpl(UserResourceRepository userResourceRepository,
            UserResourcesMapper userResourcesMapper) {
        this.userResourceRepository = userResourceRepository;
        this.userResourcesMapper = userResourcesMapper;
    }

    @Override
    public Page<UserResource> findAllUserResources(@NotNull Pageable pageable) {
        log.info("Finding all user resources");
        return userResourceRepository.findAll(pageable);
    }

    @Override
    public UserResource findUserResource(long userResourceId) {
        log.info("Finding user resource with id {}", userResourceId);
        return userResourceRepository.findById(userResourceId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "User Resource entity with id: " + userResourceId + " does not exist"));
    }

    @Override
    public Page<UserResource> findAllUserResources(long userId, @NotNull Pageable pageable) {
        log.info("Finding all resource for userId {}", userId);
        return userResourceRepository.findByUserId(userId, pageable);
    }

    @Transactional
    @Override
    public void createUserResource(UserResourceModel userResourceDto) {
        log.info("Creating new user resource - {}", userResourceDto);
        UserResource userResource = userResourceRepository.save(userResourcesMapper.mapToUserResource(userResourceDto));
        log.info("Created new user resource with id: {}", userResource.getId());
    }

    @Transactional
    @Override
    public void updateUserResource(long userResourceId, UserResourceModel userResourceDto) {
        log.info("Updating user resource with id {}, using model {}", userResourceId, userResourceDto);
        final UserResource userResource = userResourceRepository.findById(userResourceId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "User Resource entity with id: " + userResourceId + " does not exist"));
        userResourceRepository.save(userResourcesMapper.mapToUserResource(userResourceDto, userResource));
        log.info("Updated user resource with id: {}", userResource.getId());
    }

    @Transactional
    @Override
    public void removeUserResource(long userResourceId) {
        log.info("Removing user resource with id {}", userResourceId);
        final UserResource userResource = userResourceRepository.findById(userResourceId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "User Resource entity with id: " + userResourceId + " does not exist"));
        userResource.setStatus(Status.IN_TRASH);
        userResourceRepository.save(userResource);
        log.info("Removed user resource with id: {}", userResource.getId());
    }

    @Transactional
    @Override
    public void deleteUserResource(long userResourceId) {
        log.info("Deleting user resource with id: {}", userResourceId);
        userResourceRepository.deleteById(userResourceId);
        log.info("Deleted user resource with id: {}", userResourceId);
    }

    @Transactional
    @Override
    public void restoreUserResource(long userResourceId) {
        log.info("Restoring user resource with id: {}", userResourceId);
        final UserResource userResource = userResourceRepository.findById(userResourceId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "User Resource entity with id: " + userResourceId + " does not exist"));
        userResource.setStatus(Status.PENDING);
        userResourceRepository.save(userResource);
        log.info("Restored user resource with id: {}", userResourceId);
    }

    @Transactional
    @Override
    public UserResource approveUserResource(long userResourceId) {
        log.info("Approving user resource with id {}", userResourceId);
        final UserResource userResource = userResourceRepository.findById(userResourceId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "User Resource entity with id: " + userResourceId + " does not exist"));
        userResource.setStatus(Status.APPROVED);
        userResourceRepository.save(userResource);
        log.info("Approved user resource.");
        return userResource;
    }

    @Transactional
    @Override
    public void denyUserResource(long userResourceId) {
        log.info("Denying user resource with id {}", userResourceId);
        final UserResource userResource = userResourceRepository.findById(userResourceId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "User Resource entity with id: " + userResourceId + " does not exist"));
        userResource.setStatus(Status.DENIED);
        userResourceRepository.save(userResource);
        log.info("Denied user resource.");
    }

    @Transactional
    @Override
    public void pendingUserResource(long userResourceId) {
        log.info("Pending user resource with id {}", userResourceId);
        final UserResource userResource = userResourceRepository.findById(userResourceId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "User Resource entity with id: " + userResourceId + " does not exist"));
        userResource.setStatus(Status.PENDING);
        userResourceRepository.save(userResource);
        log.info("Pending user resource finished.");
    }

}
