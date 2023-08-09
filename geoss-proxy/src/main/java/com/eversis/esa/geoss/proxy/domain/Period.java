package com.eversis.esa.geoss.proxy.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;

/**
 * The type Period.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Period {

    /**
     * The Min.
     */
    @NotNull
    long min;

    /**
     * The Max.
     */
    @NotNull
    long max;

}
