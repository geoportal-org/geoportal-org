package com.eversis.esa.geoss.curated.resources.model;

import lombok.Data;

/**
 * The type Rate response.
 */
@Data
public class RateResponse {

    private Double averageScore;

    private String comment;

    private Double score;

    private String targetId;

    private Integer totalEntries;

}
