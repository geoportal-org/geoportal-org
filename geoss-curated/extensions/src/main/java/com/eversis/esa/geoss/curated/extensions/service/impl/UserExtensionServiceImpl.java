package com.eversis.esa.geoss.curated.extensions.service.impl;

import com.eversis.esa.geoss.curated.common.domain.Status;
import com.eversis.esa.geoss.curated.extensions.domain.UserExtension;
import com.eversis.esa.geoss.curated.extensions.dto.UserExtensionDTO;
import com.eversis.esa.geoss.curated.extensions.mapper.UserExtensionMapper;
import com.eversis.esa.geoss.curated.extensions.model.UserExtensionModel;
import com.eversis.esa.geoss.curated.extensions.repository.UserExtensionRepository;
import com.eversis.esa.geoss.curated.extensions.service.TransferOptionExtensionService;
import com.eversis.esa.geoss.curated.extensions.service.UserExtensionService;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class implements the UserExtensionService interface and provides the business logic for managing UserExtension
 * entities. It uses the UserExtensionRepository for database operations, UserExtensionMapper for mapping between the
 * model and entity, and TransferOptionExtensionService for managing associated TransferOptionExtension entities. All
 * methods are transactional, meaning they are part of a single database transaction. Some methods are read-only,
 * meaning they do not modify the database, while others can create, update, delete, or restore UserExtension
 * entities.
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
        final UserExtension userExtension = userExtensionRepository.findById(userExtensionId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "User Extension entity with id: " + userExtensionId + " does not exist"));
        transferOptionExtensionService.findTransferOptionExtensionsByExtensionId(
                        userExtension.getEntryExtension().getId())
                .forEach(transferOptionExtension -> transferOptionExtensionService.deleteTransferOptionExtension(
                        transferOptionExtension.getId()));
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

    @Transactional
    @Override
    public void pendingUserExtension(long userExtensionId) {
        log.info("Pending user extension with id {}", userExtensionId);
        final UserExtension userExtension = userExtensionRepository.findById(userExtensionId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "User Extension entity with id: " + userExtensionId + " does not exist"));
        userExtension.setStatus(Status.PENDING);
        userExtensionRepository.save(userExtension);
        log.info("Pending user extension finished.");
    }

    @Transactional
    @Override
    public UserExtension approveUserExtension(long userExtensionId) {
        log.info("Approving user extension with id {}", userExtensionId);
        final UserExtension userExtension = userExtensionRepository.findById(userExtensionId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "User Extension entity with id: " + userExtensionId + " does not exist"));
        userExtension.setStatus(Status.APPROVED);
        userExtensionRepository.save(userExtension);
        log.info("Approved user extension.");
        return userExtension;
    }

    @Transactional
    @Override
    public void denyUserExtension(long userExtensionId) {
        log.info("Denying user extension with id {}", userExtensionId);
        final UserExtension userExtension = userExtensionRepository.findById(userExtensionId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "User Extension entity with id: " + userExtensionId + " does not exist"));
        userExtension.setStatus(Status.DENIED);
        userExtensionRepository.save(userExtension);
        log.info("Denied user extension.");
    }

    @Override
    public Page<UserExtensionDTO> findUserExtensionsWithCheck(String userId, @NotNull Pageable pageable) {
        Page<UserExtension> userExtensionsPage = userExtensionRepository.findByUserId(userId, pageable);
        List<UserExtensionDTO> dtos = userExtensionsPage.stream()
                .map(userExtensionMapper::convertToDto)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, userExtensionsPage.getTotalElements());
    }

}
