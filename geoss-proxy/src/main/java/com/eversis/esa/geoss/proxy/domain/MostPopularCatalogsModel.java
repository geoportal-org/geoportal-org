package com.eversis.esa.geoss.proxy.domain;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

}
