package com.eversis.esa.geoss.contents.service;

import com.eversis.esa.geoss.contents.domain.Content;

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

    /**
     * Update content content.
     *
     * @param contentId  the content id
     * @param contentDto the content dto
     * @return the content
     */
    Content updateContent(long contentId, Content contentDto);

}
