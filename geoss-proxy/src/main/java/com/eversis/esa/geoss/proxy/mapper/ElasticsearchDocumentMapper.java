package com.eversis.esa.geoss.proxy.mapper;

import com.eversis.esa.geoss.proxy.document.Doc;

import java.io.IOException;

/**
 * The interface Elasticsearch document mapper.
 *
 * @param <T> the type parameter
 */
public interface ElasticsearchDocumentMapper<T> {

    /**
     * Map to document doc.
     *
     * @param model the model
     * @return the doc
     * @throws IOException the io exception
     */
    Doc mapToDocument(T model) throws IOException;

}
