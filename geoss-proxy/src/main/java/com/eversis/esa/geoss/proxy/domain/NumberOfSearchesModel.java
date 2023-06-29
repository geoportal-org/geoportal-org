package com.eversis.esa.geoss.proxy.domain;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    String interval;

}
