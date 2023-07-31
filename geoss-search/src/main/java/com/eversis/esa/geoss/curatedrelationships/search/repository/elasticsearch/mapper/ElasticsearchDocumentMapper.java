package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.mapper;

import org.elasticsearch.search.SearchHit;

import java.io.IOException;

/**
 * The interface Elasticsearch document mapper.
 *
 * @param <T> the type parameter
 */
public interface ElasticsearchDocumentMapper<T> {

    /**
     * Map to string string.
     *
     * @param object the object
     * @return the string
     * @throws IOException the io exception
     */
    String mapToString(Object object) throws IOException;

    /**
     * Map to object t.
     *
     * @param searchHit the search hit
     * @return the t
     * @throws IOException the io exception
     */
    T mapToObject(SearchHit searchHit) throws IOException;

}
