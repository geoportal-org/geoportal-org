package com.eversis.esa.geoss.personaldata.searches.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * The type Saved searches highlighted model.
 */
@Data
public class SavedSearchesHighlightedModel {

    @JsonProperty(access = Access.WRITE_ONLY)
    @NotNull
    @NotEmpty
    @NotBlank
    private String name;
}
