package com.eversis.esa.geoss.curated.extensions.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import jakarta.validation.constraints.NotNull;

import com.eversis.esa.geoss.curated.extensions.domain.EntryExtension;
import com.eversis.esa.geoss.curated.extensions.domain.TransferOptionExtension;
import com.eversis.esa.geoss.curated.extensions.mapper.TransferOptionExtensionMapper;
import com.eversis.esa.geoss.curated.extensions.model.TransferOptionExtensionModel;
import com.eversis.esa.geoss.curated.extensions.repository.TransferOptionExtensionRepository;
import com.eversis.esa.geoss.curated.extensions.service.TransferOptionExtensionService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 * The type Transfer option extension service.
 */
@Log4j2
@Service
@Transactional(readOnly = true)
@Validated
public class TransferOptionExtensionServiceImpl implements TransferOptionExtensionService {

    private final TransferOptionExtensionRepository transferOptionExtensionRepository;

    private final TransferOptionExtensionMapper transferOptionExtensionMapper;

    /**
     * Instantiates a new Transfer option extension service.
     *
     * @param transferOptionExtensionRepository the transfer option extension repository
     * @param transferOptionExtensionMapper the transfer option extension mapper
     */
    public TransferOptionExtensionServiceImpl(TransferOptionExtensionRepository transferOptionExtensionRepository,
            TransferOptionExtensionMapper transferOptionExtensionMapper) {
        this.transferOptionExtensionRepository = transferOptionExtensionRepository;
        this.transferOptionExtensionMapper = transferOptionExtensionMapper;
    }

    @Override
    public Page<TransferOptionExtension> findTransferOptionExtensions(@NotNull Pageable pageable) {
        log.info("Finding transfer option extemsions");
        return transferOptionExtensionRepository.findAll(pageable);
    }

    @Override
    public TransferOptionExtension findTransferOptionExtension(long transferOptionExtensionId) {
        log.info("Finding transfer option extension with id {}", transferOptionExtensionId);
        return transferOptionExtensionRepository.findById(transferOptionExtensionId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "TransferOptionExtension entity with id: " + transferOptionExtensionId + " does not exist"));
    }

    @Override
    public Set<TransferOptionExtension> findTransferOptionExtensionsByExtensionId(long extensionId) {
        log.info("Finding transfer option extensions by extension id");
        return transferOptionExtensionRepository.findByEntryExtensionId(extensionId);
    }

    @Transactional
    @Override
    public List<TransferOptionExtension> saveTransferOptionExtensions(
            List<TransferOptionExtensionModel> transferOptionExtensionDto, EntryExtension entryExtension) {
        log.info("Saving transfer option extensions - {}", transferOptionExtensionDto);
        Set<TransferOptionExtension> existingTransferOptionExtensions =
                transferOptionExtensionRepository.findByEntryExtensionId(entryExtension.getId());
        Set<TransferOptionExtension> newTransferOptionExtensions = transferOptionExtensionDto.stream()
                .map(transferOptionExtension ->
                        transferOptionExtensionMapper.mapToTransferOptionExtension(transferOptionExtension,
                                entryExtension))
                .collect(Collectors.toSet());
        existingTransferOptionExtensions.addAll(newTransferOptionExtensions);
        existingTransferOptionExtensions.forEach(transferOptionExtension
                -> transferOptionExtension.setModifiedDate(LocalDateTime.now()));
        log.info("Transfer option extensions saved");
        return transferOptionExtensionRepository.saveAll(existingTransferOptionExtensions);
    }

    @Transactional
    @Override
    public void removeTransferOptionExtension(long transferOptionExtensionId) {
        log.info("Removing transfer option extension with id {}", transferOptionExtensionId);
        final TransferOptionExtension transferOptionExtension =
                transferOptionExtensionRepository.findById(transferOptionExtensionId).orElseThrow(
                        () -> new ResourceNotFoundException(
                                "TransferOptionExtension entity with id: " + transferOptionExtensionId
                                        + " does not exist"));
        transferOptionExtension.setDeleted(1);
        transferOptionExtensionRepository.save(transferOptionExtension);
        log.info("Removed transfer option extension with id: {}", transferOptionExtension.getId());
    }

    @Transactional
    @Override
    public void deleteTransferOptionExtension(long transferOptionExtensionId) {
        log.info("Deleting transfer option extension with id: {}", transferOptionExtensionId);
        transferOptionExtensionRepository.deleteById(transferOptionExtensionId);
        log.info("Deleted transfer option extension with id: {}", transferOptionExtensionId);
    }

}
