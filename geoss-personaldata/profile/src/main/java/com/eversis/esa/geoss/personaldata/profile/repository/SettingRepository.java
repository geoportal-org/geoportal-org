package com.eversis.esa.geoss.personaldata.profile.repository;

import com.eversis.esa.geoss.personaldata.profile.domain.Setting;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * The interface Setting repository.
 */
@RepositoryRestResource(path = "settings", collectionResourceRel = "settings", itemResourceRel = "setting")
@Tag(name = "settings")
public interface SettingRepository extends JpaRepository<Setting, Long> {

    /**
     * Find by current user page.
     *
     * @param pageable the pageable
     * @return the page
     */
    @Operation(
            description = "Get current logged user settings.",
            summary = "Get current logged user settings.")
    @RestResource(path = "current")
    @Query("select c from Setting c where c.user = ?#{authentication.name}")
    Page<Setting> findByCurrentUser(Pageable pageable);

    /**
     * Find by user page.
     *
     * @param user the user
     * @param pageable the pageable
     * @return the page
     */
    @Operation(
            description = "Get user settings.",
            summary = "Get user settings.")
    @RestResource(path = "byUser")
    Page<Setting> findByUser(String user, Pageable pageable);
}
