package com.eversis.esa.geoss.settings.instance.domain;

import com.eversis.esa.geoss.common.constraints.URI;
import com.eversis.esa.geoss.common.domain.AuditableEmbeddable;
import com.eversis.esa.geoss.common.domain.AuditableEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * The type Api settings.
 */
@Data
@Audited
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Layer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @JsonIgnore
    @Version
    @Column
    private Long version;

    @NotNull
    @NotEmpty
    @NotBlank
    @Column(nullable = false)
    private String name;

    @URI
    @NotNull
    @NotEmpty
    @Column(nullable = false)
    private String url;

    @Schema(defaultValue = "false")
    @Column(nullable = false)
    private boolean visible;

    @NotNull
    @Column
    private Long siteId;

    @JsonProperty(access = Access.READ_ONLY)
    @JsonUnwrapped
    @NotAudited
    @Embedded
    private AuditableEmbeddable auditable = new AuditableEmbeddable();
}
