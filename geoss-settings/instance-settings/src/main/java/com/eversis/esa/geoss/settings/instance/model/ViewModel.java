package com.eversis.esa.geoss.settings.instance.model;

import com.eversis.esa.geoss.settings.common.model.AuditableModel;
import com.eversis.esa.geoss.settings.common.model.VersionedModel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.hateoas.server.core.Relation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * The type View model.
 */
@Data
@Relation(collectionRelation = "views", itemRelation = "view")
public class ViewModel {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @JsonIgnore
    private VersionedModel versioned;

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

    @Schema(defaultValue = "false")
    private boolean defaultOption;

    @NotNull
    private Long siteId;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonManagedReference
    private List<ViewOptionModel> subOptions;

    @JsonProperty(access = Access.READ_ONLY)
    @JsonUnwrapped
    private AuditableModel auditable = new AuditableModel();
}
