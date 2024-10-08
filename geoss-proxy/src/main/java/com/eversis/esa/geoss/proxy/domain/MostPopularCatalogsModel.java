package com.eversis.esa.geoss.proxy.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * The type Most popular catalogs model.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MostPopularCatalogsModel {

    /**
     * The Period.
     */
    @NotNull
    Period period;

    /**
     * The Results.
     */
    @NotNull
    @Positive
    Integer results;

}
