package com.eversis.esa.geoss.settings.system.repository;

import com.eversis.esa.geoss.settings.system.domain.ApiSettings;
import com.eversis.esa.geoss.settings.system.domain.ApiSettingsKey;
import com.eversis.esa.geoss.settings.system.domain.ApiSettingsSet;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * The interface Api settings repository.
 */
@RepositoryRestResource(path = "api-settings", collectionResourceRel = "apiSettings", itemResourceRel = "apiSetting")
@Tag(name = "api-settings")
public interface ApiSettingsRepository extends JpaRepository<ApiSettings, Long> {

    /**
     * Find by set and by key optional.
     *
     * @param set the set
     * @param key the key
     * @return the optional
     */
    @RestResource(path = "setting")
    Optional<ApiSettings> findBySetAndKey(@Param("set") ApiSettingsSet set, @Param("key") ApiSettingsKey key);

    /**
     * Find by set list.
     *
     * @param set the set
     * @return the list
     */
    @RestResource(path = "settings")
    List<ApiSettings> findBySet(@Param("set") @NotNull ApiSettingsSet set);
}
