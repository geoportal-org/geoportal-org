package com.eversis.esa.geoss.personaldata.searches.domain;

import com.eversis.esa.geoss.personaldata.common.constraints.URI;
import com.eversis.esa.geoss.personaldata.common.domain.AuditableEmbeddable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.annotations.NaturalId;
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
 * The type Highlighted searches.
 */
@Data
@Audited
@Entity
@EntityListeners(AuditingEntityListener.class)
public class HighlightedSearches {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = Access.READ_ONLY)
    private Long id;

    @JsonIgnore
    @Version
    @Column
    private Long version;

    @NaturalId
    @Column(nullable = false, unique = true)
    private String name;

    @NotNull
    @NotEmpty
    @NotBlank
    @Column(nullable = false)
    private String phrase;

    @URI(absolute = false)
    @NotNull
    @NotEmpty
    @NotBlank
    @Column(nullable = false)
    private String url;

    @Schema(defaultValue = "false")
    @Column(nullable = false)
    private boolean enabled;

    @Schema(defaultValue = "false")
    @Column(name = "default_", nullable = false)
    private boolean defaultSearch;

    @JsonProperty(access = Access.READ_ONLY)
    @JsonUnwrapped
    @NotAudited
    @Embedded
    private AuditableEmbeddable auditable = new AuditableEmbeddable();
}
