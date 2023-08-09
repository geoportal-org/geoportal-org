package com.eversis.esa.geoss.curated.resources.repository;

import com.eversis.esa.geoss.curated.resources.domain.DataSources;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

/**
 * The interface Data sources repository.
 */
@RepositoryRestResource(exported = false)
public interface DataSourcesRepository extends JpaRepository<DataSources, Long> {

    /**
     * Find by code optional.
     *
     * @param code the code
     * @return the optional
     */
    Optional<DataSources> findByCode(String code);

}
