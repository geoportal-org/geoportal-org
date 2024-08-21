package com.eversis.esa.geoss.contents.service.impl;

import com.eversis.esa.geoss.contents.domain.Page;
import com.eversis.esa.geoss.contents.repository.PageRepository;
import com.eversis.esa.geoss.contents.service.PageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Page service.
 */
@Slf4j
@Service
public class PageServiceImpl implements PageService {

    private final PageRepository pageRepository;

    /**
     * Instantiates a new Page service.
     *
     * @param pageRepository the page repository
     */
    @Autowired
    public PageServiceImpl(PageRepository pageRepository) {
        this.pageRepository = pageRepository;
    }

    @Override
    public void deleteByIdsIn(List<Long> ids) {
        pageRepository.deleteByIdsIn(ids);
    }

    @Override
    public Page updatePage(long pageId, Page pageDto) {
        log.info("Updating page with id {}, using model {}", pageId, pageDto);
        Page updatedPage = pageRepository.findById(pageId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Page entity with id: " + pageId + " does not exist"));
        pageDto.setId(pageId);
        pageDto.setCreatedBy(updatedPage.getCreatedBy());
        pageDto.setCreatedDate(updatedPage.getCreatedDate());
        return pageRepository.save(pageDto);
    }

}
