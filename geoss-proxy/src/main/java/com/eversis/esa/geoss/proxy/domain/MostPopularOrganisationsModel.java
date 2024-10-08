package com.eversis.esa.geoss.proxy.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * The type Most popular organisations model.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MostPopularOrganisationsModel {

    /**
     * The Period.
     */
    @NotNull
    Period period;

    /**
     * The Size.
     */
    @NotNull
    Integer size;

    /**
     * The Results.
     */
    @NotNull
    @Positive
    Integer results;

}
