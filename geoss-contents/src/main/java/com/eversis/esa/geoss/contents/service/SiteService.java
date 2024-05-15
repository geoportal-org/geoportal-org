package com.eversis.esa.geoss.contents.service;

import com.eversis.esa.geoss.contents.domain.Site;

/**
 * The interface Site service.
 */
public interface SiteService {

    /**
     * Delete site.
     *
     * @param siteId the site id
     */
    void deleteSite(long siteId);

    /**
     * Create site site.
     *
     * @param site the site
     * @return the site
     */
    Site createSite(Site site);

}
