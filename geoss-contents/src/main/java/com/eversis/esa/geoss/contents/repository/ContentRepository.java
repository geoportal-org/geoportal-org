package com.eversis.esa.geoss.contents.repository;

import com.eversis.esa.geoss.contents.domain.Content;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * The interface Content repository.
 */
@RepositoryRestResource(collectionResourceRel = "content", path = "content")
public interface ContentRepository extends JpaRepository<Content, Long> {

    /**
     * Find by title page.
     *
     * @param title the title
     * @param pageable the pageable
     * @return the page
     */
    Page<Content> findByTitle(@Param("title") String title, Pageable pageable);

    /**
     * Find by created by page.
     *
     * @param createdBy the created by
     * @param pageable the pageable
     * @return the page
     */
    Page<Content> findByCreatedBy(@Param("createdBy") String createdBy, Pageable pageable);

    /**
     * Find by published page.
     *
     * @param published the published
     * @param pageable the pageable
     * @return the page
     */
    Page<Content> findByPublished(@Param("published") boolean published, Pageable pageable);

}
