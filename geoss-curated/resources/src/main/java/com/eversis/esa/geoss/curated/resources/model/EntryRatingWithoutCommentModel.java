package com.eversis.esa.geoss.curated.resources.model;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * The type Entry rating without comment model.
 */
@Data
public class EntryRatingWithoutCommentModel {

    @NotNull
    private String name;

    @NotNull
    private String targetId;

    @NotNull
    private Double score;

    @NotNull
    private String dataSource;

}
