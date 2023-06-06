package com.eversis.esa.geoss.settings.dab.service;

import com.eversis.esa.geoss.settings.instance.domain.Catalog;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The interface Dab service.
 */
public interface DabService {

    /**
     * Gets catalogs.
     *
     * @param pageable the pageable
     * @return the catalogs
     */
    Page<Catalog> getCatalogs(Pageable pageable);
}
