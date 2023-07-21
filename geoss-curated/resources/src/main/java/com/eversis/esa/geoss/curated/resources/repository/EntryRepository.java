package com.eversis.esa.geoss.curated.resources.repository;

import java.util.Optional;

import com.eversis.esa.geoss.curated.resources.domain.Entry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * The interface Entry repository.
 */
@RepositoryRestResource(exported = false)
public interface EntryRepository extends JpaRepository<Entry, Long> {

    /**
     * Find by code optional.
     *
     * @param code the code
     * @return the optional
     */
    Optional<Entry> findByCode(String code);

}