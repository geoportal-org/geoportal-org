package com.eversis.esa.geoss.curated.resources.repository;

import com.eversis.esa.geoss.curated.resources.domain.UserResource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * The interface User resource repository.
 */
@RepositoryRestResource(exported = false)
public interface UserResourceRepository extends JpaRepository<UserResource, Long> {

    /**
     * Find by user id page.
     *
     * @param userId the user id
     * @param pageable the pageable
     * @return the page
     */
    Page<UserResource> findByUserId(@Param("userId") long userId, Pageable pageable);

}
