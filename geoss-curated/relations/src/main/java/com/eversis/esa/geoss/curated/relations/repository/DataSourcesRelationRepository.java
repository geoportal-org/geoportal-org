package com.eversis.esa.geoss.curated.relations.repository;

import java.util.Optional;

import com.eversis.esa.geoss.curated.relations.domain.RelationDataSources;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * The interface Data sources repository.
 */
@RepositoryRestResource(exported = false)
public interface DataSourcesRelationRepository extends JpaRepository<RelationDataSources, Long> {

    /**
     * Find by code optional.
     *
     * @param code the code
     * @return the optional
     */
    Optional<RelationDataSources> findByCode(String code);

}
