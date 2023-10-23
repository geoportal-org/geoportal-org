package com.eversis.esa.geoss.curated.dashboards.service.impl;

import com.eversis.esa.geoss.curated.common.domain.Status;
import com.eversis.esa.geoss.curated.dashboards.domain.UserDashboard;
import com.eversis.esa.geoss.curated.dashboards.mapper.UserDashboardMapper;
import com.eversis.esa.geoss.curated.dashboards.model.UserDashboardModel;
import com.eversis.esa.geoss.curated.dashboards.repository.UserDashboardRepository;
import com.eversis.esa.geoss.curated.dashboards.service.UserDashboardService;
import com.eversis.esa.geoss.curated.resources.service.TransferOptionService;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;

/**
 * The type User dashboard service.
 */
@Log4j2
@Service
@Transactional(readOnly = true)
@Validated
public class UserDashboardServiceImpl implements UserDashboardService {

    private final UserDashboardRepository userDashboardRepository;

    private final UserDashboardMapper userDashboardMapper;

    private final TransferOptionService transferOptionService;

    /**
     * Instantiates a new User dashboard service.
     *
     * @param userDashboardRepository the user dashboard repository
     * @param userDashboardMapper the user dashboard mapper
     * @param transferOptionService the transfer option service
     */
    public UserDashboardServiceImpl(UserDashboardRepository userDashboardRepository,
            UserDashboardMapper userDashboardMapper, TransferOptionService transferOptionService) {
        this.userDashboardRepository = userDashboardRepository;
        this.userDashboardMapper = userDashboardMapper;
        this.transferOptionService = transferOptionService;
    }

    @Override
    public Page<UserDashboard> findAllUserDashboards(@NotNull Pageable pageable) {
        log.info("Finding all user dashboards");
        return userDashboardRepository.findAll(pageable);
    }

    @Override
    public UserDashboard findUserDashboard(long userDashboardId) {
        log.info("Finding user dashboard with id {}", userDashboardId);
        return userDashboardRepository.findById(userDashboardId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "User Dashboard entity with id: " + userDashboardId + " does not exist"));
    }

    @Override
    public Page<UserDashboard> findUserDashboards(String userId, @NotNull Pageable pageable) {
        log.info("Finding all dashboard for userId {}", userId);
        return userDashboardRepository.findByUserId(userId, pageable);
    }

    @Transactional
    @Override
    public void createUserDashboard(UserDashboardModel userDashboardModel) {
        log.info("Creating new user dashboard - {}", userDashboardModel);
        UserDashboard userDashboard = userDashboardRepository
                .save(userDashboardMapper.mapToUserDashboard(userDashboardModel));
        transferOptionService
                .saveTransferOptions(userDashboardModel.getEntry().getTransferOptions(), userDashboard.getEntry());
        log.info("Created new user dashboard with id: {}", userDashboard.getId());
    }

    @Transactional
    @Override
    public void updateUserDashboard(long userDashboardId, UserDashboardModel userDashboardModel) {
        log.info("Updating user dashboard with id {}, using model {}", userDashboardId, userDashboardModel);
        final UserDashboard userDashboard = userDashboardRepository.findById(userDashboardId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "User Dashboard entity with id: " + userDashboardId + " does not exist"));
        userDashboardRepository.save(userDashboardMapper.mapToUserDashboard(userDashboardModel, userDashboard));
        transferOptionService
                .saveTransferOptions(userDashboardModel.getEntry().getTransferOptions(), userDashboard.getEntry());
        log.info("Updated user dahboard with id: {}", userDashboard.getId());
    }

    @Transactional
    @Override
    public void removeUserDashboard(long userDashboardId) {
        log.info("Removing user dashboard with id {}", userDashboardId);
        final UserDashboard userDashboard = userDashboardRepository.findById(userDashboardId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "User Dashboard entity with id: " + userDashboardId + " does not exist"));
        userDashboard.setStatus(Status.IN_TRASH);
        userDashboardRepository.save(userDashboard);
        log.info("Removed user dashboard with id: {}", userDashboard.getId());
    }

    @Transactional
    @Override
    public void deleteUserDashboard(long userDashboardId) {
        log.info("Deleting user dashboard with id: {}", userDashboardId);
        userDashboardRepository.deleteById(userDashboardId);
        log.info("Deleted user dashboard with id: {}", userDashboardId);
    }

    @Transactional
    @Override
    public void restoreUserDashboard(long userDashboardId) {
        log.info("Pending user dashboard with id {}", userDashboardId);
        final UserDashboard userDashboard = userDashboardRepository.findById(userDashboardId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "User Dashboard entity with id: " + userDashboardId + " does not exist"));
        userDashboard.setStatus(Status.PENDING);
        userDashboardRepository.save(userDashboard);
        log.info("Pending user dashboard finished.");
    }

    @Transactional
    @Override
    public UserDashboard approveUserDashboard(long userDashboardId) {
        log.info("Approving user dashboard with id {}", userDashboardId);
        final UserDashboard userDashboard = userDashboardRepository.findById(userDashboardId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "User Dashboard entity with id: " + userDashboardId + " does not exist"));
        userDashboard.setStatus(Status.APPROVED);
        userDashboardRepository.save(userDashboard);
        log.info("Approved user dashboard.");
        return userDashboard;
    }

    @Transactional
    @Override
    public void denyUserDashboard(long userDashboardId) {
        log.info("Denying user dashboard with id {}", userDashboardId);
        final UserDashboard userDashboard = userDashboardRepository.findById(userDashboardId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "User Dashboard entity with id: " + userDashboardId + " does not exist"));
        userDashboard.setStatus(Status.DENIED);
        userDashboardRepository.save(userDashboard);
        log.info("Denied user dashboard.");
    }

    @Transactional
    @Override
    public void pendingUserDashboard(long userDashboardId) {
        log.info("Pending user dashboard with id {}", userDashboardId);
        final UserDashboard userDashboard = userDashboardRepository.findById(userDashboardId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "User Dashboard entity with id: " + userDashboardId + " does not exist"));
        userDashboard.setStatus(Status.PENDING);
        userDashboardRepository.save(userDashboard);
        log.info("Pending user dashboard finished.");
    }

}
