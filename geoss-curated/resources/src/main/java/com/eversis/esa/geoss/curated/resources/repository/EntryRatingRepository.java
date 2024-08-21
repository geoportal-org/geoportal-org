package com.eversis.esa.geoss.curated.resources.repository;

import com.eversis.esa.geoss.curated.common.domain.DataSource;
import com.eversis.esa.geoss.curated.resources.domain.EntryRating;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * The interface Entry rating repository.
 */
@RepositoryRestResource(exported = false)
public interface EntryRatingRepository extends JpaRepository<EntryRating, Long> {

    /**
     * Find all by target id and data source list.
     *
     * @param targetId the target id
     * @param dataSource the data source
     * @return the list
     */
    List<EntryRating> findAllByTargetIdAndDataSource(String targetId, String dataSource);

    /**
     * Find by target id and data source entry rating.
     *
     * @param targetId the target id
     * @param dataSource the data source
     * @return the entry rating
     */
    EntryRating findByTargetIdAndDataSource(String targetId, String dataSource);

    /**
     * Find all by data source list.
     *
     * @param dataSourceCode the data source code
     * @return the list
     */
    List<EntryRating> findAllByDataSource(DataSource dataSourceCode);

}
