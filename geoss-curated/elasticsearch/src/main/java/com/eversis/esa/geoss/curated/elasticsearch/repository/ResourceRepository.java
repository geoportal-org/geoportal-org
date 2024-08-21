package com.eversis.esa.geoss.curated.elasticsearch.repository;

import com.eversis.esa.geoss.curated.elasticsearch.model.ResourceEntryELK;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The interface Resource repository.
 */
@Repository
public interface ResourceRepository extends ElasticsearchRepository<ResourceEntryELK, String> {

    /**
     * Find by code optional.
     *
     * @param code the code
     * @return the optional
     */
    Optional<ResourceEntryELK> findByCode(String code);

}
