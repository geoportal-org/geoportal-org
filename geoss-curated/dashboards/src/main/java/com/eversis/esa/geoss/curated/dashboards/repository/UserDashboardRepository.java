package com.eversis.esa.geoss.curated.dashboards.repository;

import com.eversis.esa.geoss.curated.dashboards.domain.UserDashboard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * The interface User dashboard repository.
 */
@RepositoryRestResource(exported = false)
public interface UserDashboardRepository extends JpaRepository<UserDashboard, Long> {

    /**
     * Find by user id page.
     *
     * @param userId the user id
     * @param pageable the pageable
     * @return the page
     */
    Page<UserDashboard> findByUserId(@Param("userId") String userId, Pageable pageable);

}
