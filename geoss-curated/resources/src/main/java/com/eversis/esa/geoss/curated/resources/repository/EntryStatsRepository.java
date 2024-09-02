package com.eversis.esa.geoss.curated.resources.repository;

import com.eversis.esa.geoss.curated.common.domain.DataSource;
import com.eversis.esa.geoss.curated.resources.domain.EntryStats;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * The interface Entry stats repository.
 */
@RepositoryRestResource(exported = false)
public interface EntryStatsRepository extends JpaRepository<EntryStats, Long> {

    /**
     * Find by target id and data source entry stats.
     *
     * @param targetId the target id
     * @param dataSource the data source
     * @return the entry stats
     */
    EntryStats findByTargetIdAndDataSource(String targetId, String dataSource);

    /**
     * Find all by data source list.
     *
     * @param dataSourceCode the data source code
     * @return the list
     */
    List<EntryStats> findAllByDataSource(DataSource dataSourceCode);

}
