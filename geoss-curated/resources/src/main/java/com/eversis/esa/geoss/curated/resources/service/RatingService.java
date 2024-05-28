package com.eversis.esa.geoss.curated.resources.service;

import java.util.List;

import com.eversis.esa.geoss.curated.resources.domain.EntryRating;
import com.eversis.esa.geoss.curated.resources.model.CommentResponse;
import com.eversis.esa.geoss.curated.resources.model.EntryRatingModel;
import com.eversis.esa.geoss.curated.resources.model.EntryRatingWithCommentModel;
import com.eversis.esa.geoss.curated.resources.model.EntryRatingWithoutCommentModel;
import com.eversis.esa.geoss.curated.resources.model.RateResponse;
import com.eversis.esa.geoss.curated.resources.model.StatsResponse;

/**
 * The interface Rating service.
 */
public interface RatingService {

    /**
     * Find ratings list.
     *
     * @return the list
     */
    List<EntryRating> findRatings();

    /**
     * Find rating entry rating.
     *
     * @param entryId the entry id
     * @return the entry rating
     */
    EntryRating findRating(long entryId);

    /**
     * Delete rating.
     *
     * @param entryId the entry id
     */
    void deleteRating(long entryId);

    /**
     * Create rating.
     *
     * @param entryRatingDto the entry rating dto
     */
    void createRating(EntryRatingModel entryRatingDto);

    /**
     * Update rating.
     *
     * @param entryId the entry id
     * @param entryRatingDto the entry rating dto
     */
    void updateRating(long entryId, EntryRatingModel entryRatingDto);

    /**
     * Rate with comment rate response.
     *
     * @param entryRatingDto the entry rating dto
     * @return the rate response
     */
    RateResponse rateWithComment(EntryRatingWithCommentModel entryRatingDto);

    /**
     * Rate without comment rate response.
     *
     * @param entryRatingDto the entry rating dto
     * @return the rate response
     */
    RateResponse rateWithoutComment(EntryRatingWithoutCommentModel entryRatingDto);

    /**
     * Find ratings by target ids and data source stats response.
     *
     * @param targetIds the target ids
     * @param dataSource the data source
     * @return the stats response
     */
    StatsResponse findRatingsByTargetIdsAndDataSource(String targetIds, String dataSource);

    /**
     * Find comments by target id and data source list.
     *
     * @param targetId the target id
     * @param dataSource the data source
     * @return the list
     */
    List<CommentResponse> findCommentsByTargetIdAndDataSource(String targetId, String dataSource);

    /**
     * Delete all ratings.
     */
    void deleteAllRatings();

}
