package com.eversis.esa.geoss.curated.extensions.service;

import jakarta.validation.constraints.NotNull;

import com.eversis.esa.geoss.curated.extensions.domain.EntryExtension;
import com.eversis.esa.geoss.curated.extensions.model.EntryExtensionModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The interface Entry extension service.
 */
public interface EntryExtensionService {

    /**
     * Find entry extensions page.
     *
     * @param pageable the pageable
     * @return the page
     */
    Page<EntryExtension> findEntryExtensions(@NotNull Pageable pageable);

    /**
     * Find entry extension entry extension.
     *
     * @param entryExtensionId the entry extension id
     * @return the entry extension
     */
    EntryExtension findEntryExtension(long entryExtensionId);

    /**
     * Create entry extension.
     *
     * @param entryExtensionDto the entry extension dto
     */
    void createEntryExtension(EntryExtensionModel entryExtensionDto);

    /**
     * Update entry extension.
     *
     * @param entryExtensionId the entry extension id
     * @param entryExtensionDto the entry extension dto
     */
    void updateEntryExtension(long entryExtensionId, EntryExtensionModel entryExtensionDto);

    /**
     * Remove entry extension.
     *
     * @param entryExtensionId the entry extension id
     */
    void removeEntryExtension(long entryExtensionId);

    /**
     * Delete entry extension.
     *
     * @param entryExtensionId the entry extension id
     */
    void deleteEntryExtension(long entryExtensionId);

    /**
     * Restore entry extension.
     *
     * @param entryExtensionId the entry extension id
     */
    void restoreEntryExtension(long entryExtensionId);

}
