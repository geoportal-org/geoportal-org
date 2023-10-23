package com.eversis.esa.geoss.curated.relations.model;

import com.eversis.esa.geoss.curated.common.domain.TaskType;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

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
