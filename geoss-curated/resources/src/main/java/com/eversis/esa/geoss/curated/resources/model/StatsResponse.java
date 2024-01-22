package com.eversis.esa.geoss.curated.resources.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

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
