package com.eversis.esa.geoss.curated.resources.service;

import com.eversis.esa.geoss.curated.resources.domain.Entry;
import com.eversis.esa.geoss.curated.resources.model.EntryModel;

/**
 * The interface Entry service.
 */
public interface EntryService {

    /**
     * Gets or create entry.
     *
     * @param entry the entry
     * @return the or create entry
     */
    Entry getOrCreateEntry(EntryModel entry);

    /**
     * Save entry entry.
     *
     * @param entryDto the entry dto
     * @return the entry
     */
    Entry saveEntry(EntryModel entryDto);

}
