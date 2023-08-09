package com.eversis.esa.geoss.proxy.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;

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
