package com.eversis.esa.geoss.proxy.mapper;

import java.io.IOException;

import com.eversis.esa.geoss.proxy.document.Doc;

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
    public Doc mapToDocument(T model) throws IOException;

}
