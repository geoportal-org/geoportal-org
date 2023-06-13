package com.eversis.esa.geoss.settings.instance.domain;

import com.eversis.esa.geoss.common.domain.AuditableEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.rest.core.annotation.RestResource;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * The type Catalog.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Audited
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Catalog extends AuditableEntity {

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

    @Schema(defaultValue = "false")
    @Column(name = "default_", nullable = false)
    private boolean defaultOption;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private String dabId;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonManagedReference
    @RestResource(exported = false)
    @NotAudited
    @OneToMany(mappedBy = "catalog", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CatalogOption> subOptions;
}
