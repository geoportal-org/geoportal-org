package com.eversis.esa.geoss.curated.relations.service;

import com.eversis.esa.geoss.curated.relations.domain.EntryRelation;
import com.eversis.esa.geoss.curated.relations.model.EntryRelationIdModel;
import com.eversis.esa.geoss.curated.relations.model.EntryRelationModel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import jakarta.validation.constraints.NotNull;

/**
 * The interface Entry relation service.
 */
public interface EntryRelationService {

    /**
     * Find entry relations page.
     *
     * @param pageable the pageable
     * @return the page
     */
    Page<EntryRelation> findEntryRelations(@NotNull Pageable pageable);

    /**
     * Find entry relation entry relation.
     *
     * @param entryRelationId the entry relation id
     * @return the entry relation
     */
    EntryRelation findEntryRelation(EntryRelationIdModel entryRelationId);

    /**
     * Create entry relation.
     *
     * @param entryRelationDto the entry relation dto
     */
    void createEntryRelation(EntryRelationModel entryRelationDto);

    /**
     * Update entry relation.
     *
     * @param entryRelationId the entry relation id
     * @param entryRelationDto the entry relation dto
     */
    void updateEntryRelation(EntryRelationIdModel entryRelationId, EntryRelationModel entryRelationDto);

    /**
     * Remove entry relation.
     *
     * @param entryRelationId the entry relation id
     */
    void removeEntryRelation(EntryRelationIdModel entryRelationId);

    /**
     * Delete entry relation.
     *
     * @param entryRelationId the entry relation id
     */
    void deleteEntryRelation(EntryRelationIdModel entryRelationId);

    /**
     * Restore entry relation.
     *
     * @param entryRelationId the entry relation id
     */
    void restoreEntryRelation(EntryRelationIdModel entryRelationId);

    /**
     * Gets or create entry.
     *
     * @param model the model
     * @return the or create entry
     */
    EntryRelation getOrCreateEntry(EntryRelationModel model);

}
