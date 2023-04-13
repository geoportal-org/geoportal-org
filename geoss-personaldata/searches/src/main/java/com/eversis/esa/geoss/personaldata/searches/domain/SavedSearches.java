package com.eversis.esa.geoss.personaldata.searches.domain;

import com.eversis.esa.geoss.personaldata.common.constraints.URI;
import com.eversis.esa.geoss.personaldata.common.domain.AuditableEmbeddable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
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
 * The type Saved searches.
 */
@Data
@Audited
@Entity
@EntityListeners(AuditingEntityListener.class)
public class SavedSearches {

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
    @Column(name = "user_", nullable = false)
    private String user;

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

    @JsonProperty(access = Access.READ_ONLY)
    @JsonUnwrapped
    @NotAudited
    @Embedded
    private AuditableEmbeddable auditable = new AuditableEmbeddable();
}
