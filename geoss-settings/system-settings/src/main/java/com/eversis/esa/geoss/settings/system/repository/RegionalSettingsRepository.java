package com.eversis.esa.geoss.settings.system.repository;

import com.eversis.esa.geoss.settings.system.domain.RegionalSettings;
import com.eversis.esa.geoss.settings.system.domain.RegionalSettingsId;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * The interface Regional settings repository.
 */
@RepositoryRestResource(path = "regional-settings", collectionResourceRel = "regionalSettings",
                        itemResourceRel = "regionalSetting", exported = false)
@Tag(name = "regional-settings")
public interface RegionalSettingsRepository extends JpaRepository<RegionalSettings, RegionalSettingsId> {

}
