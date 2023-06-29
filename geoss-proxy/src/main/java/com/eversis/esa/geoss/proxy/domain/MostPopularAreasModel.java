package com.eversis.esa.geoss.proxy.domain;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Most popular areas model.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MostPopularAreasModel {

    /**
     * The Ds sources group key.
     */
    String dsSourcesGroupKey;

    /**
     * The Period.
     */
    @NotNull
    Period period;

}
