package com.eversis.esa.geoss.curatedrelationships.search.repository.ckan.mapper;

import java.io.IOException;

/**
 * The interface Ckan result mapper.
 *
 * @param <T> the type parameter
 * @param <S> the type parameter
 */
public interface CkanResultMapper<T, S> {

    /**
     * Map to object t.
     *
     * @param result the result
     * @return the t
     * @throws IOException the io exception
     */
    T mapToObject(S result) throws IOException;

}
