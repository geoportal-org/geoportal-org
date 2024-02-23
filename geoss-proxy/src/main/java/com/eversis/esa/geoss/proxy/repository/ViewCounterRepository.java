package com.eversis.esa.geoss.proxy.repository;

import com.eversis.esa.geoss.proxy.document.ViewCounterDoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * The interface View counter repository.
 */
public interface ViewCounterRepository extends ElasticsearchRepository<ViewCounterDoc, String> {

}
