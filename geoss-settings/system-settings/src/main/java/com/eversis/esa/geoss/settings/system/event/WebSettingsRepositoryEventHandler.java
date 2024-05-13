package com.eversis.esa.geoss.settings.system.event;

import com.eversis.esa.geoss.settings.common.properties.Constants;
import com.eversis.esa.geoss.settings.system.domain.WebSettings;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

/**
 * The type Web settings repository event handler.
 */
@Log4j2
@RequiredArgsConstructor
@Component
@RepositoryEventHandler
public class WebSettingsRepositoryEventHandler {

    /**
     * Handle before create.
     *
     * @param webSettings the web settings
     */
    @HandleBeforeCreate
    void handleBeforeCreate(final WebSettings webSettings) {
        log.debug("webSettings:{}", webSettings);
        if (webSettings.getSiteId() == null) {
            webSettings.setSiteId(Constants.DEFAULT_SITE_ID);
        }
    }

    /**
     * Handle before save.
     *
     * @param webSettings the web settings
     */
    @HandleBeforeSave
    void handleBeforeSave(final WebSettings webSettings) {
        log.debug("webSettings:{}", webSettings);
        if (webSettings.getSiteId() == null) {
            webSettings.setSiteId(Constants.DEFAULT_SITE_ID);
        }
    }

    /**
     * Handle before delete.
     *
     * @param webSettings the web settings
     */
    @HandleBeforeDelete
    void handleBeforeDelete(final WebSettings webSettings) {
        log.debug("webSettings:{}", webSettings);
    }
}
