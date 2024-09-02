package com.eversis.esa.geoss.curated.resources.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * The type Stats response.
 */
@Data
@AllArgsConstructor
public class StatsResponse {

    /**
     * The Stats.
     */
    List<RateResponse> stats;

}
