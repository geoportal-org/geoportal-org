package com.eversis.esa.geoss.curated.relations.service.impl;

import jakarta.validation.constraints.NotNull;

import com.eversis.esa.geoss.curated.relations.domain.EntryRelation;
import com.eversis.esa.geoss.curated.relations.mapper.EntryRelationMapper;
import com.eversis.esa.geoss.curated.relations.model.EntryRelationIdModel;
import com.eversis.esa.geoss.curated.relations.model.EntryRelationModel;
import com.eversis.esa.geoss.curated.relations.repository.EntryRelationRepository;
import com.eversis.esa.geoss.curated.relations.service.EntryRelationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 * The type Entry relation service.
 */
@Log4j2
@Service
@Transactional(readOnly = true)
@Validated
public class EntryRelationServiceImpl implements EntryRelationService {

    private final EntryRelationRepository entryRelationRepository;

    private final EntryRelationMapper entryRelationMapper;

    /**
     * Instantiates a new Entry relation service.
     *
     * @param entryRelationRepository the entry relation repository
     * @param entryRelationMapper the entry relation mapper
     */
    public EntryRelationServiceImpl(EntryRelationRepository entryRelationRepository,
            EntryRelationMapper entryRelationMapper) {
        this.entryRelationRepository = entryRelationRepository;
        this.entryRelationMapper = entryRelationMapper;
    }

    @Override
    public Page<EntryRelation> findEntryRelations(@NotNull Pageable pageable) {
        log.info("Finding entry relations");
        return entryRelationRepository.findAll(pageable);
    }

    @Override
    public EntryRelation findEntryRelation(EntryRelationIdModel entryRelationId) {
        log.info("Finding entry relation with id {}", entryRelationId);
        return entryRelationRepository.findById(entryRelationMapper.mapToEntryRelation(entryRelationId)).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Entry relation entity with id: " + entryRelationId.toString() + " does not exist"));
    }

    @Transactional
    @Override
    public void createEntryRelation(EntryRelationModel entryRelationDto) {
        log.info("Creating new entry relation - {}", entryRelationDto);
        EntryRelation entryRelation =
                entryRelationRepository.save(entryRelationMapper.mapToEntryRelation(entryRelationDto));
        log.info("Created new entry relation with id: {}", entryRelation.getId().toString());
    }

    @Transactional
    @Override
    public void updateEntryRelation(EntryRelationIdModel entryRelationId, EntryRelationModel entryRelationDto) {
        log.info("Updating entry relation with id {}, using model {}", entryRelationId, entryRelationDto);
        final EntryRelation entryRelation = entryRelationRepository
                .findById(entryRelationMapper.mapToEntryRelation(entryRelationId)).orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Entry relation entity with id: " + entryRelationId.toString() + " does not exist"));
        entryRelationRepository.save(entryRelationMapper.mapToEntryRelation(entryRelationDto, entryRelation));
        log.info("Updated entry relation with id: {}", entryRelation.getId().toString());
    }

    @Transactional
    @Override
    public void removeEntryRelation(EntryRelationIdModel entryRelationId) {
        log.info("Removing entry relation with id {}", entryRelationId);
        final EntryRelation entryRelation = entryRelationRepository
                .findById(entryRelationMapper.mapToEntryRelation(entryRelationId)).orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Entry relation entity with id: " + entryRelationId + " does not exist"));
        entryRelation.setDeleted(1);
        entryRelationRepository.save(entryRelation);
        log.info("Removed entry relation with id: {}", entryRelation.getId().toString());
    }

    @Transactional
    @Override
    public void deleteEntryRelation(EntryRelationIdModel entryRelationId) {
        log.info("Deleting entry relation with id: {}", entryRelationId);
        entryRelationRepository.deleteById(entryRelationMapper.mapToEntryRelation(entryRelationId));
        log.info("Deleted entry with id: {}", entryRelationId);
    }

    @Transactional
    @Override
    public void restoreEntryRelation(EntryRelationIdModel entryRelationId) {
        log.info("Restoring entry relation with id {}", entryRelationId);
        final EntryRelation entryRelation = entryRelationRepository
                .findById(entryRelationMapper.mapToEntryRelation(entryRelationId)).orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Entry relation entity with id: " + entryRelationId + " does not exist"));
        entryRelation.setDeleted(0);
        entryRelationRepository.save(entryRelation);
        log.info("Restored entry with id: {}", entryRelation.getId().toString());
    }

    @Transactional
    @Override
    public EntryRelation getOrCreateEntry(EntryRelationModel model) {
        return entryRelationRepository.findById(entryRelationMapper.mapToEntryRelationId(model))
                .orElseGet(() -> entryRelationRepository.save(entryRelationMapper.mapToEntryRelation(model)));
    }

}
