package com.eversis.esa.geoss.contents.controller;

import java.util.List;

import com.eversis.esa.geoss.contents.service.ContentService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The type Content controller.
 */
@Log4j2
@RepositoryRestController("/content")
@ResponseBody
public class ContentController {

    private final ContentService contentService;

    /**
     * Instantiates a new Content controller.
     *
     * @param contentService the content service
     */
    public ContentController(
            final ContentService contentService) {
        this.contentService = contentService;
    }

    /**
     * Delete by ids in.
     *
     * @param ids the ids
     */
    @DeleteMapping("deleteByIdsIn")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteByIdsIn(@RequestParam List<Long> ids) {
        contentService.deleteByIdsIn(ids);
    }

}
