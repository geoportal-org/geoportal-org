package com.eversis.esa.geoss.settings.instance.domain;

import com.eversis.esa.geoss.common.constraints.AvailableLocale;
import com.eversis.esa.geoss.common.domain.AuditableEmbeddable;
import com.eversis.esa.geoss.common.domain.AuditableEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Version;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Locale;
import java.util.Map;

/**
 * The type Tag.
 */
@Data
@Audited
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Tag {

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

    @Schema(defaultValue = "false")
    @Column(nullable = false, name = "show_")
    private boolean show;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "type_")
    private TagType type;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TagPlacement placement;

    @Schema(ref = AnnotationsUtils.COMPONENTS_REF + "TagLocalizedTitle")
    @Valid
    @NotNull
    @ElementCollection
    @CollectionTable(name = "tag_title")
    @MapKeyColumn(name = "locale")
    @Column(name = "title")
    private Map<@AvailableLocale Locale, String> title;

    @Schema(ref = AnnotationsUtils.COMPONENTS_REF + "TagLocalizedDescription")
    @Valid
    @NotNull
    @ElementCollection
    @CollectionTable(name = "tag_description")
    @MapKeyColumn(name = "locale")
    @Column(name = "description")
    private Map<@AvailableLocale Locale, String> description;

    @JsonProperty(access = Access.READ_ONLY)
    @JsonUnwrapped
    @NotAudited
    @Embedded
    private AuditableEmbeddable auditable = new AuditableEmbeddable();
}
