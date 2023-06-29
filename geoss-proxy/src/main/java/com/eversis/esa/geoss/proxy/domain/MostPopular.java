package com.eversis.esa.geoss.proxy.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Most popular.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MostPopular {

    /**
     * The Key.
     */
    String key;

    /**
     * The Doc count.
     */
    long docCount;

}
