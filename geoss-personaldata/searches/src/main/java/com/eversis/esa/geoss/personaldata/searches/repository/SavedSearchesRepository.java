package com.eversis.esa.geoss.personaldata.searches.repository;

import com.eversis.esa.geoss.personaldata.searches.domain.SavedSearches;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * The interface Saved searches repository.
 */
@RepositoryRestResource(path = "saved-searches", collectionResourceRel = "savedSearches",
                        itemResourceRel = "savedSearches")
@Tag(name = "saved-searches")
public interface SavedSearchesRepository extends JpaRepository<SavedSearches, Long> {

}
