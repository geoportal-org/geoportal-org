package com.eversis.esa.geoss.settings.site.controller;

import com.eversis.esa.geoss.settings.site.service.SiteService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The type Site controller.
 */
@Log4j2
@RequiredArgsConstructor
@BasePathAwareController("/sites")
@ResponseBody
@Tag(name = "site")
public class SiteController {

    private final SiteService siteService;

    /**
     * Delete site api-settings, web-settings, layers and views.
     *
     * @param siteId the site id
     */
    @PreAuthorize("(hasRole('API_REMOVER') and hasRole('LAYER_REMOVER') and hasRole('VIEW_REMOVER') and "
                  + "hasRole('WEB_REMOVER')) or hasRole('ADMIN')")
    @DeleteMapping("/{siteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteSite(@PathVariable Long siteId) {
        siteService.deleteSite(siteId);
    }
}
