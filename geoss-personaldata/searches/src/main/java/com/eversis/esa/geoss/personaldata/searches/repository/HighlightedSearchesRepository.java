package com.eversis.esa.geoss.personaldata.searches.repository;

import com.eversis.esa.geoss.personaldata.searches.domain.HighlightedSearches;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * The interface Highlighted searches repository.
 */
@RepositoryRestResource(path = "highlighted-searches", collectionResourceRel = "highlightedSearches",
                        itemResourceRel = "highlightedSearches")
@Tag(name = "highlighted-searches")
public interface HighlightedSearchesRepository extends JpaRepository<HighlightedSearches, Long> {

}
