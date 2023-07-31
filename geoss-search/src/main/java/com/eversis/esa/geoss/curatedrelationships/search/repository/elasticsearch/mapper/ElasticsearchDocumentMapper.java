package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.mapper;

import org.elasticsearch.search.SearchHit;

import java.io.IOException;

public interface ElasticsearchDocumentMapper<T> {

    String mapToString(Object object) throws IOException;

    T mapToObject(SearchHit searchHit) throws IOException;

}
