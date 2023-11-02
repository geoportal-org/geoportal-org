package com.eversis.esa.geoss.personaldata.run.event;

import com.eversis.esa.geoss.personaldata.run.domain.SavedRun;

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
 * The type Saved run repository event.
 */
@Log4j2
@RequiredArgsConstructor
@Component
@RepositoryEventHandler
public class SavedRunRepositoryEventHandler {

    /**
     * Handle before create.
     *
     * @param savedRun the saved run
     */
    @HandleBeforeCreate
    void handleBeforeCreate(final SavedRun savedRun) {
        log.debug("savedRun:{}", savedRun);
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        savedRun.setUser(userName);
    }

    /**
     * Handle before save.
     *
     * @param savedRun the saved run
     */
    @PreAuthorize("#savedSearches?.user == authentication.name")
    @HandleBeforeSave
    void handleBeforeSave(final SavedRun savedRun) {
        log.debug("savedSearches:{}", savedRun);
    }

    /**
     * Handle before delete.
     *
     * @param savedRun the saved run
     */
    @PreAuthorize("#savedSearches?.user == authentication.name or hasAnyRole('SAVED_SEARCHES_REMOVER', 'ADMIN')")
    @HandleBeforeDelete
    void handleBeforeDelete(final SavedRun savedRun) {
        log.debug("savedSearches:{}", savedRun);
    }
}
