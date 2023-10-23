package com.eversis.esa.geoss.curated.relations.repository;

import com.eversis.esa.geoss.curated.relations.domain.RelationType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

/**
 * The interface Relation type repository.
 */
@RepositoryRestResource(exported = false)
public interface RelationTypeRepository extends JpaRepository<RelationType, Long> {

    /**
     * Find by code optional.
     *
     * @param code the code
     * @return the optional
     */
    Optional<RelationType> findByCode(String code);

}
