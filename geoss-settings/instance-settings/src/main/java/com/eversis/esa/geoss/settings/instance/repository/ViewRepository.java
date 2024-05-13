package com.eversis.esa.geoss.settings.instance.repository;

import com.eversis.esa.geoss.settings.instance.domain.View;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * The interface View repository.
 */
@RepositoryRestResource(path = "views", collectionResourceRel = "views", itemResourceRel = "view")
@Tag(name = "views")
public interface ViewRepository extends JpaRepository<View, Long> {

    /**
     * Find all by site id page.
     *
     * @param siteId the site id
     * @param pageable the pageable
     * @return the page
     */
    @RestResource(exported = false)
    Page<View> findAllBySiteId(Long siteId, Pageable pageable);

    /**
     * Delete all by site id long.
     *
     * @param siteId the site id
     * @return the long
     */
    @RestResource(exported = false)
    long deleteAllBySiteId(Long siteId);
}
