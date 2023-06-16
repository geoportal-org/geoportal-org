package com.eversis.esa.geoss.proxy.domain;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Get most popular model.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetMostPopularModel {

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
     * The Size.
     */
    @NotNull
    int size;

}
