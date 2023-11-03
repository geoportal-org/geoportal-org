package com.eversis.esa.geoss.personaldata.profile.event;

import com.eversis.esa.geoss.personaldata.profile.domain.Setting;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * The type Setting repository event handler.
 */
@Log4j2
@RequiredArgsConstructor
@Component
@RepositoryEventHandler
public class SettingRepositoryEventHandler {

    /**
     * Handle before create.
     *
     * @param setting the setting
     */
    @HandleBeforeCreate
    void handleBeforeCreate(final Setting setting) {
        log.debug("setting:{}", setting);
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        setting.setUser(userName);
    }

    /**
     * Handle before save.
     *
     * @param setting the setting
     */
    @PreAuthorize("#setting?.user == authentication.name")
    @HandleBeforeSave
    void handleBeforeSave(final Setting setting) {
        log.debug("setting:{}", setting);
    }

    /**
     * Handle before delete.
     *
     * @param setting the setting
     */
    @PreAuthorize("#setting?.user == authentication.name or hasAnyRole('USER_SETTINGS_REMOVER', 'ADMIN')")
    @HandleBeforeDelete
    void handleBeforeDelete(final Setting setting) {
        log.debug("setting:{}", setting);
    }
}
