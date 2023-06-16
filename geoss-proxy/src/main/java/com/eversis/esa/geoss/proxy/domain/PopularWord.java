package com.eversis.esa.geoss.proxy.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Popular.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PopularWord {

    /**
     * The Key.
     */
    String key;

    /**
     * The Doc count.
     */
    int docCount;

}
