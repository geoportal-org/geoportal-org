package com.eversis.esa.geoss.contents.service;

import java.util.List;

/**
 * The interface Page service.
 */
public interface PageService {

    /**
     * Delete by ids in.
     *
     * @param ids the ids
     */
    void deleteByIdsIn(List<Long> ids);

}
