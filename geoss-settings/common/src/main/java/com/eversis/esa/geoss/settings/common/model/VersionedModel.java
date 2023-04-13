package com.eversis.esa.geoss.settings.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * The type Versioned model.
 */
@Data
public class VersionedModel {

    @JsonIgnore
    private Long version;
}
