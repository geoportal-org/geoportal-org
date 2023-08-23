package com.eversis.esa.geoss.curated.relations.repository;

import com.eversis.esa.geoss.curated.relations.domain.UserRelation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * The interface User relation repository.
 */
@RepositoryRestResource(exported = false)
public interface UserRelationRepository extends JpaRepository<UserRelation, Long> {

    /**
     * Find by user id page.
     *
     * @param userId the user id
     * @param pageable the pageable
     * @return the page
     */
    Page<UserRelation> findByUserId(@Param("userId") String userId, Pageable pageable);

}
