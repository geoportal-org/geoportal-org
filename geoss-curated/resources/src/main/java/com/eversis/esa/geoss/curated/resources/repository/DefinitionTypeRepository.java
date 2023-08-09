package com.eversis.esa.geoss.curated.resources.repository;

import com.eversis.esa.geoss.curated.resources.domain.DefinitionType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

/**
 * The interface Definition type repository.
 */
@RepositoryRestResource(exported = false)
public interface DefinitionTypeRepository extends JpaRepository<DefinitionType, Long> {

    /**
     * Find by code optional.
     *
     * @param code the code
     * @return the optional
     */
    Optional<DefinitionType> findByCode(int code);

}
