package com.eversis.esa.geoss.settings.system.repository;

import com.eversis.esa.geoss.settings.system.domain.WebSettings;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * The interface Web settings repository.
 */
@RepositoryRestResource(path = "web-settings", collectionResourceRel = "webSettings", itemResourceRel = "webSetting")
@Tag(name = "web-settings")
public interface WebSettingsRepository extends JpaRepository<WebSettings, Long> {

}
