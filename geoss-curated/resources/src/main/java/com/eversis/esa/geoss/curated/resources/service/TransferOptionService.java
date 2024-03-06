package com.eversis.esa.geoss.curated.resources.service;

import com.eversis.esa.geoss.curated.resources.domain.Entry;
import com.eversis.esa.geoss.curated.resources.domain.TransferOption;
import com.eversis.esa.geoss.curated.resources.model.TransferOptionModel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

/**
 * The interface Transfer option service.
 */
public interface TransferOptionService {

    /**
     * Find transfer options page.
     *
     * @param pageable the pageable
     * @return the page
     */
    Page<TransferOption> findTransferOptions(@NotNull Pageable pageable);

    /**
     * Find transfer option transfer option.
     *
     * @param transferOptionId the transfer option id
     * @return the transfer option
     */
    TransferOption findTransferOption(long transferOptionId);

    /**
     * Find transfer options by entry id set.
     *
     * @param entryId the entry id
     * @return the set
     */
    Set<TransferOption> findTransferOptionsByEntryId(long entryId);

    /**
     * Save transfer options list.
     *
     * @param transferOptionDto the transfer option dto
     * @param relatedEntry the related entry
     * @return the list
     */
    List<TransferOption> saveTransferOptions(List<TransferOptionModel> transferOptionDto, Entry relatedEntry);

    /**
     * Save transfer option.
     *
     * @param transferOption the transfer option
     */
    void saveTransferOption(TransferOption transferOption);

    /**
     * Remove transfer option.
     *
     * @param transferOptionId the transfer option id
     */
    void removeTransferOption(long transferOptionId);

    /**
     * Delete transfer option.
     *
     * @param transferOptionId the transfer option id
     */
    void deleteTransferOption(long transferOptionId);

    /**
     * Update transfer options by entry id.
     *
     * @param entryId the entry id
     * @param transferOptions the transfer options
     */
    void updateTransferOptionsByEntryId(long entryId, Set<TransferOptionModel> transferOptions);

    /**
     * Create transfer option transfer option.
     *
     * @param transferOption the transfer option
     * @return the transfer option
     */
    TransferOption createTransferOption(TransferOption transferOption);

}
