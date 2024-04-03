package com.eversis.esa.geoss.curated.extensions.service;

import com.eversis.esa.geoss.curated.extensions.domain.EntryExtension;
import com.eversis.esa.geoss.curated.extensions.model.EntryExtensionModel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import jakarta.validation.constraints.NotNull;

/**
 * The EntryExtensionService interface. This interface defines the contract for managing EntryExtension entities. It
 * includes methods for finding, creating, updating, deleting, and restoring EntryExtension entities. It also includes a
 * method for getting or creating an EntryExtension entity, and a method for saving an EntryExtension entity. Each
 * method should be implemented by any class that provides the business logic for managing EntryExtension entities.
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

    /**
     * Gets or create entry extension.
     *
     * @param entryExtensionModel the entry extension model
     * @return the or create entry extension
     */
    EntryExtension getOrCreateEntryExtension(EntryExtensionModel entryExtensionModel);

    /**
     * Save entry extension entry extension.
     *
     * @param entryExtensionModel the entry extension model
     * @return the entry extension
     */
    EntryExtension saveEntryExtension(EntryExtensionModel entryExtensionModel);

}
