package com.eversis.esa.geoss.proxy.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Session properties.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionProperties {

    /**
     * The Session login.
     */
    String sessionLogin;

    /**
     * The Session id.
     */
    String sessionId;

    /**
     * The Session timestamp.
     */
    long sessionTimestamp;

    /**
     * The Session date.
     */
    String sessionDate;

}
