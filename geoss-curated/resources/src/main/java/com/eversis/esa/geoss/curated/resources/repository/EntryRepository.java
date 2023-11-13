package com.eversis.esa.geoss.curated.resources.repository;

import com.eversis.esa.geoss.curated.common.domain.DataSource;
import com.eversis.esa.geoss.curated.common.domain.Type;
import com.eversis.esa.geoss.curated.resources.domain.Entry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

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

    /**
     * Find all by type list.
     *
     * @param type the type
     * @return the list
     */
    List<Entry> findAllByType(Type type);

    /**
     * Find all by data source list.
     *
     * @param dataSourceCode the data source code
     * @return the list
     */
    List<Entry> findAllByDataSource(DataSource dataSourceCode);

}
