package com.eversis.esa.geoss.proxy.repository;

import com.eversis.esa.geoss.proxy.document.ElementClickDoc;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * The interface Element click repository.
 */
public interface ElementClickRepository extends ElasticsearchRepository<ElementClickDoc, String> {

}
