package com.eversis.esa.geoss.curated.recommendations.repository;

import com.eversis.esa.geoss.curated.recommendations.domain.DataSource;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * The interface Data source repository.
 */
@RepositoryRestResource(exported = false)
public interface DataSourceRepository extends CrudRepository<DataSource, Integer> {

    /**
     * Find data source by code data source.
     *
     * @param code the code
     * @return the data source
     */
    DataSource findDataSourceByCode(String code);
}
