package com.eversis.esa.geoss.contents.service;

import com.eversis.esa.geoss.contents.domain.Page;

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

    /**
     * Update page page.
     *
     * @param pageId  the page id
     * @param pageDto the page dto
     * @return the page
     */
    Page updatePage(long pageId, Page pageDto);

}
