package com.eversis.esa.geoss.personaldata.searches.repository;

import com.eversis.esa.geoss.personaldata.searches.domain.SavedSearches;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Optional;

/**
 * The interface Saved searches repository.
 */
@RepositoryRestResource(path = "saved-searches", collectionResourceRel = "savedSearches",
                        itemResourceRel = "savedSearches")
@Tag(name = "saved-searches")
public interface SavedSearchesRepository extends JpaRepository<SavedSearches, Long> {

    @PostAuthorize("returnObject.isEmpty() or returnObject.orElse(null)?.user == authentication.name"
            + " or hasAnyRole('SAVED_SEARCHES_READER', 'SAVED_SEARCHES_REMOVER', 'ADMIN')")
    @Override
    Optional<SavedSearches> findById(Long id);

    /**
     * Find by current user page.
     *
     * @param pageable the pageable
     * @return the page
     */
    @RestResource(path = "current")
    @Query("select s from SavedSearches s where s.user = ?#{authentication.name}")
    Page<SavedSearches> findByCurrentUser(Pageable pageable);

    /**
     * Find by user page.
     *
     * @param user the user
     * @param pageable the pageable
     * @return the page
     */
    @PreAuthorize("#user == authentication.name or hasAnyRole('SAVED_SEARCHES_READER', 'ADMIN')")
    @RestResource(path = "byUser")
    Page<SavedSearches> findByUser(String user, Pageable pageable);
}
