package com.eversis.esa.geoss.contents.repository;

import com.eversis.esa.geoss.contents.domain.Content;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * The interface Content repository.
 */
@RepositoryRestResource(collectionResourceRel = "content", path = "content")
@Tag(name = "contents")
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
     * Find by created by and site id page.
     *
     * @param createdBy the created by
     * @param siteId the site id
     * @param pageable the pageable
     * @return the page
     */
    Page<Content> findByCreatedByAndSiteId(@Param("createdBy") String createdBy, @Param("siteId") Long siteId,
            Pageable pageable);

    /**
     * Find by title and site id content.
     *
     * @param title the title
     * @param siteId the site id
     * @return the content
     */
    Content findByTitleAndSiteId(@Param("title") String title, @Param("siteId") Long siteId);

    /**
     * Find by published page.
     *
     * @param published the published
     * @param pageable the pageable
     * @return the page
     */
    Page<Content> findByPublished(@Param("published") boolean published, Pageable pageable);

    /**
     * Find by published and site id page.
     *
     * @param published the published
     * @param siteId the site id
     * @param pageable the pageable
     * @return the page
     */
    Page<Content> findByPublishedAndSiteId(@Param("published") boolean published, @Param("siteId") Long siteId,
            Pageable pageable);

    /**
     * Delete by ids in.
     *
     * @param ids the ids
     */
    @Modifying
    @Transactional
    @Query("delete from Content c where c.id in :ids")
    void deleteByIdsIn(@Param("ids") List<Long> ids);

    /**
     * Delete by site id.
     *
     * @param siteId the site id
     */
    @Transactional
    void deleteBySiteId(@Param("siteId") Long siteId);

}
