package com.eversis.esa.geoss.contents.repository;

import com.eversis.esa.geoss.contents.domain.Menu;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * The interface Menu repository.
 */
@RepositoryRestResource(collectionResourceRel = "menu", path = "menu")
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
    Page<Menu> findByParentMenuId(@Param("parentMenuId") String parentMenuId, Pageable pageable);

    /**
     * Find by level id page.
     *
     * @param levelId the level id
     * @param pageable the pageable
     * @return the page
     */
    Page<Menu> findByLevelId(@Param("levelId") String levelId, Pageable pageable);

}
