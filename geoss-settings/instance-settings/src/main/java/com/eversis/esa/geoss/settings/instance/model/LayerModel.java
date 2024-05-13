package com.eversis.esa.geoss.settings.instance.model;

import com.eversis.esa.geoss.common.constraints.URI;
import com.eversis.esa.geoss.settings.common.model.AuditableModel;
import com.eversis.esa.geoss.settings.common.model.VersionedModel;

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

/**
 * The type Layer model.
 */
@Data
@Relation(collectionRelation = "layers", itemRelation = "layer")
public class LayerModel {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @JsonIgnore
    private VersionedModel versioned;

    @NotNull
    @NotEmpty
    @NotBlank
    private String name;

    @URI
    @NotNull
    @NotEmpty
    private String url;

    @Schema(defaultValue = "false")
    private boolean visible;

    @NotNull
    private Long siteId;

    @JsonProperty(access = Access.READ_ONLY)
    @JsonUnwrapped
    private AuditableModel auditable = new AuditableModel();
}
