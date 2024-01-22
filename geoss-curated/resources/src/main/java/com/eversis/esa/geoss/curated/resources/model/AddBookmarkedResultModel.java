package com.eversis.esa.geoss.curated.resources.model;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

/**
 * The type Add bookmarked result model.
 */
@Data
public class AddBookmarkedResultModel {

    @NotNull
    private String name;

    @NotNull
    private String targetId;

    @NotNull
    private String userId;

    @NotNull
    private String currMap;

    @NotNull
    private String dataSource;

}
