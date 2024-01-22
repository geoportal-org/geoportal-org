package com.eversis.esa.geoss.curated.resources.mapper;

import com.eversis.esa.geoss.curated.resources.domain.EntryRating;
import com.eversis.esa.geoss.curated.resources.model.CommentResponse;
import com.eversis.esa.geoss.curated.resources.model.EntryRatingModel;
import com.eversis.esa.geoss.curated.resources.model.EntryRatingWithCommentModel;
import com.eversis.esa.geoss.curated.resources.model.EntryRatingWithoutCommentModel;
import org.springframework.stereotype.Component;

/**
 * The type Entry rating mapper.
 */
@Component
public class EntryRatingMapper {

    /**
     * Map to entry rating entry rating.
     *
     * @param model the model
     * @return the entry rating
     */
    public EntryRating mapToEntryRating(EntryRatingModel model) {
        return getEntryRating(model);
    }

    /**
     * Map to entry rating entry rating.
     *
     * @param model the model
     * @return the entry rating
     */
    public EntryRating mapToEntryRating(EntryRatingWithCommentModel model) {
        return getEntryRating(model);
    }

    /**
     * Map to entry rating entry rating.
     *
     * @param model the model
     * @return the entry rating
     */
    public EntryRating mapToEntryRating(EntryRatingWithoutCommentModel model) {
        return getEntryRating(model);
    }

    /**
     * Map to entry rating entry rating.
     *
     * @param model the model
     * @param entryRating the entry rating
     * @return the entry rating
     */
    public EntryRating mapToEntryRating(EntryRatingModel model, EntryRating entryRating) {
        return getEntryRating(model, entryRating);
    }

    /**
     * Map to comment response comment response.
     *
     * @param entryRating the entry rating
     * @return the comment response
     */
    public CommentResponse mapToCommentResponse(EntryRating entryRating) {
        if (entryRating == null) {
            return null;
        }
        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setScore(entryRating.getScore());
        commentResponse.setCreatedDate(String.valueOf(entryRating.getCreateDate()));
        commentResponse.setComment(entryRating.getComment());
        commentResponse.setUserName("");
        commentResponse.setUserId(entryRating.getUserId());
        return commentResponse;
    }

    private EntryRating getEntryRating(EntryRatingModel model) {
        if (model == null) {
            return null;
        }
        EntryRating entryRating = new EntryRating();
        entryRating.setTargetId(model.getTargetId());
        entryRating.setName(model.getName());
        entryRating.setUserId(model.getUserId());
        entryRating.setGroupId(model.getGroupId());
        entryRating.setScore(model.getScore());
        entryRating.setComment(model.getComment());
        entryRating.setEntryXml(model.getEntryXml());
        entryRating.setDataSource(model.getDataSource());
        entryRating.setSourceBaseUrl(model.getSourceBaseUrl());
        entryRating.setValid(model.getValid());
        return entryRating;
    }

    private EntryRating getEntryRating(EntryRatingModel model, EntryRating entryRating) {
        if (model == null) {
            return null;
        }
        entryRating.setTargetId(model.getTargetId());
        entryRating.setName(model.getName());
        entryRating.setUserId(model.getUserId());
        entryRating.setGroupId(model.getGroupId());
        entryRating.setScore(model.getScore());
        entryRating.setComment(model.getComment());
        entryRating.setEntryXml(model.getEntryXml());
        entryRating.setDataSource(model.getDataSource());
        entryRating.setSourceBaseUrl(model.getSourceBaseUrl());
        entryRating.setValid(model.getValid());
        return entryRating;
    }

    private EntryRating getEntryRating(EntryRatingWithCommentModel model) {
        if (model == null) {
            return null;
        }
        EntryRating entryRating = new EntryRating();
        entryRating.setTargetId(model.getTargetId());
        entryRating.setName(model.getName());
        entryRating.setScore(model.getScore());
        entryRating.setComment(model.getComment());
        entryRating.setDataSource(model.getDataSource());
        entryRating.setValid(1);
        return entryRating;
    }

    private EntryRating getEntryRating(EntryRatingWithoutCommentModel model) {
        if (model == null) {
            return null;
        }
        EntryRating entryRating = new EntryRating();
        entryRating.setTargetId(model.getTargetId());
        entryRating.setName(model.getName());
        entryRating.setScore(model.getScore());
        entryRating.setDataSource(model.getDataSource());
        entryRating.setValid(1);
        return entryRating;
    }

}
