package com.eversis.esa.geoss.settings.system.service;

import com.eversis.esa.geoss.settings.system.domain.WebSettings;
import com.eversis.esa.geoss.settings.system.domain.WebSettingsKey;
import com.eversis.esa.geoss.settings.system.domain.WebSettingsSet;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * The interface Web settings service.
 */
public interface WebSettingsService {

    /**
     * Gets site web settings.
     *
     * @param siteId the site id
     * @param webSettingsSet the web settings set
     * @param webSettingsKey the web settings key
     * @return the site web settings
     */
    WebSettings getSiteWebSettings(Long siteId, WebSettingsSet webSettingsSet, WebSettingsKey webSettingsKey);

    /**
     * Gets site web settings.
     *
     * @param siteId the site id
     * @param webSettingsSet the web settings set
     * @return the site web settings
     */
    List<WebSettings> getSiteWebSettings(Long siteId, WebSettingsSet webSettingsSet);

    /**
     * Gets site web settings.
     *
     * @param siteId the site id
     * @param pageable the pageable
     * @return the site web settings
     */
    Page<WebSettings> getSiteWebSettings(Long siteId, Pageable pageable);

    /**
     * Delete site web settings.
     *
     * @param siteId the site id
     */
    void deleteSiteWebSettings(Long siteId);
}
