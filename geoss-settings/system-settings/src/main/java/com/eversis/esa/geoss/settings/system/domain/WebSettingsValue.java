package com.eversis.esa.geoss.settings.system.domain;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * The interface Web settings value.
 */
@Schema(enumAsRef = true, implementation = String.class)
public interface WebSettingsValue {

    /**
     * Value string.
     *
     * @return the string
     */
    String value();
}
