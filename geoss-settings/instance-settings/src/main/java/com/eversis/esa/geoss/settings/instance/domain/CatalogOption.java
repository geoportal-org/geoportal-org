package com.eversis.esa.geoss.settings.instance.domain;

import com.eversis.esa.geoss.common.domain.AuditableEntity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.rest.core.annotation.RestResource;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * The type Catalog option.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Audited
@Entity
@EntityListeners(AuditingEntityListener.class)
public class CatalogOption extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @JsonIgnore
    @Version
    @Column
    private Long version;

    @Hidden
    @JsonBackReference
    @RestResource(exported = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "catalog_id", nullable = false)
    private Catalog catalog;

    @NotNull
    @NotEmpty
    @NotBlank
    @Column(nullable = false)
    private String label;

    @NotNull
    @NotEmpty
    @NotBlank
    @Column(nullable = false)
    private String title;

    @NotNull
    @NotEmpty
    @NotBlank
    @Column(name = "value_", nullable = false)
    private String value;
}
