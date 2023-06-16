package com.eversis.esa.geoss.proxy.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Common properties.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonProperties {

    /**
     * The Session properties.
     */
    SessionProperties sessionProperties;

    /**
     * The User agent properties.
     */
    UserAgentProperties userAgentProperties;

    /**
     * The Other properties.
     */
    OtherProperties otherProperties;

}
