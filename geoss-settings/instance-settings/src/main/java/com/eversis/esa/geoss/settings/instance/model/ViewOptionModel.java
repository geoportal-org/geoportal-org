package com.eversis.esa.geoss.settings.instance.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.hateoas.server.core.Relation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * The type View option model.
 */
@Data
@Relation(collectionRelation = "viewOptions", itemRelation = "viewOption")
public class ViewOptionModel {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @JsonIgnore
    private Long viewId;

    @NotNull
    @NotEmpty
    @NotBlank
    private String label;

    @NotNull
    @NotEmpty
    @NotBlank
    private String title;

    @NotNull
    @NotEmpty
    @NotBlank
    private String value;
}
