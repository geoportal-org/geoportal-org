package com.eversis.esa.geoss.curated.extensions.model;

import com.eversis.esa.geoss.curated.common.domain.TaskType;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * The type User extension model.
 */
@Data
public class UserExtensionModel {

    @NotNull
    private String userId;

    @NotNull
    private String entryName;

    private String description;

    @NotNull
    private TaskType taskType;

    @NotNull
    private EntryExtensionModel entryExtension;

}
