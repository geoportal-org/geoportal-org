package com.eversis.esa.geoss.proxy.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Search.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Search {

    /**
     * The Key.
     */
    long key;

    /**
     * The Key as string.
     */
    String keyAsString;

    /**
     * The Doc count.
     */
    int docCount;

}
