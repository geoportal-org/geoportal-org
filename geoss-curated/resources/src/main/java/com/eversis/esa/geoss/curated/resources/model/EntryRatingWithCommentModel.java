package com.eversis.esa.geoss.curated.resources.model;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

/**
 * The type Entry rating with comment model.
 */
@Data
public class EntryRatingWithCommentModel {

    @NotNull
    private String name;

    @NotNull
    private String targetId;

    @NotNull
    private Double score;

    @NotNull
    private String comment;

    @NotNull
    private String dataSource;

}
