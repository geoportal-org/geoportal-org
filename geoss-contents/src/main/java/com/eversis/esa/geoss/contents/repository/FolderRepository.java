package com.eversis.esa.geoss.contents.repository;

import com.eversis.esa.geoss.contents.domain.Folder;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

/**
 * The interface Folder repository.
 */
@RepositoryRestResource(collectionResourceRel = "folder", path = "folder")
@Tag(name = "folders")
public interface FolderRepository extends JpaRepository<Folder, Long> {

    /**
     * Find by title page.
     *
     * @param title the title
     * @param pageable the pageable
     * @return the page
     */
    Page<Folder> findByTitle(@Param("title") String title, Pageable pageable);

    /**
     * Find by created by page.
     *
     * @param createdBy the created by
     * @param pageable the pageable
     * @return the page
     */
    Page<Folder> findByCreatedBy(@Param("createdBy") String createdBy, Pageable pageable);

    /**
     * Find by parent folder id page.
     *
     * @param parentFolderId the parent folder id
     * @param pageable the pageable
     * @return the page
     */
    Page<Folder> findByParentFolderId(@Param("parentFolderId") Long parentFolderId, Pageable pageable);

    /**
     * Delete by site id.
     *
     * @param siteId the site id
     */
    @Transactional
    void deleteBySiteId(@Param("siteId") Long siteId);

}
