package com.eversis.esa.geoss.settings.system.service;

import com.eversis.esa.geoss.settings.system.domain.RegionalSettingsKey;
import com.eversis.esa.geoss.settings.system.model.RegionalSettingsModel;

import java.util.function.Consumer;

/**
 * The interface Regional settings service.
 */
public interface RegionalSettingsService {

    /**
     * Gets regional settings.
     *
     * @param regionalSettingsKey the regional settings key
     * @return the regional settings
     */
    RegionalSettingsModel getRegionalSettings(RegionalSettingsKey regionalSettingsKey);

    /**
     * Create or update regional settings regional settings model.
     *
     * @param regionalSettingsKey the regional settings key
     * @param regionalSettingsModel the regional settings model
     * @param versionChecker the version checker
     * @return the regional settings model
     */
    RegionalSettingsModel createOrUpdateRegionalSettings(RegionalSettingsKey regionalSettingsKey,
            RegionalSettingsModel regionalSettingsModel, Consumer<Long> versionChecker);

    /**
     * Update regional settings regional settings model.
     *
     * @param regionalSettingsKey the regional settings key
     * @param regionalSettingsModel the regional settings model
     * @param versionChecker the version checker
     * @return the regional settings model
     */
    RegionalSettingsModel updateRegionalSettings(RegionalSettingsKey regionalSettingsKey,
            RegionalSettingsModel regionalSettingsModel, Consumer<Long> versionChecker);
}
