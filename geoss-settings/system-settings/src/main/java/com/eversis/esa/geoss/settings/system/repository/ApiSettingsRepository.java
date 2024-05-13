package com.eversis.esa.geoss.settings.system.repository;

import com.eversis.esa.geoss.settings.system.domain.ApiSettings;
import com.eversis.esa.geoss.settings.system.domain.ApiSettingsKey;
import com.eversis.esa.geoss.settings.system.domain.ApiSettingsSet;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.Optional;

/**
 * The interface Api settings repository.
 */
@RepositoryRestResource(path = "api-settings", collectionResourceRel = "apiSettings", itemResourceRel = "apiSetting")
@Tag(name = "api-settings")
public interface ApiSettingsRepository extends JpaRepository<ApiSettings, Long> {

    /**
     * Find by site page.
     *
     * @param siteId the site id
     * @param pageable the pageable
     * @return the page
     */
    @RestResource(exported = false)
    Page<ApiSettings> findBySiteId(Long siteId, Pageable pageable);

    /**
     * Find by site and set list.
     *
     * @param siteId the site id
     * @param set the set
     * @return the list
     */
    @RestResource(exported = false)
    List<ApiSettings> findBySiteIdAndSet(Long siteId, ApiSettingsSet set);

    /**
     * Find by site and set and key optional.
     *
     * @param siteId the site id
     * @param set the set
     * @param key the key
     * @return the optional
     */
    @RestResource(exported = false)
    Optional<ApiSettings> findBySiteIdAndSetAndKey(Long siteId, ApiSettingsSet set, ApiSettingsKey key);

    /**
     * Delete all by site id long.
     *
     * @param siteId the site id
     * @return the long
     */
    @RestResource(exported = false)
    long deleteAllBySiteId(Long siteId);
}
