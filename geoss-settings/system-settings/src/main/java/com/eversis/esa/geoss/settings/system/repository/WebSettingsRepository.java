package com.eversis.esa.geoss.settings.system.repository;

import com.eversis.esa.geoss.settings.system.domain.WebSettings;
import com.eversis.esa.geoss.settings.system.domain.WebSettingsKey;
import com.eversis.esa.geoss.settings.system.domain.WebSettingsSet;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * The interface Web settings repository.
 */
@RepositoryRestResource(path = "web-settings", collectionResourceRel = "webSettings", itemResourceRel = "webSetting")
@Tag(name = "web-settings")
public interface WebSettingsRepository extends JpaRepository<WebSettings, Long> {

    /**
     * Find by set and key optional.
     *
     * @param set the set
     * @param key the key
     * @return the optional
     */
    @Operation(
            description = "Get web settings by set and key.",
            summary = "Get web settings by set and key.")
    @RestResource(path = "setting")
    Optional<WebSettings> findBySetAndKey(@Param("set") WebSettingsSet set, @Param("key") WebSettingsKey key);

    /**
     * Find by set list.
     *
     * @param set the set
     * @return the list
     */
    @Operation(
            description = "Get web settings by set.",
            summary = "Get web settings by set.")
    @RestResource(path = "settings")
    List<WebSettings> findBySet(@Param("set") @NotNull WebSettingsSet set);
}
