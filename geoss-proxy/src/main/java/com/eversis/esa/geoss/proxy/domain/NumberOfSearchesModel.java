package com.eversis.esa.geoss.proxy.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;

/**
 * The type Number of searches model.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NumberOfSearchesModel {

    /**
     * The Ds sources group key.
     */
    String dsSourcesGroupKey;

    /**
     * The Period.
     */
    @NotNull
    Period period;

    /**
     * The Interval.
     */
    @NotNull
    @Schema(example = "10d")
    String interval;

    /**
     * The Results.
     */
    @NotNull
    Integer results;

}
