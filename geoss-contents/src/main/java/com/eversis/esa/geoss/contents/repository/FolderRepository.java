package com.eversis.esa.geoss.contents.repository;

import com.eversis.esa.geoss.contents.domain.Folder;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

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
     * Find by title and site id.
     *
     * @param title the title
     * @param siteId the site id
     * @param pageable the pageable
     * @return the page
     */
    Page<Folder> findByTitleAndSiteId(@Param("title") String title, @Param("siteId") Long siteId, Pageable pageable);

    /**
     * Find by created by page.
     *
     * @param createdBy the created by
     * @param pageable the pageable
     * @return the page
     */
    Page<Folder> findByCreatedBy(@Param("createdBy") String createdBy, Pageable pageable);

    /**
     * Find by created by and title page.
     *
     * @param createdBy the created by
     * @param siteId the site id
     * @param pageable the pageable
     * @return the page
     */
    Page<Folder> findByCreatedByAndTitle(@Param("createdBy") String createdBy, @Param("siteId") Long siteId,
            Pageable pageable);

    /**
     * Find by parent folder id page.
     *
     * @param parentFolderId the parent folder id
     * @param pageable the pageable
     * @return the page
     */
    Page<Folder> findByParentFolderId(@Param("parentFolderId") Long parentFolderId, Pageable pageable);

    /**
     * Find by parent folder id and site id list.
     *
     * @param parentFolderId the parent folder id
     * @param siteId the site id
     * @return the list
     */
    List<Folder> findByParentFolderIdAndSiteId(@Param("parentFolderId") Long parentFolderId,
            @Param("siteId") Long siteId);

    /**
     * Delete by site id.
     *
     * @param siteId the site id
     */
    @Transactional
    @DeleteMapping("/folder/deleteBySiteId")
    void deleteBySiteId(@Param("siteId") Long siteId);

}
