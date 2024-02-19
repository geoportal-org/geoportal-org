package com.eversis.esa.geoss.curated.resources.service.impl;

import jakarta.validation.constraints.NotNull;

import com.eversis.esa.geoss.curated.resources.domain.Entry;
import com.eversis.esa.geoss.curated.resources.mapper.EntryMapper;
import com.eversis.esa.geoss.curated.resources.model.EntryModel;
import com.eversis.esa.geoss.curated.resources.repository.EntryRepository;
import com.eversis.esa.geoss.curated.resources.service.ResourceService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 * The type Resource service.
 */
@Log4j2
@Service
@Transactional(readOnly = true)
@Validated
public class ResourceServiceImpl implements ResourceService {

    private final EntryRepository entryRepository;

    private final EntryMapper entryMapper;

    /**
     * Instantiates a new Resource service.
     *
     * @param entryRepository the entry repository
     * @param entryMapper the entry mapper
     */
    public ResourceServiceImpl(EntryRepository entryRepository, EntryMapper entryMapper) {
        this.entryRepository = entryRepository;
        this.entryMapper = entryMapper;
    }

    @Override
    public Page<Entry> findEntries(@NotNull Pageable pageable) {
        log.info("Finding entries");
        return entryRepository.findAll(pageable);
    }

    @Override
    public Entry findEntry(long entryId) {
        log.info("Finding entry with id {}", entryId);
        return entryRepository.findById(entryId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Entry entity with id: " + entryId + " does not exist"));
    }

    @Transactional
    @Override
    public void createEntry(EntryModel entryDto) {
        log.info("Creating new entry - {}", entryDto);
        Entry entry = entryRepository.save(entryMapper.mapToEntry(entryDto));
        log.info("Created new entry with id: {}", entry.getId());
    }

    @Transactional
    @Override
    public void updateEntry(long entryId, EntryModel entryDto) {
        log.info("Updating entry with id {}, using model {}", entryId, entryDto);
        final Entry entry = entryRepository.findById(entryId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Entry entity with id: " + entryId + " does not exist"));
        entryRepository.save(entryMapper.mapToEntry(entryDto, entry));
        log.info("Updated entry with id: {}", entry.getId());
    }

    @Transactional
    @Override
    public void removeEntry(long entryId) {
        log.info("Removing entry with id {}", entryId);
        final Entry entry = entryRepository.findById(entryId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Entry entity with id: " + entryId + " does not exist"));
        entry.setDeleted(1);
        entryRepository.save(entry);
        log.info("Removed entry with id: {}", entry.getId());
    }

    @Transactional
    @Override
    public void deleteEntry(long entryId) {
        log.info("Deleting entry with id: {}", entryId);
        entryRepository.deleteById(entryId);
        log.info("Deleted entry with id: {}", entryId);
    }

    @Transactional
    @Override
    public void restoreEntry(long entryId) {
        log.info("Restoring entry with id {}", entryId);
        final Entry entry = entryRepository.findById(entryId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Entry entity with id: " + entryId + " does not exist"));
        entry.setDeleted(0);
        entryRepository.save(entry);
        log.info("Restored entry with id: {}", entry.getId());
    }

}
