package com.eversis.esa.geoss.contents.domain;

import java.util.Locale;
import java.util.Map;

import com.eversis.esa.geoss.contents.validation.AvailableLocale;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MapKeyColumn;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import io.swagger.v3.core.util.AnnotationsUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * The type Page.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Audited
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Page extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Schema(ref = AnnotationsUtils.COMPONENTS_REF + "PageLocalizedTitle")
    @Valid
    @NotNull(message = "{validation.notNull}")
    @Size(min = 1, max = 255, message = "{validation.title}")
    @ElementCollection
    @CollectionTable(name = "page_title")
    @MapKeyColumn(name = "locale")
    @Column(name = "title", nullable = false)
    private Map<@AvailableLocale Locale, String> title;

    @Schema(ref = AnnotationsUtils.COMPONENTS_REF + "PageLocalizedDescription")
    @Valid
    @NotNull(message = "{validation.notNull}")
    @Size(min = 1, max = 2048, message = "{validation.description}")
    @ElementCollection
    @CollectionTable(name = "page_description")
    @MapKeyColumn(name = "locale")
    @Column(name = "description", nullable = false)
    private Map<@AvailableLocale Locale, String> description;

    @NotNull(message = "{validation.notNull}")
    @Min(value = 0, message = "{validation.equalToOrGreaterThanZero}")
    @Column(nullable = false)
    private Long contentId;

    @NotNull(message = "{validation.notNull}")
    @Size(min = 1, max = 255, message = "{validation.slug}")
    @Column(nullable = false)
    private String slug;

    @Column(nullable = false)
    private boolean published;

}
