package com.eversis.esa.geoss.curated.relations.model;

import jakarta.validation.constraints.NotNull;

import com.eversis.esa.geoss.curated.relations.domain.TaskType;
import lombok.Data;

/**
 * The type User relation model.
 */
@Data
public class UserRelationModel {

    @NotNull
    private String userId;

    @NotNull
    private String entryName;

    @NotNull
    private TaskType taskType;

    @NotNull
    private EntryRelationModel entryRelation;

}
