package com.eversis.esa.geoss.contents.repository;

import com.eversis.esa.geoss.contents.domain.Page;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * The interface Page repository.
 */
@RepositoryRestResource(collectionResourceRel = "page", path = "page")
@Tag(name = "pages")
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
     * Find by title and site id org . springframework . data . domain . page.
     *
     * @param title the title
     * @param siteId the site id
     * @param pageable the pageable
     * @return the org . springframework . data . domain . page
     */
    org.springframework.data.domain.Page<Page> findByTitleAndSiteId(@Param("title") String title,
            @Param("siteId") Long siteId, Pageable pageable);

    /**
     * Find by created by org . springframework . data . domain . page.
     *
     * @param createdBy the created by
     * @param pageable the pageable
     * @return the org . springframework . data . domain . page
     */
    org.springframework.data.domain.Page<Page> findByCreatedBy(@Param("createdBy") String createdBy, Pageable pageable);

    /**
     * Find by created by and site id org . springframework . data . domain . page.
     *
     * @param createdBy the created by
     * @param siteId the site id
     * @param pageable the pageable
     * @return the org . springframework . data . domain . page
     */
    org.springframework.data.domain.Page<Page> findByCreatedByAndSiteId(@Param("createdBy") String createdBy,
            @Param("siteId") Long siteId, Pageable pageable);

    /**
     * Find by published org . springframework . data . domain . page.
     *
     * @param published the published
     * @param pageable the pageable
     * @return the org . springframework . data . domain . page
     */
    org.springframework.data.domain.Page<Page> findByPublished(@Param("published") boolean published,
            Pageable pageable);

    /**
     * Find by published and site id org . springframework . data . domain . page.
     *
     * @param published the published
     * @param siteId the site id
     * @param pageable the pageable
     * @return the org . springframework . data . domain . page
     */
    org.springframework.data.domain.Page<Page> findByPublishedAndSiteId(@Param("published") boolean published,
            @Param("siteId") Long siteId,
            Pageable pageable);

    /**
     * Delete by ids in.
     *
     * @param ids the ids
     */
    @Modifying
    @Transactional
    @Query("delete from Page p where p.id in :ids")
    void deleteByIdsIn(@Param("ids") List<Long> ids);

    /**
     * Delete by site id.
     *
     * @param siteId the site id
     */
    @Transactional
    void deleteBySiteId(@Param("siteId") Long siteId);

    /**
     * Find by site id org . springframework . data . domain . page.
     *
     * @param siteId the site id
     * @param pageable the pageable
     * @return the org . springframework . data . domain . page
     */
    org.springframework.data.domain.Page<Page> findBySiteId(@Param("siteId") Long siteId, Pageable pageable);

}
