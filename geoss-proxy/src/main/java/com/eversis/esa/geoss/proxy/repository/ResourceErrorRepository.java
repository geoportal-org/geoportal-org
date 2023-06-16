package com.eversis.esa.geoss.proxy.repository;

import com.eversis.esa.geoss.proxy.document.ResourceErrorDoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * The interface Resource error repository.
 */
public interface ResourceErrorRepository extends ElasticsearchRepository<ResourceErrorDoc, String> {

}
