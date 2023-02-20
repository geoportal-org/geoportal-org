package com.eversis.esa.geoss.settings.instance.domain;

import com.eversis.esa.geoss.settings.common.constraints.AvailableLocale;
import com.eversis.esa.geoss.settings.common.domain.AuditableEntity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MapKeyColumn;
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
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Audited
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Tag extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotNull
    @NotEmpty
    @NotBlank
    @Column(nullable = false)
    private String name;

    @Schema(defaultValue = "false")
    @Column(nullable = false)
    private boolean show;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
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
}
