package com.eversis.esa.geoss.settings.instance.service;

import com.eversis.esa.geoss.settings.instance.model.CatalogOptionModel;

import java.util.List;

/**
 * The interface Catalog service.
 */
public interface CatalogService {

    /**
     * Gets catalog options.
     *
     * @param id the id
     * @return the catalog options
     */
    List<CatalogOptionModel> getCatalogOptions(Long id);

    /**
     * Remove catalog options.
     *
     * @param id the id
     */
    void removeCatalogOptions(Long id);

    /**
     * Add catalog option catalog option model.
     *
     * @param id the id
     * @param CatalogOptionModel the catalog option model
     * @return the catalog option model
     */
    CatalogOptionModel addCatalogOption(Long id, CatalogOptionModel CatalogOptionModel);

    /**
     * Gets catalog option.
     *
     * @param id the id
     * @param optionId the option id
     * @return the catalog option
     */
    CatalogOptionModel getCatalogOption(Long id, Long optionId);

    /**
     * Sets catalog option.
     *
     * @param id the id
     * @param optionId the option id
     * @param CatalogOptionModel the catalog option model
     * @return the catalog option
     */
    CatalogOptionModel setCatalogOption(Long id, Long optionId, CatalogOptionModel CatalogOptionModel);

    /**
     * Remove catalog option.
     *
     * @param id the id
     * @param optionId the option id
     */
    void removeCatalogOption(Long id, Long optionId);

    /**
     * Update catalog option catalog option model.
     *
     * @param id the id
     * @param optionId the option id
     * @param CatalogOptionModel the catalog option model
     * @return the catalog option model
     */
    CatalogOptionModel updateCatalogOption(Long id, Long optionId, CatalogOptionModel CatalogOptionModel);
}
