package com.eversis.esa.geoss.proxy.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * The type Most popular keywords model.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MostPopularKeywordsModel {

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
     * The Results.
     */
    @NotNull
    @Positive
    Integer results;

}
