package com.eversis.esa.geoss.proxy.domain;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Logged model.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoggedModel {

    /**
     * The Common properties.
     */
    @NotNull
    CommonProperties commonProperties = null;

}
