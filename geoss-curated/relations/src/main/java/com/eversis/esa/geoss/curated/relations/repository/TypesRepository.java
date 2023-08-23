package com.eversis.esa.geoss.curated.relations.repository;

import java.util.Optional;

import com.eversis.esa.geoss.curated.relations.domain.Types;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * The interface Types repository.
 */
@RepositoryRestResource(exported = false)
public interface TypesRepository extends JpaRepository<Types, Long>{

    /**
     * Find by code optional.
     *
     * @param code the code
     * @return the optional
     */
    Optional<Types> findByCode(String code);

}
