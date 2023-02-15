package com.eversis.esa.geoss.settings.system.controller;

import com.eversis.esa.geoss.settings.system.domain.ApiSettingsKey;
import com.eversis.esa.geoss.settings.system.domain.ApiSettingsSet;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.EnumSet;

/**
 * The type Api settings controller.
 */
@Log4j2
@RequiredArgsConstructor
@Validated
@RepositoryRestController("/api-settings")
@ResponseBody
@Tag(name = "api-settings")
public class ApiSettingsController {

    /**
     * Gets api settings sets.
     *
     * @return the api settings sets
     */
    @RequestMapping(path = "/sets", method = RequestMethod.OPTIONS)
    EnumSet<ApiSettingsSet> getApiSettingsSets() {
        return EnumSet.allOf(ApiSettingsSet.class);
    }

    /**
     * Gets api settings keys.
     *
     * @return the api settings keys
     */
    @RequestMapping(path = "/keys", method = RequestMethod.OPTIONS)
    EnumSet<ApiSettingsKey> getApiSettingsKeys() {
        return EnumSet.allOf(ApiSettingsKey.class);
    }
}
