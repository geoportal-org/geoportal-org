package com.eversis.esa.geoss.contents.repository;

import com.eversis.esa.geoss.contents.domain.Menu;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

/**
 * The interface Menu repository.
 */
@RepositoryRestResource(collectionResourceRel = "menu", path = "menu")
@Tag(name = "menus")
public interface MenuRepository extends JpaRepository<Menu, Long> {

    /**
     * Find by title page.
     *
     * @param title the title
     * @param pageable the pageable
     * @return the page
     */
    Page<Menu> findByTitle(@Param("title") String title, Pageable pageable);

    /**
     * Find by parent menu id page.
     *
     * @param parentMenuId the parent menu id
     * @param pageable the pageable
     * @return the page
     */
    Page<Menu> findByParentMenuId(@Param("parentMenuId") Long parentMenuId, Pageable pageable);

    /**
     * Find by level id page.
     *
     * @param levelId the level id
     * @param pageable the pageable
     * @return the page
     */
    Page<Menu> findByLevelId(@Param("levelId") Long levelId, Pageable pageable);

    /**
     * Delete by site id.
     *
     * @param siteId the site id
     */
    @Transactional
    void deleteBySiteId(@Param("siteId") Long siteId);

}
