package com.eversis.esa.geoss.proxy.domain;

import lombok.Data;

import jakarta.validation.constraints.Min;

/**
 * Represents a view counter with a non-negative value.
 */
@Data
public class ViewCounter {

    private String entryId;

    /**
     * The non-negative value of the view counter.
     */
    @Min(0)
    private long counter;

}
