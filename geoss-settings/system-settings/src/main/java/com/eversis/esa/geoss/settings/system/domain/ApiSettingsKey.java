package com.eversis.esa.geoss.settings.system.domain;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * The interface Api settings key.
 */
@Schema(enumAsRef = true, implementation = String.class)
public interface ApiSettingsKey {

    /**
     * Key string.
     *
     * @return the string
     */
    String key();
}
