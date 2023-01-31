package com.eversis.esa.geoss.settings.system.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

/**
 * The type Portal setup wizard.
 */
@Data
@Audited
@Entity
@EntityListeners(AuditingEntityListener.class)
public class PortalSetupWizard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotNull
    @NotEmpty
    @NotBlank
    @Column(nullable = false)
    private String domain;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @NotAudited
    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false)
    private String createdBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @NotAudited
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_on", nullable = false, updatable = false)
    private Instant createdDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @NotAudited
    @LastModifiedBy
    @Column(name = "modified_by", nullable = false)
    private String modifiedBy;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @NotAudited
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_on", nullable = false)
    private Instant modifiedDate;
}
