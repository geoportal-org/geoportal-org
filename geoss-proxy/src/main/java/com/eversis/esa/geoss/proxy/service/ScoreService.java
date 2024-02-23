package com.eversis.esa.geoss.proxy.service;

import com.eversis.esa.geoss.proxy.domain.Score;
import com.eversis.esa.geoss.proxy.domain.ScoreModel;

/**
 * The interface Score service.
 */
public interface ScoreService {

    /**
     * Add score.
     *
     * @param scoreModel the score model
     */
    void addScore(ScoreModel scoreModel);

    /**
     * Gets score.
     *
     * @param entryId the entry id
     * @return the score
     */
    Score getScore(String entryId);

}
