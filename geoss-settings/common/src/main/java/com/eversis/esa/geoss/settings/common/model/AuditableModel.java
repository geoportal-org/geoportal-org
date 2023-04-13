package com.eversis.esa.geoss.settings.common.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.Instant;

/**
 * The type Auditable model.
 */
@Data
public class AuditableModel {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY, value = "created_by")
    protected String createdBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY, value = "created_on")
    protected Instant createdDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY, value = "modified_by")
    protected String lastModifiedBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY, value = "modified_on")
    protected Instant lastModifiedDate;
}
