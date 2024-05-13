package com.eversis.esa.geoss.settings.system.event;

import com.eversis.esa.geoss.settings.common.properties.Constants;
import com.eversis.esa.geoss.settings.system.domain.ApiSettings;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * The type Api settings repository event handler.
 */
@Log4j2
@RequiredArgsConstructor
@Component
@RepositoryEventHandler
public class ApiSettingsRepositoryEventHandler {

    /**
     * Handle before create.
     *
     * @param apiSettings the api settings
     */
    @HandleBeforeCreate
    void handleBeforeCreate(final ApiSettings apiSettings) {
        log.debug("apiSettings:{}", apiSettings);
        if (apiSettings.getSiteId() == null) {
            apiSettings.setSiteId(Constants.DEFAULT_SITE_ID);
        }
    }

    /**
     * Handle before save.
     *
     * @param apiSettings the api settings
     */
    @HandleBeforeSave
    void handleBeforeSave(final ApiSettings apiSettings) {
        log.debug("apiSettings:{}", apiSettings);
        if (apiSettings.getSiteId() == null) {
            apiSettings.setSiteId(Constants.DEFAULT_SITE_ID);
        }
    }

    /**
     * Handle before delete.
     *
     * @param apiSettings the api settings
     */
    @HandleBeforeDelete
    void handleBeforeDelete(final ApiSettings apiSettings) {
        log.debug("apiSettings:{}", apiSettings);
    }
}
