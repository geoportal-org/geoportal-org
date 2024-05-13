package com.eversis.esa.geoss.settings.site.service.internal;

import com.eversis.esa.geoss.settings.instance.service.LayerService;
import com.eversis.esa.geoss.settings.instance.service.ViewService;
import com.eversis.esa.geoss.settings.site.service.SiteService;
import com.eversis.esa.geoss.settings.system.service.ApiSettingsService;
import com.eversis.esa.geoss.settings.system.service.WebSettingsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The type Site service.
 */
@Log4j2
@RequiredArgsConstructor
@Service
@Transactional
public class SiteServiceImpl implements SiteService {

    private final ApiSettingsService apiSettingsService;

    private final WebSettingsService webSettingsService;

    private final LayerService layerService;

    private final ViewService viewService;

    @Override
    public void deleteSite(Long siteId) {
        log.info("Delete site '{}' api settings...", siteId);
        apiSettingsService.deleteSiteApiSettings(siteId);
        log.info("Delete site '{}' web settings...", siteId);
        webSettingsService.deleteSiteWebSettings(siteId);
        log.info("Delete site '{}' layers...", siteId);
        layerService.deleteSiteLayers(siteId);
        log.info("Delete site '{}' views...", siteId);
        viewService.deleteSiteViews(siteId);
        log.info("Delete site '{}' done.", siteId);
    }
}
