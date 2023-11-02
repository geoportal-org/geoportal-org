package com.eversis.esa.geoss.personaldata.run.repository;

import com.eversis.esa.geoss.personaldata.run.domain.SavedRun;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * The interface Run repository.
 */
@RepositoryRestResource(path = "saved-runs", collectionResourceRel = "savedRuns", itemResourceRel = "savedRun")
@Tag(name = "saved-runs")
public interface SavedRunRepository extends JpaRepository<SavedRun, Long> {

    /**
     * Find by current user page.
     *
     * @param pageable the pageable
     * @return the page
     */
    @Operation(
            description = "Get current logged user runs.",
            summary = "Get current logged user runs.")
    @RestResource(path = "current")
    @Query("select c from SavedRun c where c.user = ?#{authentication.name}")
    Page<SavedRun> findByCurrentUser(Pageable pageable);

    /**
     * Find by user page.
     *
     * @param user the user
     * @param pageable the pageable
     * @return the page
     */
    @Operation(
            description = "Get user runs.",
            summary = "Get user runs.")
    @RestResource(path = "byUser")
    Page<SavedRun> findByUser(String user, Pageable pageable);
}
