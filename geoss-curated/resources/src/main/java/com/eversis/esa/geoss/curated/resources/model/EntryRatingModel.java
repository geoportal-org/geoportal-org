package com.eversis.esa.geoss.curated.resources.model;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * The type Entry rating model.
 */
@Data
public class EntryRatingModel {

    @NotNull
    private String targetId;

    @NotNull
    private String name;

    private String userId;

    private Long groupId;

    @NotNull
    private Double score;

    private String comment;

    private String entryXml;

    @NotNull
    private String dataSource;

    private String sourceBaseUrl;

    private Integer valid;

}
