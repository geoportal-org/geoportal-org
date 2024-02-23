package com.eversis.esa.geoss.proxy.domain;

import jakarta.validation.constraints.Min;

import lombok.Data;

/**
 * Represents a score with a non-negative value.
 */
@Data
public class Score {

    /**
     * The non-negative value of the score.
     */
    @Min(0)
    private long value;

}
