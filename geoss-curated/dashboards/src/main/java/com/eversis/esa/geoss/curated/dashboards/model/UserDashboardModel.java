package com.eversis.esa.geoss.curated.dashboards.model;

import com.eversis.esa.geoss.curated.common.domain.TaskType;
import com.eversis.esa.geoss.curated.resources.model.EntryModel;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * The type User dashboard model.
 */
@Data
public class UserDashboardModel {

    @NotNull
    private String userId;

    @NotNull
    private String entryName;

    @NotNull
    private TaskType taskType;

    @NotNull
    private EntryModel entry;

}
