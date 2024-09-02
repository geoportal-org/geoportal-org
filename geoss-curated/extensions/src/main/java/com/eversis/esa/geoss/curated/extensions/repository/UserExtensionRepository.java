package com.eversis.esa.geoss.curated.extensions.repository;

import com.eversis.esa.geoss.curated.extensions.domain.UserExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * The interface User extension repository.
 */
@RepositoryRestResource(exported = false)
public interface UserExtensionRepository extends JpaRepository<UserExtension, Long> {

    /**
     * Find by user id page.
     *
     * @param userId the user id
     * @param pageable the pageable
     * @return the page
     */
    Page<UserExtension> findByUserId(@Param("userId") String userId, Pageable pageable);

    /**
     * Find by entry name list.
     *
     * @param entryName the entry name
     * @return the list
     */
    List<UserExtension> findByEntryName(@Param("entryName") String entryName);

}
