package com.eversis.esa.geoss.settings.system.service;

import com.eversis.esa.geoss.settings.system.domain.ApiSettings;
import com.eversis.esa.geoss.settings.system.domain.ApiSettingsKey;
import com.eversis.esa.geoss.settings.system.domain.ApiSettingsSet;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * The interface Api settings service.
 */
public interface ApiSettingsService {

    /**
     * Gets site api settings.
     *
     * @param siteId the site id
     * @param apiSettingsSet the api settings set
     * @param apiSettingsKey the api settings key
     * @return the site api settings
     */
    ApiSettings getSiteApiSettings(Long siteId, ApiSettingsSet apiSettingsSet, ApiSettingsKey apiSettingsKey);

    /**
     * Gets site api settings.
     *
     * @param siteId the site id
     * @param apiSettingsSet the api settings set
     * @return the site api settings
     */
    List<ApiSettings> getSiteApiSettings(Long siteId, ApiSettingsSet apiSettingsSet);

    /**
     * Gets site api settings.
     *
     * @param siteId the site id
     * @param pageable the pageable
     * @return the site api settings
     */
    Page<ApiSettings> getSiteApiSettings(Long siteId, Pageable pageable);

    /**
     * Delete site api settings.
     *
     * @param siteId the site id
     */
    void deleteSiteApiSettings(Long siteId);
}
