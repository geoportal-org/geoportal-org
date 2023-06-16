package com.eversis.esa.geoss.proxy.domain;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Get number of searches model.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetNumberOfSearchesModel {

    /**
     * The Ds sources group.
     */
    DsSourcesGroup dsSourcesGroup;

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

    /**
     * The Min doc count.
     */
    @NotNull
    int minDocCount;

}
