package com.eversis.esa.geoss.curated.resources.service;

import com.eversis.esa.geoss.curated.resources.domain.Entry;
import com.eversis.esa.geoss.curated.resources.model.EntryModel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import jakarta.validation.constraints.NotNull;

/**
 * The interface Resource service.
 */
public interface ResourceService {

    /**
     * Find entries page.
     *
     * @param pageable the pageable
     * @return the page
     */
    Page<Entry> findEntries(@NotNull Pageable pageable);

    /**
     * Find entry entry.
     *
     * @param entryId the entry id
     * @return the entry
     */
    Entry findEntry(long entryId);

    /**
     * Create entry.
     *
     * @param entryDto the entry dto
     */
    void createEntry(EntryModel entryDto);

    /**
     * Update entry.
     *
     * @param entryId the entry id
     * @param entryDto the entry dto
     */
    void updateEntry(long entryId, EntryModel entryDto);

    /**
     * Remove entry.
     *
     * @param entryId the entry id
     */
    void removeEntry(long entryId);

    /**
     * Delete entry.
     *
     * @param entryId the entry id
     */
    void deleteEntry(long entryId);

    /**
     * Restore entry.
     *
     * @param entryId the entry id
     */
    void restoreEntry(long entryId);

}
