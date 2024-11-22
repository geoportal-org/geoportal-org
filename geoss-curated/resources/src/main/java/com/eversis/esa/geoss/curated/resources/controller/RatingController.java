package com.eversis.esa.geoss.curated.resources.controller;

import com.eversis.esa.geoss.curated.resources.domain.EntryRating;
import com.eversis.esa.geoss.curated.resources.model.CommentResponse;
import com.eversis.esa.geoss.curated.resources.model.EntryRatingModel;
import com.eversis.esa.geoss.curated.resources.model.EntryRatingWithCommentModel;
import com.eversis.esa.geoss.curated.resources.model.EntryRatingWithoutCommentModel;
import com.eversis.esa.geoss.curated.resources.model.RateResponse;
import com.eversis.esa.geoss.curated.resources.model.StatsResponse;
import com.eversis.esa.geoss.curated.resources.service.RatingService;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.validation.Valid;
import java.util.List;

/**
 * The type Rating controller.
 */
@Log4j2
@RequiredArgsConstructor
@BasePathAwareController("/rating")
@ResponseBody
@Tag(name = "rating")
public class RatingController {

    private final RatingService ratingService;

    /**
     * Find ratings list.
     *
     * @return the list
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<EntryRating> findRatings() {
        log.info("Find ratings");
        return ratingService.findRatings();
    }

    /**
     * Find rating entry rating.
     *
     * @param entryId the entry id
     * @return the entry rating
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{entryId}")
    public EntryRating findRating(@PathVariable long entryId) {
        log.info("Find rating");
        return ratingService.findRating(entryId);
    }

    /**
     * Create rating.
     *
     * @param entryRatingDto the entry rating dto
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createRating(@RequestBody @Valid EntryRatingModel entryRatingDto) {
        log.info("Create rating");
        ratingService.createRating(entryRatingDto);
    }

    /**
     * Update rating.
     *
     * @param entryId the entry id
     * @param entryRatingDto the entry rating dto
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{entryId}")
    public void updateRating(
            @PathVariable long entryId,
            @RequestBody @Valid EntryRatingModel entryRatingDto) {
        log.info("Update rating");
        ratingService.updateRating(entryId, entryRatingDto);
    }

    /**
     * Delete rating.
     *
     * @param entryId the entry id
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping("/{entryId}")
    public void deleteRating(@PathVariable long entryId) {
        log.info("Delete rating");
        ratingService.deleteRating(entryId);
    }

    /**
     * Rate with comment rate response.
     *
     * @param entryRatingDto the entry rating dto
     * @return the rate response
     */
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/rate/withComment")
    public RateResponse rateWithComment(@RequestBody @Valid EntryRatingWithCommentModel entryRatingDto) {
        log.info("Rate with comment");
        return ratingService.rateWithComment(entryRatingDto);
    }

    /**
     * Rate without comment rate response.
     *
     * @param entryRatingDto the entry rating dto
     * @return the rate response
     */
    @PreAuthorize("permitAll()")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/rate/withoutComment")
    public RateResponse rateWithoutComment(@RequestBody @Valid EntryRatingWithoutCommentModel entryRatingDto) {
        log.info("Rate without comment");
        return ratingService.rateWithoutComment(entryRatingDto);
    }

    /**
     * Find ratings by target ids and data source stats response.
     *
     * @param targetIds the target ids
     * @param dataSource the data source
     * @return the stats response
     */
    @PreAuthorize("permitAll()")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/search/findRatingsByTargetIdsAndDataSource")
    public StatsResponse findRatingsByTargetIdsAndDataSource(@RequestParam String targetIds,
            @RequestParam String dataSource) {
        log.info("Find ratings by targetIds and dataSource");
        return ratingService.findRatingsByTargetIdsAndDataSource(targetIds, dataSource);
    }

    /**
     * Find comments by target id and data source list.
     *
     * @param targetId the target id
     * @param dataSource the data source
     * @return the list
     */
    @PreAuthorize("permitAll()")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/search/findCommentsByTargetIdAndDataSource")
    public List<CommentResponse> findCommentsByTargetIdAndDataSource(@RequestParam String targetId,
            @RequestParam String dataSource) {
        log.info("Find comments by targetId and dataSource");
        return ratingService.findCommentsByTargetIdAndDataSource(targetId, dataSource);
    }

    /**
     * Delete all ratings response entity.
     *
     * @return the response entity
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAllRatings() {
        ratingService.deleteAllRatings();
        return ResponseEntity.noContent().build();
    }

}
