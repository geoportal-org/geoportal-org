package com.eversis.esa.geoss.curated.extensions.service.impl;

import com.eversis.esa.geoss.curated.extensions.domain.EntryExtension;
import com.eversis.esa.geoss.curated.extensions.mapper.EntryExtensionMapper;
import com.eversis.esa.geoss.curated.extensions.model.EntryExtensionModel;
import com.eversis.esa.geoss.curated.extensions.repository.EntryExtensionRepository;
import com.eversis.esa.geoss.curated.extensions.service.EntryExtensionService;

import com.eversis.esa.geoss.curated.extensions.service.TransferOptionExtensionService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;

/**
 * This class implements the EntryExtensionService interface and provides the business logic for managing EntryExtension
 * entities. It uses the EntryExtensionRepository for database operations, EntryExtensionMapper for mapping between the
 * model and entity, and TransferOptionExtensionService for managing associated TransferOptionExtension entities. All
 * methods are transactional, meaning they are part of a single database transaction. Some methods are read-only,
 * meaning they do not modify the database, while others can create, update, delete, or restore EntryExtension
 * entities.
 */
@Log4j2
@Service
@Transactional(readOnly = true)
@Validated
public class EntryExtensionServiceImpl implements EntryExtensionService {

    private final EntryExtensionRepository entryExtensionRepository;

    private final EntryExtensionMapper entryExtensionMapper;

    private final TransferOptionExtensionService transferOptionExtensionService;

    /**
     * Instantiates a new Entry extension service.
     *
     * @param entryExtensionRepository the entry extension repository
     * @param entryExtensionMapper the entry extension mapper
     * @param transferOptionExtensionService the transfer option extension service
     */
    public EntryExtensionServiceImpl(EntryExtensionRepository entryExtensionRepository,
            EntryExtensionMapper entryExtensionMapper, TransferOptionExtensionService transferOptionExtensionService) {
        this.entryExtensionRepository = entryExtensionRepository;
        this.entryExtensionMapper = entryExtensionMapper;
        this.transferOptionExtensionService = transferOptionExtensionService;
    }

    @Override
    public Page<EntryExtension> findEntryExtensions(@NotNull Pageable pageable) {
        log.info("Finding entry extensions");
        return entryExtensionRepository.findAll(pageable);
    }

    @Override
    public EntryExtension findEntryExtension(long entryExtensionId) {
        log.info("Finding entry extension with id {}", entryExtensionId);
        return entryExtensionRepository.findById(entryExtensionId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Entry extension entity with id: " + entryExtensionId + " does not exist"));
    }

    @Transactional
    @Override
    public void createEntryExtension(EntryExtensionModel entryExtensionDto) {
        log.info("Creating new entry extension - {}", entryExtensionDto);
        EntryExtension entryExtension = entryExtensionRepository
                .save(entryExtensionMapper.mapToEntryExtension(entryExtensionDto));
        log.info("Created new entry extension with id: {}", entryExtension.getId());
    }

    @Transactional
    @Override
    public void updateEntryExtension(long entryExtensionId, EntryExtensionModel entryExtensionDto) {
        log.info("Updating entry extension with id {}, using model {}", entryExtensionId, entryExtensionDto);
        final EntryExtension entryExtension = entryExtensionRepository.findById(entryExtensionId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Entry extension entity with id: " + entryExtensionId + " does not exist"));
        entryExtensionRepository.save(entryExtensionMapper.mapToEntryExtension(entryExtensionDto, entryExtension));
        log.info("Updated entry extension with id: {}", entryExtension.getId());
    }

    @Transactional
    @Override
    public void removeEntryExtension(long entryExtensionId) {
        log.info("Removing entry extension with id {}", entryExtensionId);
        final EntryExtension entryExtension = entryExtensionRepository.findById(entryExtensionId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Entry extension entity with id: " + entryExtensionId + " does not exist"));
        entryExtension.setDeleted(1);
        entryExtensionRepository.save(entryExtension);
        log.info("Removed entry extension with id: {}", entryExtension.getId());
    }

    @Transactional
    @Override
    public void deleteEntryExtension(long entryExtensionId) {
        log.info("Deleting entry extension with id: {}", entryExtensionId);
        transferOptionExtensionService.findTransferOptionExtensionsByExtensionId(entryExtensionId)
                .forEach(transferOptionExtension -> transferOptionExtensionService.deleteTransferOptionExtension(
                        transferOptionExtension.getId()));
        entryExtensionRepository.deleteById(entryExtensionId);
        log.info("Deleted entry extension with id: {}", entryExtensionId);
    }

    @Transactional
    @Override
    public void restoreEntryExtension(long entryExtensionId) {
        log.info("Restoring entry extension with id {}", entryExtensionId);
        final EntryExtension entryExtension = entryExtensionRepository.findById(entryExtensionId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Entry extension entity with id: " + entryExtensionId + " does not exist"));
        entryExtension.setDeleted(0);
        entryExtensionRepository.save(entryExtension);
        log.info("Restored entry extension with id: {}", entryExtension.getId());
    }

    @Override
    public EntryExtension getOrCreateEntryExtension(EntryExtensionModel entryExtensionModel) {
        return entryExtensionRepository
                .findByCodeAndTitle(entryExtensionModel.getCode(), entryExtensionModel.getTitle())
                .orElseGet(() ->
                        entryExtensionRepository.save(entryExtensionMapper.mapToEntryExtension(entryExtensionModel)));
    }

}
