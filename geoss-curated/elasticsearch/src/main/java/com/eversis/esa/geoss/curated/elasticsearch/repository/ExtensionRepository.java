package com.eversis.esa.geoss.curated.elasticsearch.repository;

import java.util.Optional;

import com.eversis.esa.geoss.curated.elasticsearch.model.EntryExtensionELK;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Extension repository.
 */
@Repository
public interface ExtensionRepository extends ElasticsearchRepository<EntryExtensionELK, String> {

    /**
     * Find by entry code optional.
     *
     * @param code the code
     * @return the optional
     */
    Optional<EntryExtensionELK> findByEntryCode(String code);

}
