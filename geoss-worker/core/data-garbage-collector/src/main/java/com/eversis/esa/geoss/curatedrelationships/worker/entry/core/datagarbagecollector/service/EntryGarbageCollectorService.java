package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.datagarbagecollector.service;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.DefinitionType;

import java.time.LocalDateTime;

/**
 * The interface Entry garbage collector service.
 */
public interface EntryGarbageCollectorService {

    /**
     * Runs entry garbage collector. It creates and updates not found counters in garbage log for those entries, which
     * were not updated during last update. Removes entries (and its related resources), whose notFoundCounters exceed
     * provided limit and are matching provided criteria and its related resources.
     *
     * @param codePrefix code prefix allows to narrow entries result to specific group
     * @param definitionType definition type narrows down entries to specific type, like user defined or predefined
     * @param lastEntryUpdate start date of last update of entries matching provided criteria like {@code codePrefix}
     and {@code definitionType}
     * @param notFoundLimit counters which are greater or equal to value of this parameter are counted as exceeded
     */
    void run(String codePrefix, DefinitionType definitionType, LocalDateTime lastEntryUpdate, int notFoundLimit);

    /**
     * Creates and updates not found counters in garbage log for those entries, which were not updated during last
     * update.
     *
     * @param codePrefix code prefix allows to narrow entries result to specific group
     * @param definitionType definition type narrows down entries to specific type, like user defined or predefined
     * @param lastEntryUpdate start date of last update of entries matching provided criteria like {@code codePrefix}
     and {@code definitionType}
     */
    void updateEntitiesNotFoundCounters(String codePrefix, DefinitionType definitionType,
            LocalDateTime lastEntryUpdate);

    /**
     * Updates entries basing on their notFoundCounters. Entries (and their relations), which exceed limit and are
     * matching provided criteria and marked as deleted. Entries, which match provided criteria but not not exceed limit
     * are marked as not deleted.
     *
     * @param codePrefix code prefix allows to narrow entries result to specific group
     * @param definitionType definition type narrows down entries to specific type, like user defined or predefined
     * @param notFoundLimit counters which are greater or equal to value of this parameter are counted as exceeded
     */
    void updateEntityStatuses(String codePrefix, DefinitionType definitionType, int notFoundLimit);

}
