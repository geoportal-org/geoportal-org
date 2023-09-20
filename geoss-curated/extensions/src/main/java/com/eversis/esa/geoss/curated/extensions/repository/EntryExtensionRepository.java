package com.eversis.esa.geoss.curated.extensions.repository;

import java.util.Optional;

import com.eversis.esa.geoss.curated.extensions.domain.EntryExtension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * The interface Entry extension repository.
 */
@RepositoryRestResource(exported = false)
public interface EntryExtensionRepository extends JpaRepository<EntryExtension, Long> {

    /**
     * Find by code optional.
     *
     * @param code the code
     * @return the optional
     */
    Optional<EntryExtension> findByCode(String code);

}
