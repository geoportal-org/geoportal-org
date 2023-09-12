package com.eversis.esa.geoss.curated.resources.model;

import com.eversis.esa.geoss.curated.common.domain.TaskType;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * The type User resource model.
 */
@Data
public class UserResourceModel {

    @NotNull
    private String userId;

    @NotNull
    private String entryName;

    @NotNull
    private TaskType taskType;

    @NotNull
    private EntryModel entry;

}
