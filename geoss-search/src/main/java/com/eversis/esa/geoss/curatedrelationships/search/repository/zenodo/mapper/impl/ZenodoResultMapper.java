package com.eversis.esa.geoss.curatedrelationships.search.repository.zenodo.mapper.impl;

import java.io.IOException;

/**
 * The interface Zenodo result mapper.
 *
 * @param <T> the type parameter
 * @param <S> the type parameter
 */
public interface ZenodoResultMapper<T, S> {

    /**
     * Map to object t.
     *
     * @param result the result
     * @return the t
     * @throws IOException the io exception
     */
    T mapToObject(S result) throws IOException;

}
