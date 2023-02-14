package com.eversis.esa.geoss.settings.system.repository;

import com.eversis.esa.geoss.settings.system.domain.ApiSettings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.Optional;

/**
 * The interface Api settings repository.
 */
@RepositoryRestResource(collectionResourceRel = "apiSettings", path = "api-settings")
public interface ApiSettingsRepository extends JpaRepository<ApiSettings, Long> {

    /**
     * Find by name optional.
     *
     * @param name the name
     * @return the optional
     */
    @RestResource(path = "setting")
    Optional<ApiSettings> findByName(@Param("name") String name);

    /**
     * Find by category list.
     *
     * @param category the category
     * @return the list
     */
    @RestResource(path = "settings")
    List<ApiSettings> findByCategory(@Param("category") String category);
}
