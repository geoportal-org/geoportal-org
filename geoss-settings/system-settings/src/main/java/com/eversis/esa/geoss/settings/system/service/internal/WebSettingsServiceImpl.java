package com.eversis.esa.geoss.settings.system.service.internal;

import com.eversis.esa.geoss.settings.system.domain.WebSettings;
import com.eversis.esa.geoss.settings.system.domain.WebSettingsKey;
import com.eversis.esa.geoss.settings.system.domain.WebSettingsSet;
import com.eversis.esa.geoss.settings.system.repository.WebSettingsRepository;
import com.eversis.esa.geoss.settings.system.service.WebSettingsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * The type Web settings service.
 */
@Log4j2
@RequiredArgsConstructor
@Service
@Transactional
public class WebSettingsServiceImpl implements WebSettingsService {

    private final WebSettingsRepository webSettingsRepository;

    @Override
    public WebSettings getSiteWebSettings(Long siteId, WebSettingsSet webSettingsSet, WebSettingsKey webSettingsKey) {
        log.debug("siteId:{},set:{},key:{}", siteId, webSettingsSet, webSettingsKey);
        return webSettingsRepository.findBySiteIdAndSetAndKey(siteId, webSettingsSet, webSettingsKey)
                .map(webSettings -> {
                    log.debug("webSettings:{}", webSettings);
                    return webSettings;
                })
                .orElseThrow(() -> new ResourceNotFoundException("Web settings not found"));
    }

    @Override
    public List<WebSettings> getSiteWebSettings(Long siteId, WebSettingsSet webSettingsSet) {
        log.debug("siteId:{},set:{}", siteId, webSettingsSet);
        return webSettingsRepository.findBySiteIdAndSet(siteId, webSettingsSet);
    }

    @Override
    public Page<WebSettings> getSiteWebSettings(Long siteId, Pageable pageable) {
        log.debug("siteId:{},pageable:{}", siteId, pageable);
        return webSettingsRepository.findBySiteId(siteId, pageable);
    }

    @Override
    public void deleteSiteWebSettings(Long siteId) {
        log.debug("siteId:{}", siteId);
        long deleted = webSettingsRepository.deleteAllBySiteId(siteId);
        log.debug("deleted:{}", deleted);
    }
}
