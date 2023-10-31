package com.eversis.esa.geoss.contents.controller;

import com.eversis.esa.geoss.contents.service.ContentService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

/**
 * The type Content controller.
 */
@Log4j2
@RepositoryRestController("/content")
@ResponseBody
@Tag(name = "contents")
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
