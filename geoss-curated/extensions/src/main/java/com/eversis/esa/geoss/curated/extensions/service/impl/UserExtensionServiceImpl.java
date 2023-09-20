package com.eversis.esa.geoss.curated.extensions.service.impl;

import jakarta.validation.constraints.NotNull;

import com.eversis.esa.geoss.curated.common.domain.Status;
import com.eversis.esa.geoss.curated.extensions.domain.UserExtension;
import com.eversis.esa.geoss.curated.extensions.mapper.UserExtensionMapper;
import com.eversis.esa.geoss.curated.extensions.model.UserExtensionModel;
import com.eversis.esa.geoss.curated.extensions.repository.UserExtensionRepository;
import com.eversis.esa.geoss.curated.extensions.service.TransferOptionExtensionService;
import com.eversis.esa.geoss.curated.extensions.service.UserExtensionService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 * The type User extension service.
 */
@Log4j2
@Service
@Transactional(readOnly = true)
@Validated
public class UserExtensionServiceImpl implements UserExtensionService {

    private final UserExtensionRepository userExtensionRepository;

    private final UserExtensionMapper userExtensionMapper;

    private final TransferOptionExtensionService transferOptionExtensionService;

    /**
     * Instantiates a new User extension service.
     *
     * @param userExtensionRepository the user extension repository
     * @param userExtensionMapper the user extension mapper
     * @param transferOptionExtensionService the transfer option extension service
     */
    public UserExtensionServiceImpl(UserExtensionRepository userExtensionRepository,
            UserExtensionMapper userExtensionMapper, TransferOptionExtensionService transferOptionExtensionService) {
        this.userExtensionRepository = userExtensionRepository;
        this.userExtensionMapper = userExtensionMapper;
        this.transferOptionExtensionService = transferOptionExtensionService;
    }

    @Override
    public Page<UserExtension> findAllUserExtensions(@NotNull Pageable pageable) {
        log.info("Finding all user extensions");
        return userExtensionRepository.findAll(pageable);
    }

    @Override
    public UserExtension findUserExtension(long userExtensionId) {
        log.info("Finding user extension with id {}", userExtensionId);
        return userExtensionRepository.findById(userExtensionId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "User Extension entity with id: " + userExtensionId + " does not exist"));
    }

    @Override
    public Page<UserExtension> findUserExtensions(String userId, @NotNull Pageable pageable) {
        log.info("Finding all extensions for userId {}", userId);
        return userExtensionRepository.findByUserId(userId, pageable);
    }

    @Transactional
    @Override
    public void createUserExtension(UserExtensionModel userExtensionDto) {
        log.info("Creating new user extension - {}", userExtensionDto);
        UserExtension userExtension =
                userExtensionRepository.save(userExtensionMapper.mapToUserUserExtension(userExtensionDto));
        transferOptionExtensionService.saveTransferOptionExtensions(userExtensionDto.getEntryExtension()
                .getTransferOptionExtensions(), userExtension.getEntryExtension());
        log.info("Created new user extension with id: {}", userExtension.getId());
    }

    @Transactional
    @Override
    public void updateUserExtension(long userExtensionId, UserExtensionModel userExtensionDto) {
        log.info("Updating user extension with id {}, using model {}", userExtensionId, userExtensionDto);
        final UserExtension userExtension = userExtensionRepository.findById(userExtensionId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "User Extension entity with id: " + userExtensionId + " does not exist"));
        userExtensionRepository.save(userExtensionMapper.mapToUserUserExtension(userExtensionDto, userExtension));
        transferOptionExtensionService.saveTransferOptionExtensions(userExtensionDto.getEntryExtension()
                .getTransferOptionExtensions(), userExtension.getEntryExtension());
        log.info("Updated user extension with id: {}", userExtension.getId());
    }

    @Transactional
    @Override
    public void removeUserExtension(long userExtensionId) {
        log.info("Removing user extension with id {}", userExtensionId);
        final UserExtension userExtension = userExtensionRepository.findById(userExtensionId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "User Extension entity with id: " + userExtensionId + " does not exist"));
        userExtension.setStatus(Status.IN_TRASH);
        userExtensionRepository.save(userExtension);
        log.info("Removed user extension with id: {}", userExtension.getId());
    }

    @Transactional
    @Override
    public void deleteUserExtension(long userExtensionId) {
        log.info("Deleting user extension with id: {}", userExtensionId);
        userExtensionRepository.deleteById(userExtensionId);
        log.info("Deleted user extension with id: {}", userExtensionId);
    }

    @Transactional
    @Override
    public void restoreUserExtension(long userExtensionId) {
        log.info("Restoring user extension with id: {}", userExtensionId);
        final UserExtension userExtension = userExtensionRepository.findById(userExtensionId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "User Extension entity with id: " + userExtensionId + " does not exist"));
        userExtension.setStatus(Status.PENDING);
        userExtensionRepository.save(userExtension);
        log.info("Restored user extension with id: {}", userExtensionId);
    }

}
