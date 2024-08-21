package com.eversis.esa.geoss.contents.service.impl;

import com.eversis.esa.geoss.contents.domain.Content;
import com.eversis.esa.geoss.contents.repository.ContentRepository;
import com.eversis.esa.geoss.contents.service.ContentService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
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

    @Override
    public Content updateContent(long contentId, Content contentDto) {
        log.info("Updating content with id {}, using model {}", contentId, contentDto);
        Content updatedContent = contentRepository.findById(contentId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Content entity with id: " + contentId + " does not exist"));
        contentDto.setId(contentId);
        contentDto.setCreatedBy(updatedContent.getCreatedBy());
        contentDto.setCreatedDate(updatedContent.getCreatedDate());
        return contentRepository.save(contentDto);
    }

}
