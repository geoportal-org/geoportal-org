package com.eversis.esa.geoss.curated.resources.model;

import jakarta.validation.constraints.NotNull;

import com.eversis.esa.geoss.curated.resources.domain.TaskType;
import lombok.Data;

/**
 * The type User resource model.
 */
@Data
public class UserResourceModel {

    @NotNull
    private Long userId;

    @NotNull
    private String entryName;

    @NotNull
    private TaskType taskType;

    @NotNull
    private EntryModel entry;

}
