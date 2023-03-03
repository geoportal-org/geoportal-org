package com.eversis.esa.geoss.settings.system.controller;

import com.eversis.esa.geoss.settings.system.domain.WebSettingsSet;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.EnumSet;

/**
 * The type Web settings controller.
 */
@Log4j2
@RequiredArgsConstructor
@RepositoryRestController("/web-settings")
@ResponseBody
@Tag(name = "web-settings")
public class WebSettingsController {

    /**
     * Gets web settings sets.
     *
     * @return the web settings sets
     */
    @RequestMapping(path = "/sets", method = RequestMethod.OPTIONS)
    EnumSet<WebSettingsSet> getWebSettingsSets() {
        return EnumSet.allOf(WebSettingsSet.class);
    }

    /**
     * Gets web settings keys.
     *
     * @param webSettingsSet the web settings set
     * @return the web settings keys
     */
    @RequestMapping(path = "/sets/{set}/keys", method = RequestMethod.OPTIONS)
    EnumSet<?> getWebSettingsKeys(@PathVariable("set") WebSettingsSet webSettingsSet) {
        return webSettingsSet.getKeys();
    }
}
