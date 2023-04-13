package com.eversis.esa.geoss.contents.service;

import java.util.List;

/**
 * The interface Content service.
 */
public interface ContentService {

    /**
     * Delete by ids in.
     *
     * @param ids the ids
     */
    void deleteByIdsIn(List<Long> ids);

}
