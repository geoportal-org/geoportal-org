package com.eversis.esa.geoss.curated.extensions.service;

import com.eversis.esa.geoss.curated.extensions.domain.EntryExtension;
import com.eversis.esa.geoss.curated.extensions.domain.TransferOptionExtension;
import com.eversis.esa.geoss.curated.extensions.model.TransferOptionExtensionModel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

/**
 * The interface Transfer option extension service.
 */
public interface TransferOptionExtensionService {

    /**
     * Find transfer option extensions page.
     *
     * @param pageable the pageable
     * @return the page
     */
    Page<TransferOptionExtension> findTransferOptionExtensions(@NotNull Pageable pageable);

    /**
     * Find transfer option extension transfer option extension.
     *
     * @param transferOptionExtensionId the transfer option extension id
     * @return the transfer option extension
     */
    TransferOptionExtension findTransferOptionExtension(long transferOptionExtensionId);

    /**
     * Find transfer option extensions by extension id set.
     *
     * @param extensionId the extension id
     * @return the set
     */
    Set<TransferOptionExtension> findTransferOptionExtensionsByExtensionId(long extensionId);

    /**
     * Save transfer option extensions list.
     *
     * @param transferOptionExtensionDto the transfer option extension dto
     * @param entryExtension the entry extension
     * @return the list
     */
    List<TransferOptionExtension> saveTransferOptionExtensions(
            List<TransferOptionExtensionModel> transferOptionExtensionDto,
            EntryExtension entryExtension);

    /**
     * Save transfer option extension.
     *
     * @param transferOptionExtension the transfer option extension
     */
    void saveTransferOptionExtension(TransferOptionExtension transferOptionExtension);

    /**
     * Remove transfer option extension.
     *
     * @param transferOptionExtensionId the transfer option extension id
     */
    void removeTransferOptionExtension(long transferOptionExtensionId);

    /**
     * Delete transfer option extension.
     *
     * @param transferOptionExtensionId the transfer option extension id
     */
    void deleteTransferOptionExtension(long transferOptionExtensionId);

    /**
     * Update transfer option extensions by extension id.
     *
     * @param extensionId the extension id
     * @param transferOptionExtensions the transfer option extensions
     */
    void updateTransferOptionExtensionsByExtensionId(long extensionId,
            Set<TransferOptionExtensionModel> transferOptionExtensions);

    /**
     * Create transfer option extension transfer option extension.
     *
     * @param transferOptionExtension the transfer option extension
     * @return the transfer option extension
     */
    TransferOptionExtension createTransferOptionExtension(TransferOptionExtension transferOptionExtension);
}
