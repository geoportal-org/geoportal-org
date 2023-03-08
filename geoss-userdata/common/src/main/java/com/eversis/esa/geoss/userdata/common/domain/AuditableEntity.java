package com.eversis.esa.geoss.userdata.common.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.NotAudited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Auditable;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import java.time.Instant;
import java.util.Optional;

/**
 * The type Auditable entity.
 */
@Setter
@ToString
@MappedSuperclass
public abstract class AuditableEntity implements Auditable<String, Long, Instant> {

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

    @JsonIgnore
    @Transient
    @Override
    public boolean isNew() {
        return null == getId();
    }

    @Override
    public Optional<String> getCreatedBy() {
        return Optional.ofNullable(createdBy);
    }

    @Override
    public Optional<Instant> getCreatedDate() {
        return null == createdDate ? Optional.empty() : Optional.of(createdDate);
    }

    @Override
    public Optional<String> getLastModifiedBy() {
        return Optional.ofNullable(lastModifiedBy);
    }

    @Override
    public Optional<Instant> getLastModifiedDate() {
        return null == createdDate ? Optional.empty() : Optional.of(lastModifiedDate);
    }
}
