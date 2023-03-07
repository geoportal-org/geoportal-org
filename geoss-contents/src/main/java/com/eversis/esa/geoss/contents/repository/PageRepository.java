package com.eversis.esa.geoss.contents.repository;

import com.eversis.esa.geoss.contents.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * The interface Page repository.
 */
@RepositoryRestResource(collectionResourceRel = "page", path = "page")
public interface PageRepository extends JpaRepository<Page, Long> {

    /**
     * Find by title org . springframework . data . domain . page.
     *
     * @param title the title
     * @param pageable the pageable
     * @return the org . springframework . data . domain . page
     */
    org.springframework.data.domain.Page<Page> findByTitle(@Param("title") String title, Pageable pageable);

    /**
     * Find by created by org . springframework . data . domain . page.
     *
     * @param createdBy the created by
     * @param pageable the pageable
     * @return the org . springframework . data . domain . page
     */
    org.springframework.data.domain.Page<Page> findByCreatedBy(@Param("createdBy") String createdBy, Pageable pageable);

    /**
     * Find by published org . springframework . data . domain . page.
     *
     * @param published the published
     * @param pageable the pageable
     * @return the org . springframework . data . domain . page
     */
    org.springframework.data.domain.Page<Page> findByPublished(@Param("published") boolean published, Pageable pageable);

}
