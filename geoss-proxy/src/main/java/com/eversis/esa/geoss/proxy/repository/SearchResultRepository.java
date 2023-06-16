package com.eversis.esa.geoss.proxy.repository;

import com.eversis.esa.geoss.proxy.document.SearchResultDoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * The interface Search result repository.
 */
public interface SearchResultRepository extends ElasticsearchRepository<SearchResultDoc, String> {

}
