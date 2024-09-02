package com.eversis.esa.geoss.proxy.service;

import com.eversis.esa.geoss.proxy.domain.ViewCounter;
import com.eversis.esa.geoss.proxy.domain.ViewCounterModel;

import java.util.List;

/**
 * The interface View counter service.
 */
public interface ViewCounterService {

    /**
     * Increase counter.
     *
     * @param viewCounterModel the view counter model
     */
    void increaseCounter(ViewCounterModel viewCounterModel);

    /**
     * Gets counter.
     *
     * @param entryId the entry id
     * @return the counter
     */
    ViewCounter getCounter(String entryId);

    /**
     * Gets counter.
     *
     * @param entryIds the entry ids
     * @return the counter
     */
    List<ViewCounter> getCounter(List<String> entryIds);

}
