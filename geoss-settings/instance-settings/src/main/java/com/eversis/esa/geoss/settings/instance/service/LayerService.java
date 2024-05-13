package com.eversis.esa.geoss.settings.instance.service;

import com.eversis.esa.geoss.settings.instance.model.LayerModel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The interface Layer service.
 */
public interface LayerService {

    /**
     * Gets site layers.
     *
     * @param siteId the site id
     * @param pageable the pageable
     * @return the site layers
     */
    Page<LayerModel> getSiteLayers(Long siteId, Pageable pageable);

    /**
     * Delete site layers.
     *
     * @param siteId the site id
     */
    void deleteSiteLayers(Long siteId);
}
