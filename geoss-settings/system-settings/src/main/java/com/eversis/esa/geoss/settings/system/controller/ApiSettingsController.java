package com.eversis.esa.geoss.settings.system.controller;

import com.eversis.esa.geoss.settings.system.domain.ApiSettingsKey;
import com.eversis.esa.geoss.settings.system.domain.ApiSettingsSet;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.EnumSet;
import java.util.Set;

/**
 * The type Api settings controller.
 */
@Log4j2
@RequiredArgsConstructor
@RepositoryRestController("/api-settings")
@ResponseBody
@Tag(name = "api-settings")
public class ApiSettingsController {

    /**
     * Options api settings sets enum set.
     *
     * @return the enum set
     */
    @RequestMapping(path = "/sets", method = RequestMethod.OPTIONS)
    EnumSet<ApiSettingsSet> optionsApiSettingsSets() {
        return EnumSet.allOf(ApiSettingsSet.class);
    }

    /**
     * Options api settings keys set.
     *
     * @param apiSettingsSet the api settings set
     * @return the set
     */
    @RequestMapping(path = "/sets/{set}/keys", method = RequestMethod.OPTIONS)
    Set<ApiSettingsKey> optionsApiSettingsKeys(@PathVariable("set") ApiSettingsSet apiSettingsSet) {
        return apiSettingsSet.getKeys();
    }
}
