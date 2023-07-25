package com.eversis.esa.geoss.curated.resources.elasticsearch.repository;

import com.eversis.esa.geoss.curated.resources.elasticsearch.model.ResourceEntryELK;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * The interface Resource repository.
 */
@RepositoryRestResource(exported = false)
public interface ResourceRepository extends ElasticsearchRepository<ResourceEntryELK, String> {

}
