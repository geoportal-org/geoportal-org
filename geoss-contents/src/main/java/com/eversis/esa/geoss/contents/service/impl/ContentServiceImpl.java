package com.eversis.esa.geoss.contents.service.impl;

import com.eversis.esa.geoss.contents.repository.ContentRepository;
import com.eversis.esa.geoss.contents.service.ContentService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Content service.
 */
@Slf4j
@Service
public class ContentServiceImpl implements ContentService {

    private final ContentRepository contentRepository;

    /**
     * Instantiates a new Content service.
     *
     * @param contentRepository the content repository
     */
    @Autowired
    public ContentServiceImpl(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    @Override
    public void deleteByIdsIn(List<Long> ids) {
        contentRepository.deleteByIdsIn(ids);
    }
}
