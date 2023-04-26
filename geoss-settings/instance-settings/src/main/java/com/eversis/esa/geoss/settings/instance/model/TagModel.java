package com.eversis.esa.geoss.settings.instance.model;

import com.eversis.esa.geoss.settings.common.model.AuditableModel;
import com.eversis.esa.geoss.settings.common.model.VersionedModel;
import com.eversis.esa.geoss.settings.instance.domain.TagPlacement;
import com.eversis.esa.geoss.settings.instance.domain.TagType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.hateoas.server.core.Relation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Locale;

/**
 * The type Tag.
 */
@Data
@Relation(collectionRelation = "tags", itemRelation = "tag")
public class TagModel {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @JsonIgnore
    private VersionedModel versioned;

    @JsonIgnore
    private Locale locale;

    @NotNull
    @NotEmpty
    @NotBlank
    private String name;

    @Schema(defaultValue = "false")
    private boolean show;

    @NotNull
    private TagType type;

    @NotNull
    private TagPlacement placement;

    @NotNull
    @NotEmpty
    @NotBlank
    private String title;

    @NotNull
    @NotEmpty
    @NotBlank
    private String description;

    @JsonProperty(access = Access.READ_ONLY)
    @JsonUnwrapped
    private AuditableModel auditable = new AuditableModel();
}
