package com.eversis.esa.geoss.settings.system.service.internal;

import com.eversis.esa.geoss.settings.system.domain.ApiSettings;
import com.eversis.esa.geoss.settings.system.domain.ApiSettingsKey;
import com.eversis.esa.geoss.settings.system.domain.ApiSettingsSet;
import com.eversis.esa.geoss.settings.system.repository.ApiSettingsRepository;
import com.eversis.esa.geoss.settings.system.service.ApiSettingsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * The type Api settings service.
 */
@Log4j2
@RequiredArgsConstructor
@Service
@Transactional
public class ApiSettingsServiceImpl implements ApiSettingsService {

    private final ApiSettingsRepository apiSettingsRepository;

    @Override
    public ApiSettings getSiteApiSettings(Long siteId, ApiSettingsSet apiSettingsSet, ApiSettingsKey apiSettingsKey) {
        log.debug("siteId:{},set:{},key:{}", siteId, apiSettingsSet, apiSettingsKey);
        return apiSettingsRepository.findBySiteIdAndSetAndKey(siteId, apiSettingsSet, apiSettingsKey)
                .map(apiSettings -> {
                    log.debug("apiSettings:{}", apiSettings);
                    return apiSettings;
                })
                .orElseThrow(() -> new ResourceNotFoundException("Api settings not found"));
    }

    @Override
    public List<ApiSettings> getSiteApiSettings(Long siteId, ApiSettingsSet apiSettingsSet) {
        log.debug("siteId:{},set:{}", siteId, apiSettingsSet);
        return apiSettingsRepository.findBySiteIdAndSet(siteId, apiSettingsSet);
    }

    @Override
    public Page<ApiSettings> getSiteApiSettings(Long siteId, Pageable pageable) {
        log.debug("siteId:{},pageable:{}", siteId, pageable);
        return apiSettingsRepository.findBySiteId(siteId, pageable);
    }

    @Override
    public void deleteSiteApiSettings(Long siteId) {
        log.debug("siteId:{}", siteId);
        long deleted = apiSettingsRepository.deleteAllBySiteId(siteId);
        log.debug("deleted:{}", deleted);
    }
}
