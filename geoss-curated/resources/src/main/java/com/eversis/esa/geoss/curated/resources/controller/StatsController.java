package com.eversis.esa.geoss.curated.resources.controller;

import com.eversis.esa.geoss.curated.resources.domain.EntryStats;
import com.eversis.esa.geoss.curated.resources.model.EntryStatsModel;
import com.eversis.esa.geoss.curated.resources.service.StatsService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.validation.Valid;
import java.util.List;

/**
 * The type Stats controller.
 */
@Log4j2
@RequiredArgsConstructor
@BasePathAwareController("/stats")
@ResponseBody
@Tag(name = "stats")
public class StatsController {

    private final StatsService statsService;

    /**
     * Find stats list.
     *
     * @return the list
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<EntryStats> findStatsList() {
        log.info("Find stats list");
        return statsService.findStatsList();
    }

    /**
     * Find stats entry stats.
     *
     * @param statsId the stats id
     * @return the entry stats
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{statsId}")
    public EntryStats findStats(@PathVariable long statsId) {
        log.info("Find stats");
        return statsService.findStats(statsId);
    }

    /**
     * Create stats.
     *
     * @param entryStatsDto the entry stats dto
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createStats(@RequestBody @Valid EntryStatsModel entryStatsDto) {
        log.info("Create stats");
        statsService.createStats(entryStatsDto);
    }

    /**
     * Update stats.
     *
     * @param statsId the stats id
     * @param entryStatsDto the entry stats dto
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{statsId}")
    public void updateStats(
            @PathVariable long statsId,
            @RequestBody @Valid EntryStatsModel entryStatsDto) {
        log.info("Update stats");
        statsService.updateStats(statsId, entryStatsDto);
    }

    /**
     * Delete stats.
     *
     * @param statsId the stats id
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping("/{statsId}")
    public void deleteStats(@PathVariable long statsId) {
        log.info("Delete statsId");
        statsService.deleteStats(statsId);
    }

    /**
     * Delete all stats response entity.
     *
     * @return the response entity
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAllStats() {
        log.info("Deleting all stats");
        statsService.deleteAllStats();
        return ResponseEntity.noContent().build();
    }
}
