package com.eversis.esa.geoss.curated.extensions.repository;

import com.eversis.esa.geoss.curated.extensions.domain.EntryExtension;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

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

    /**
     * Find by code and title optional.
     *
     * @param code the code
     * @param title the title
     * @return the optional
     */
    Optional<EntryExtension> findByCodeAndTitle(String code, String title);

}
