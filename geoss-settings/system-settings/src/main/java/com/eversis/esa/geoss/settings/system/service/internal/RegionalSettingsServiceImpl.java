package com.eversis.esa.geoss.settings.system.service.internal;

import com.eversis.esa.geoss.settings.system.domain.RegionalSettings;
import com.eversis.esa.geoss.settings.system.domain.RegionalSettingsId;
import com.eversis.esa.geoss.settings.system.domain.RegionalSettingsKey;
import com.eversis.esa.geoss.settings.system.model.RegionalSettingsModel;
import com.eversis.esa.geoss.settings.system.repository.RegionalSettingsRepository;
import com.eversis.esa.geoss.settings.system.service.RegionalSettingsService;
import com.eversis.esa.geoss.settings.system.support.RegionalSettingsModelToRegionalSettingsMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;

/**
 * The type Regional settings service.
 */
@Log4j2
@RequiredArgsConstructor
@Service
@Transactional
public class RegionalSettingsServiceImpl implements RegionalSettingsService {

    private final RegionalSettingsRepository regionalSettingsRepository;

    private final RegionalSettingsModelToRegionalSettingsMapper regionalSettingsModelToRegionalSettingsMapper;

    private ConversionService conversionService;

    /**
     * Sets conversion service.
     *
     * @param conversionService the conversion service
     */
    @Autowired
    public void setConversionService(@Qualifier("mvcConversionService") ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public RegionalSettingsModel getRegionalSettings(RegionalSettingsKey regionalSettingsKey) {
        log.debug("regionalSettingsKey:{}", regionalSettingsKey);
        RegionalSettingsId regionalSettingsId = new RegionalSettingsId(regionalSettingsKey);
        return regionalSettingsRepository.findById(regionalSettingsId)
                .map(regionalSettings -> {
                    log.debug("regionalSettings:{}", regionalSettings);
                    RegionalSettingsModel regionalSettingsModel = conversionService.convert(regionalSettings,
                            RegionalSettingsModel.class);
                    log.debug("regionalSettingsModel:{}", regionalSettingsModel);
                    return regionalSettingsModel;
                })
                .orElseThrow(() -> new ResourceNotFoundException("Regional settings not found"));
    }

    @Override
    public RegionalSettingsModel createOrUpdateRegionalSettings(RegionalSettingsKey regionalSettingsKey,
            RegionalSettingsModel regionalSettingsModel, Consumer<Long> versionChecker) {
        log.debug("regionalSettingsKey:{}", regionalSettingsKey);
        RegionalSettingsId regionalSettingsId = new RegionalSettingsId(regionalSettingsKey);
        return regionalSettingsRepository.findById(regionalSettingsId)
                .map(regionalSettings -> {
                    log.debug("regionalSettings:{}", regionalSettings);
                    versionChecker.accept(regionalSettings.getVersion());

                    return saveOrUpdateRegionalSettings(regionalSettingsModel, regionalSettings);
                })
                .orElseGet(() -> {
                    RegionalSettings regionalSettings = new RegionalSettings();
                    regionalSettings.setId(regionalSettingsKey);

                    return saveOrUpdateRegionalSettings(regionalSettingsModel, regionalSettings);
                })
                ;
    }

    @Override
    public RegionalSettingsModel updateRegionalSettings(RegionalSettingsKey regionalSettingsKey,
            RegionalSettingsModel regionalSettingsModel, Consumer<Long> versionChecker) {
        log.debug("regionalSettingsKey:{}", regionalSettingsKey);
        RegionalSettingsId regionalSettingsId = new RegionalSettingsId(regionalSettingsKey);
        return regionalSettingsRepository.findById(regionalSettingsId)
                .map(regionalSettings -> {
                    log.debug("regionalSettings:{}", regionalSettings);
                    versionChecker.accept(regionalSettings.getVersion());

                    return saveOrUpdateRegionalSettings(regionalSettingsModel, regionalSettings);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Regional settings not found"))
                ;
    }

    private RegionalSettingsModel saveOrUpdateRegionalSettings(RegionalSettingsModel regionalSettingsModel,
            RegionalSettings regionalSettings) {
        log.debug("regionalSettingsModel:{}", regionalSettingsModel);

        regionalSettings = regionalSettingsModelToRegionalSettingsMapper.update(regionalSettingsModel,
                regionalSettings);
        log.debug("regionalSettings:{}", regionalSettings);

        regionalSettings = regionalSettingsRepository.save(regionalSettings);
        log.debug("regionalSettings:{}", regionalSettings);

        RegionalSettingsModel regionalSettingsModelResult = conversionService.convert(regionalSettings,
                RegionalSettingsModel.class);
        log.debug("regionalSettingsModel:{}", regionalSettingsModelResult);
        return regionalSettingsModelResult;
    }
}
