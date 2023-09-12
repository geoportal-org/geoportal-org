package com.eversis.esa.geoss.curated.common.repository;

import com.eversis.esa.geoss.curated.common.domain.DataSource;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

/**
 * The interface Data source repository.
 */
@RepositoryRestResource(exported = false)
public interface DataSourceRepository extends JpaRepository<DataSource, Long> {

    /**
     * Find by code optional.
     *
     * @param code the code
     * @return the optional
     */
    Optional<DataSource> findByCode(String code);

}
