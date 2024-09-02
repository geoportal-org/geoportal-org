package com.eversis.esa.geoss.curated.resources.model;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * The type Entry stats model.
 */
@Data
public class EntryStatsModel {

    @NotNull
    private String targetId;

    private Integer totalEntries;

    private Double totalScore;

    private Double averageScore;

    @NotNull
    private String dataSource;

}
