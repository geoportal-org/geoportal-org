package com.eversis.esa.geoss.settings.common.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.Instant;

/**
 * The type Auditable model.
 */
@Data
public class AuditableModel {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY, value = "createdBy")
    protected String createdBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY, value = "createdOn")
    protected Instant createdDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY, value = "modifiedBy")
    protected String lastModifiedBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY, value = "modifiedOn")
    protected Instant lastModifiedDate;
}
