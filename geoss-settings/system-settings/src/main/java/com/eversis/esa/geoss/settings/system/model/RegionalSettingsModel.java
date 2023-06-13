package com.eversis.esa.geoss.settings.system.model;

import com.eversis.esa.geoss.common.constraints.AvailableLocale;
import com.eversis.esa.geoss.settings.common.constraints.AvailableLanguage;
import com.eversis.esa.geoss.settings.common.model.AuditableModel;
import com.eversis.esa.geoss.settings.common.model.VersionedModel;
import com.eversis.esa.geoss.settings.system.domain.RegionalSettingsKey;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

/**
 * The type Regional settings.
 */
@Data
public class RegionalSettingsModel {

    @JsonIgnore
    private RegionalSettingsKey id;

    @JsonIgnore
    private VersionedModel versioned;

    @AvailableLanguage
    @Schema(nullable = true, description = "Default language", example = "en")
    private String language;

    @ArraySchema(arraySchema = @Schema(description = "Current languages",
                                       example = "[\"en\", \"es\", \"fr\", \"pl\", \"ru\", \"zh\"]"))
    @NotNull
    @Valid
    private Set<@AvailableLanguage String> languages;

    @Schema(implementation = String.class, nullable = true, example = "en_GB")
    @AvailableLocale(required = false)
    private Locale locale;

    @Schema(implementation = String.class, nullable = true, example = "Europe/Rome")
    private TimeZone timeZone;

    @JsonProperty(access = Access.READ_ONLY)
    @JsonUnwrapped
    private AuditableModel auditable = new AuditableModel();
}
