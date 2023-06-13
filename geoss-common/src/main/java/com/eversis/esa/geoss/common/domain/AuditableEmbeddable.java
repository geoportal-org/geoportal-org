package com.eversis.esa.geoss.common.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.envers.NotAudited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.Instant;

/**
 * The type Auditable entity.
 */
@Data
@Embeddable
public class AuditableEmbeddable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY, value = "createdBy")
    @NotAudited
    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false)
    protected String createdBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY, value = "createdOn")
    @NotAudited
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_on", nullable = false, updatable = false)
    protected Instant createdDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY, value = "modifiedBy")
    @NotAudited
    @LastModifiedBy
    @Column(name = "modified_by", nullable = false)
    protected String lastModifiedBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY, value = "modifiedOn")
    @NotAudited
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_on", nullable = false)
    protected Instant lastModifiedDate;
}
