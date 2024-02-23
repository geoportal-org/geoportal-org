package com.eversis.esa.geoss.proxy.repository;

import com.eversis.esa.geoss.proxy.document.ScoreDoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * The interface Score repository.
 */
public interface ScoreRepository extends ElasticsearchRepository<ScoreDoc, String> {

}
