package com.eversis.esa.geoss.curated.resources.repository;

import com.eversis.esa.geoss.curated.resources.domain.Source;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

/**
 * The interface Source repository.
 */
@RepositoryRestResource(exported = false)
public interface SourceRepository extends JpaRepository<Source, Long> {

    /**
     * Find by code optional.
     *
     * @param sourceId the source id
     * @return the optional
     */
    Optional<Source> findByCode(String sourceId);

    /**
     * Find by is custom order by term list.
     *
     * @param isCustom the is custom
     * @return the list
     */
    List<Source> findByIsCustomOrderByTerm(int isCustom);

}
