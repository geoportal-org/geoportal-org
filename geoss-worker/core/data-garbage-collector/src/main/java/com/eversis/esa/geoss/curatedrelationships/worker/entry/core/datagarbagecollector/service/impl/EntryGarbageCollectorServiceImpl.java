package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.datagarbagecollector.service.impl;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.datagarbagecollector.repository.EntryGarbageRepository;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.datagarbagecollector.service.EntryGarbageCollectorService;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.DefinitionType;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * The type Entry garbage collector service.
 */
@Log4j2
@Service("dataGarbageCollectorService")
public class EntryGarbageCollectorServiceImpl implements EntryGarbageCollectorService {

    private final EntryGarbageRepository entryGarbageRepository;

    /**
     * Instantiates a new Entry garbage collector service.
     *
     * @param entryGarbageRepository the entry garbage repository
     */
    public EntryGarbageCollectorServiceImpl(EntryGarbageRepository entryGarbageRepository) {
        this.entryGarbageRepository = entryGarbageRepository;
    }

    /**
     * Run.
     *
     * @param codePrefix the code prefix
     * @param definitionType the definition type
     * @param lastEntryUpdate the last entry update
     * @param notFoundLimit the not found limit
     */
    @Override
    public void run(String codePrefix, DefinitionType definitionType, LocalDateTime lastEntryUpdate,
            int notFoundLimit) {
        log.info("Running entry garbage collector with params - codePrefix : {}, definitionType : {}, lastEntryUpdate"
                 + " : {}, notFoundLimit : {}", codePrefix, definitionType, lastEntryUpdate, notFoundLimit);
        updateEntitiesNotFoundCounters(codePrefix, definitionType, lastEntryUpdate);
        updateEntityStatuses(codePrefix, definitionType, notFoundLimit);
    }

    /**
     * Update entities not found counters.
     *
     * @param codePrefix the code prefix
     * @param definitionType the definition type
     * @param lastEntryUpdate the last entry update
     */
    @Override
    public void updateEntitiesNotFoundCounters(
            String codePrefix,
            DefinitionType definitionType,
            LocalDateTime lastEntryUpdate) {
        log.info("Updating entry garbage counters by querying entities with params - codePrefix : {}"
                + ", definitionType : {}, lastEntryUpdate : {}", codePrefix, definitionType, lastEntryUpdate);
        int notModifiedEntriesCount = entryGarbageRepository.getNotUpdatedEntriesCount(codePrefix, definitionType,
                lastEntryUpdate);
        log.info("Found {} entries which were not updated during last update", notModifiedEntriesCount);
        entryGarbageRepository.updateNotFoundCounters(codePrefix, definitionType, lastEntryUpdate);
        entryGarbageRepository.removeNoLongerValidGarbageLogs(codePrefix, definitionType, lastEntryUpdate,
                LocalDateTime.now());
    }

    /**
     * Update entity statuses.
     *
     * @param codePrefix the code prefix
     * @param definitionType the definition type
     * @param notFoundLimit the not found limit
     */
    @Override
    public void updateEntityStatuses(
            String codePrefix,
            DefinitionType definitionType,
            int notFoundLimit) {
        log.info("Removing entries with notFoundCounter greater or equal to {} by querying entities with"
                + " - codePrefix : {}, definitionType : {}", notFoundLimit, codePrefix, definitionType);
        entryGarbageRepository.removeEntitiesExceedingNotFoundLimit(codePrefix, definitionType, notFoundLimit);
    }
}
