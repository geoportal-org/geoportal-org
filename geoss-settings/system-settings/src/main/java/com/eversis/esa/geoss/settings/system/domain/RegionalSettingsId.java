package com.eversis.esa.geoss.settings.system.domain;

import com.eversis.esa.geoss.settings.system.support.RegionalSettingsKeyAttributeConverter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import java.io.Serializable;

/**
 * The type Regional settings id.
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegionalSettingsId implements Serializable {

    @Column(name = "id")
    @Convert(converter = RegionalSettingsKeyAttributeConverter.class)
    private RegionalSettingsKey id;
}
