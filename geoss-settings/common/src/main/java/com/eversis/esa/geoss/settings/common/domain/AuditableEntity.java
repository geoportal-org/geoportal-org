package com.eversis.esa.geoss.settings.common.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.envers.NotAudited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.Instant;

/**
 * The type Auditable entity.
 */
@Data
@MappedSuperclass
public abstract class AuditableEntity {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY, value = "created_by")
    @NotAudited
    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false)
    protected String createdBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY, value = "created_on")
    @NotAudited
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_on", nullable = false, updatable = false)
    protected Instant createdDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY, value = "modified_by")
    @NotAudited
    @LastModifiedBy
    @Column(name = "modified_by", nullable = false)
    protected String lastModifiedBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY, value = "modified_on")
    @NotAudited
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_on", nullable = false)
    protected Instant lastModifiedDate;
}
