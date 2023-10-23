package com.eversis.esa.geoss.curated.relations.service.impl;

import com.eversis.esa.geoss.curated.common.domain.Status;
import com.eversis.esa.geoss.curated.relations.domain.UserRelation;
import com.eversis.esa.geoss.curated.relations.mapper.UserRelationMapper;
import com.eversis.esa.geoss.curated.relations.model.UserRelationModel;
import com.eversis.esa.geoss.curated.relations.repository.UserRelationRepository;
import com.eversis.esa.geoss.curated.relations.service.UserRelationService;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;

/**
 * The type User relation service.
 */
@Log4j2
@Service
@Transactional(readOnly = true)
@Validated
public class UserRelationServiceImpl implements UserRelationService {

    private final UserRelationRepository userRelationRepository;

    private final UserRelationMapper userRelationMapper;

    /**
     * Instantiates a new User relation service.
     *
     * @param userRelationRepository the user relation repository
     * @param userRelationMapper the user relation mapper
     */
    public UserRelationServiceImpl(UserRelationRepository userRelationRepository,
            UserRelationMapper userRelationMapper) {
        this.userRelationRepository = userRelationRepository;
        this.userRelationMapper = userRelationMapper;
    }

    @Override
    public Page<UserRelation> findAllUserRelations(@NotNull Pageable pageable) {
        log.info("Finding all user relations");
        return userRelationRepository.findAll(pageable);
    }

    @Override
    public Page<UserRelation> findAllUserRelations(String userId, Pageable pageable) {
        log.info("Finding all relations for userId {}", userId);
        return userRelationRepository.findByUserId(userId, pageable);
    }

    @Override
    public UserRelation findUserRelation(long userRelationId) {
        log.info("Finding user relation with id {}", userRelationId);
        return userRelationRepository.findById(userRelationId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "User Relation entity with id: " + userRelationId + " does not exist"));
    }

    @Transactional
    @Override
    public void createUserRelation(UserRelationModel userRelationDto) {
        log.info("Creating new user relation - {}", userRelationDto);
        UserRelation userRelation  = userRelationRepository
                .save(userRelationMapper.mapToUserRelation(userRelationDto));
        log.info("Created new user relation with id: {}", userRelation.getId());
    }

    @Transactional
    @Override
    public void updateUserRelation(long userRelationId, UserRelationModel userRelationDto) {
        log.info("Updating user relation with id {}, using model {}", userRelationId, userRelationDto);
        final UserRelation userRelation = userRelationRepository.findById(userRelationId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "User Relation entity with id: " + userRelationId + " does not exist"));
        userRelationRepository.save(userRelationMapper.mapToUserRelation(userRelationDto, userRelation));
        log.info("Updated user relation with id: {}", userRelation.getId());
    }

    @Transactional
    @Override
    public void removeUserRelation(long userRelationId) {
        log.info("Removing user relation with id {}", userRelationId);
        final UserRelation userRelation = userRelationRepository.findById(userRelationId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "User Relation entity with id: " + userRelationId + " does not exist"));
        userRelation.setStatus(Status.IN_TRASH);
        userRelationRepository.save(userRelation);
        log.info("Removed user relation with id: {}", userRelation.getId());
    }

    @Transactional
    @Override
    public void deleteUserRelation(long userRelationId) {
        log.info("Deleting user relation with id: {}", userRelationId);
        userRelationRepository.deleteById(userRelationId);
        log.info("Deleted user relation with id: {}", userRelationId);
    }

    @Transactional
    @Override
    public void restoreUserRelation(long userRelationId) {
        log.info("Restoring user relation with id: {}", userRelationId);
        final UserRelation userRelation = userRelationRepository.findById(userRelationId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "User Relation entity with id: " + userRelationId + " does not exist"));
        userRelation.setStatus(Status.PENDING);
        userRelationRepository.save(userRelation);
        log.info("Restored user relation with id: {}", userRelationId);
    }

    @Transactional
    @Override
    public UserRelation approveUserResource(long userRelationId) {
        log.info("Approving user relation with id {}", userRelationId);
        final UserRelation userRelation = userRelationRepository.findById(userRelationId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "User Relation entity with id: " + userRelationId + " does not exist"));
        userRelation.setStatus(Status.APPROVED);
        userRelationRepository.save(userRelation);
        log.info("Approved user relation.");
        return userRelation;
    }

    @Transactional
    @Override
    public void denyUserRelation(long userRelationId) {
        log.info("Denying user relation with id {}", userRelationId);
        final UserRelation userRelation = userRelationRepository.findById(userRelationId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "User Relation entity with id: " + userRelationId + " does not exist"));
        userRelation.setStatus(Status.DENIED);
        userRelationRepository.save(userRelation);
        log.info("Denied user relation.");
    }

    @Transactional
    @Override
    public void pendingUserRelation(long userRelationId) {
        log.info("Pending user relation with id {}", userRelationId);
        final UserRelation userRelation = userRelationRepository.findById(userRelationId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "User Relation entity with id: " + userRelationId + " does not exist"));
        userRelation.setStatus(Status.PENDING);
        userRelationRepository.save(userRelation);
        log.info("Pending user relation finished.");
    }
}
