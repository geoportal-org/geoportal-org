package com.eversis.esa.geoss.settings.system.domain;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

/**
 * The interface Web settings key.
 */
@Schema(enumAsRef = true, implementation = String.class)
public interface WebSettingsKey {

    /**
     * Key string.
     *
     * @return the string
     */
    String key();

    /**
     * Key values set.
     *
     * @return the set
     */
    Set<WebSettingsValue> keyValues();
}
