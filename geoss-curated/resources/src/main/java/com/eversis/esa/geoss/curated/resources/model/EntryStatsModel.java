package com.eversis.esa.geoss.curated.resources.model;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

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
