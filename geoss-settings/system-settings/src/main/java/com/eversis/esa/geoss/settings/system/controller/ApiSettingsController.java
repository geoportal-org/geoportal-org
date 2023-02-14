package com.eversis.esa.geoss.settings.system.controller;

import com.eversis.esa.geoss.settings.system.domain.ApiSettingsCategory;
import com.eversis.esa.geoss.settings.system.domain.ApiSettingsName;

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
     * Gets api settings categories.
     *
     * @return the api settings categories
     */
    @RequestMapping(path = "/categories", method = RequestMethod.OPTIONS)
    EnumSet<ApiSettingsCategory> getApiSettingsCategories() {
        return EnumSet.allOf(ApiSettingsCategory.class);
    }

    /**
     * Gets api settings names.
     *
     * @return the api settings names
     */
    @RequestMapping(path = "/names", method = RequestMethod.OPTIONS)
    EnumSet<ApiSettingsName> getApiSettingsNames() {
        return EnumSet.allOf(ApiSettingsName.class);
    }
}
