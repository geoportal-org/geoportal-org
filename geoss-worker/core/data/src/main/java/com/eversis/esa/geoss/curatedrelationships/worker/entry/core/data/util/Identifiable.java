package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.util;

import java.io.Serializable;

/**
 * The interface Identifiable.
 *
 * @param <T> the type parameter
 */
public interface Identifiable<T extends Serializable> {

    /**
     * Gets id.
     *
     * @return the id
     */
    T getId();
}
