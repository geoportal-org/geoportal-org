package com.eversis.esa.geoss.personaldata.searches.event;

import com.eversis.esa.geoss.personaldata.searches.domain.SavedSearches;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * The type Saved searches repository event.
 */
@Log4j2
@RequiredArgsConstructor
@Component
@RepositoryEventHandler
public class SavedSearchesRepositoryEvent {

    /**
     * Handle before create.
     *
     * @param savedSearches the saved searches
     */
    @HandleBeforeCreate
    void handleBeforeCreate(final SavedSearches savedSearches) {
        log.debug("savedSearches:{}", savedSearches);
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        savedSearches.setUser(userName);
    }

    /**
     * Handle before save.
     *
     * @param savedSearches the saved searches
     */
    @PreAuthorize("#savedSearches?.user == authentication.name")
    @HandleBeforeSave
    void handleBeforeSave(final SavedSearches savedSearches) {
        log.debug("savedSearches:{}", savedSearches);
    }

    /**
     * Handle before delete.
     *
     * @param savedSearches the saved searches
     */
    @PreAuthorize("#savedSearches?.user == authentication.name or hasAnyRole('SAVED_SEARCHES_REMOVER', 'ADMIN')")
    @HandleBeforeDelete
    void handleBeforeDelete(final SavedSearches savedSearches) {
        log.debug("savedSearches:{}", savedSearches);
    }
}
