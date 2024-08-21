package com.eversis.esa.geoss.contents.service;

import com.eversis.esa.geoss.contents.domain.Site;

import org.springframework.web.multipart.MultipartFile;

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
     * @param file the file
     * @return the site
     */
    Site createSite(Site site, MultipartFile file);

    /**
     * Exists by url boolean.
     *
     * @param url the url
     * @return the boolean
     */
    boolean existsByUrl(String url);

}
