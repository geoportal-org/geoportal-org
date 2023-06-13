package com.eversis.esa.geoss.settings.system.domain;

import com.eversis.esa.geoss.common.constraints.AvailableLocale;
import com.eversis.esa.geoss.common.domain.AuditableEmbeddable;
import com.eversis.esa.geoss.settings.common.constraints.AvailableLanguage;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.annotations.TimeZoneColumn;
import org.hibernate.annotations.TimeZoneStorage;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Version;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

/**
 * The type Regional settings.
 */
@IdClass(RegionalSettingsId.class)
@Data
@Audited
@Entity
@EntityListeners(AuditingEntityListener.class)
public class RegionalSettings {

    @JsonIgnore
    @Id
    private RegionalSettingsKey id;

    @JsonIgnore
    @Version
    @Column
    private Long version;

    @AvailableLanguage
    @Schema(nullable = true, description = "Default language", example = "en")
    @Column
    private String language;

    @ArraySchema(arraySchema = @Schema(description = "Current languages",
                                       example = "[\"en\", \"es\", \"fr\", \"pl\", \"ru\", \"zh\"]"))
    @NotNull
    @Valid
    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "language")
    private Set<@AvailableLanguage String> languages = new LinkedHashSet<>();

    @Schema(implementation = String.class, nullable = true, example = "en_GB")
    @AvailableLocale(required = false)
    @Column
    private Locale locale;

    @Schema(implementation = String.class, nullable = true, example = "Europe/Rome")
    @TimeZoneStorage
    @TimeZoneColumn
    @Column
    private TimeZone timeZone;

    @JsonProperty(access = Access.READ_ONLY)
    @JsonUnwrapped
    @NotAudited
    @Embedded
    private AuditableEmbeddable auditable = new AuditableEmbeddable();
}
