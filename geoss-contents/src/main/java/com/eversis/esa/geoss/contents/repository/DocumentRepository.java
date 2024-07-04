package com.eversis.esa.geoss.contents.repository;

import java.util.List;

import com.eversis.esa.geoss.contents.domain.Document;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;

/**
 * The interface Document repository.
 */
@RepositoryRestResource(collectionResourceRel = "document", path = "document")
@Tag(name = "documents")
public interface DocumentRepository extends JpaRepository<Document, Long> {

    /**
     * Find by title page.
     *
     * @param title the title
     * @param pageable the pageable
     * @return the page
     */
    Page<Document> findByTitle(@Param("title") String title, Pageable pageable);

    /**
     * Find by title and site id.
     *
     * @param title the title
     * @param siteId the site id
     * @param pageable the pageable
     * @return the page
     */
    Page<Document> findByTitleAndSiteId(@Param("title") String title, @Param("siteId") Long siteId, Pageable pageable);

    /**
     * Find by created by page.
     *
     * @param createdBy the created by
     * @param pageable the pageable
     * @return the page
     */
    Page<Document> findByCreatedBy(@Param("createdBy") String createdBy, Pageable pageable);

    /**
     * Find by created by and title page.
     *
     * @param createdBy the created by
     * @param siteId the site id
     * @param pageable the pageable
     * @return the page
     */
    Page<Document> findByCreatedByAndTitle(@Param("createdBy") String createdBy, @Param("siteId") Long siteId,
            Pageable pageable);

    /**
     * Find by file name page.
     *
     * @param fileName the file name
     * @param pageable the pageable
     * @return the page
     */
    Page<Document> findByFileName(@Param("fileName") String fileName, Pageable pageable);

    /**
     * Find by file name and title page.
     *
     * @param fileName the file name
     * @param siteId the site id
     * @param pageable the pageable
     * @return the page
     */
    Page<Document> findByFileNameAndTitle(@Param("fileName") String fileName, @Param("siteId") Long siteId,
            Pageable pageable);

    /**
     * Find by folder id page.
     *
     * @param folderId the folder id
     * @param pageable the pageable
     * @return the page
     */
    Page<Document> findByFolderId(@Param("folderId") Long folderId, Pageable pageable);

    /**
     * Find by folder id and title page.
     *
     * @param folderId the folder id
     * @param siteId the site id
     * @param pageable the pageable
     * @return the page
     */
    Page<Document> findByFolderIdAndTitle(@Param("folderId") Long folderId, @Param("siteId") Long siteId,
            Pageable pageable);

    /**
     * Delete by site id.
     *
     * @param siteId the site id
     */
    @Transactional
    @DeleteMapping("/document/deleteBySiteId")
    void deleteBySiteId(@Param("siteId") Long siteId);

    /**
     * Find by site id list.
     *
     * @param siteId the site id
     * @return the list
     */
    List<Document> findBySiteId(@Param("siteId") Long siteId);

}
