package com.eversis.esa.geoss.contents.repository;

import com.eversis.esa.geoss.contents.domain.Document;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

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
     * Find by created by page.
     *
     * @param createdBy the created by
     * @param pageable the pageable
     * @return the page
     */
    Page<Document> findByCreatedBy(@Param("createdBy") String createdBy, Pageable pageable);

    /**
     * Find by file name page.
     *
     * @param fileName the file name
     * @param pageable the pageable
     * @return the page
     */
    Page<Document> findByFileName(@Param("fileName") String fileName, Pageable pageable);

    /**
     * Find by folder id page.
     *
     * @param folderId the folder id
     * @param pageable the pageable
     * @return the page
     */
    Page<Document> findByFolderId(@Param("folderId") Long folderId, Pageable pageable);

}
