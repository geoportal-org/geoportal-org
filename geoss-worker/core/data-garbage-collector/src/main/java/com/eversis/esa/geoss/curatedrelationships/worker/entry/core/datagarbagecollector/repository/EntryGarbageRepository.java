package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.datagarbagecollector.repository;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.DefinitionType;

import java.time.LocalDateTime;

/**
 * The interface Entry garbage repository.
 */
public interface EntryGarbageRepository {

    /**
     * Returns number of affected rows, which were not updated during last update.
     *
     * @param codePrefix code prefix allows to narrow entries result to specific group
     * @param definitionType definition type narrows down entries to specific type, like user defined or predefined
     * @param lastEntryUpdate start date of last update of entries matching provided criteria like {@code codePrefix}
     and {@code definitionType}
     * @return number of entries, which were not updated during last update
     */
    int getNotUpdatedEntriesCount(String codePrefix, DefinitionType definitionType, LocalDateTime lastEntryUpdate);

    /**
     * Creates and updates not found counters in garbage log table for those entries, which were not updated during last
     * update.
     *
     * @param codePrefix code prefix allows to narrow entries result to specific group
     * @param definitionType definition type narrows down entries to specific type, like user defined or predefined
     * @param lastEntryUpdate start date of last update of entries matching provided criteria like {@code codePrefix}
     and {@code definitionType}
     */
    void updateNotFoundCounters(String codePrefix, DefinitionType definitionType, LocalDateTime lastEntryUpdate);

    /**
     * Removes counters from garbage log, which are no longer valid - their matching entryId was updated during last
     * update.
     *
     * @param codePrefix code prefix allows to narrow entries result to specific group
     * @param definitionType definition type narrows down entries to specific type, like user defined or predefined
     * @param lastEntryUpdate start date of last update of entries matching provided criteria like {@code codePrefix}
     and {@code definitionType}
     * @param lastGarbageLogUpdate start date of last garbage log update
     * @return number of entries, which were removed from garbage log
     */
    int removeNoLongerValidGarbageLogs(String codePrefix, DefinitionType definitionType, LocalDateTime lastEntryUpdate,
            LocalDateTime lastGarbageLogUpdate);

    /**
     * Removes entries (and its related resources), whose notFoundCounters exceed provided limit and are matching
     * provided criteria and its related resources.
     *
     * @param codePrefix code prefix allows to narrow entries result to specific group
     * @param definitionType definition type narrows down entries to specific type, like user defined or predefined
     * @param notFoundLimit counters which are greater or equal to value of this parameter are counted as exceeded
     */
    void removeEntitiesExceedingNotFoundLimit(String codePrefix, DefinitionType definitionType, int notFoundLimit);

}
