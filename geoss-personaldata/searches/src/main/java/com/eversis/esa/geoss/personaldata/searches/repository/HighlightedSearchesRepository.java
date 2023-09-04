package com.eversis.esa.geoss.personaldata.searches.repository;

import com.eversis.esa.geoss.personaldata.searches.domain.HighlightedSearches;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * The interface Highlighted searches repository.
 */
@RepositoryRestResource(path = "highlighted-searches", collectionResourceRel = "highlightedSearches",
                        itemResourceRel = "highlightedSearches")
@Tag(name = "highlighted-searches")
public interface HighlightedSearchesRepository extends JpaRepository<HighlightedSearches, Long> {

    /**
     * Find by enabled true page.
     *
     * @param pageable the pageable
     * @return the page
     */
    @Operation(
            operationId = "search-enabled-highlightedsearches-get",
            description = "Get enabled highlighted searches.",
            summary = "Get enabled highlighted searches.")
    @RestResource(path = "enabled")
    Page<HighlightedSearches> findByEnabledTrue(Pageable pageable);
}
