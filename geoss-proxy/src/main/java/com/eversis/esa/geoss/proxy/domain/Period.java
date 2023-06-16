package com.eversis.esa.geoss.proxy.domain;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    String min;

    /**
     * The Max.
     */
    @NotNull
    String max;

}
