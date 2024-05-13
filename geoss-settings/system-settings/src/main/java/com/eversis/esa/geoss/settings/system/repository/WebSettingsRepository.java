package com.eversis.esa.geoss.settings.system.repository;

import com.eversis.esa.geoss.settings.system.domain.WebSettings;
import com.eversis.esa.geoss.settings.system.domain.WebSettingsKey;
import com.eversis.esa.geoss.settings.system.domain.WebSettingsSet;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.Optional;

/**
 * The interface Web settings repository.
 */
@RepositoryRestResource(path = "web-settings", collectionResourceRel = "webSettings", itemResourceRel = "webSetting")
@Tag(name = "web-settings")
public interface WebSettingsRepository extends JpaRepository<WebSettings, Long> {

    /**
     * Find by site page.
     *
     * @param siteId the site id
     * @param pageable the pageable
     * @return the page
     */
    @RestResource(exported = false)
    Page<WebSettings> findBySiteId(Long siteId, Pageable pageable);

    /**
     * Find by site and set list.
     *
     * @param siteId the site id
     * @param set the set
     * @return the list
     */
    @RestResource(exported = false)
    List<WebSettings> findBySiteIdAndSet(Long siteId, WebSettingsSet set);

    /**
     * Find by site and set and key optional.
     *
     * @param siteId the site id
     * @param set the set
     * @param key the key
     * @return the optional
     */
    @RestResource(exported = false)
    Optional<WebSettings> findBySiteIdAndSetAndKey(Long siteId, WebSettingsSet set, WebSettingsKey key);

    /**
     * Delete all by site id long.
     *
     * @param siteId the site id
     * @return the long
     */
    @RestResource(exported = false)
    long deleteAllBySiteId(Long siteId);
}
