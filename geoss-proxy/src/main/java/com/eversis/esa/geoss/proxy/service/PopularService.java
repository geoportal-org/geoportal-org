package com.eversis.esa.geoss.proxy.service;

import java.util.List;

import com.eversis.esa.geoss.proxy.domain.PopularWord;

/**
 * The interface Popular service.
 */
public interface PopularService {

    /**
     * Gets popular words.
     *
     * @param query the query
     * @param limit the limit
     * @return the popular words
     */
    List<PopularWord> getPopularWords(String query, int limit);

}
