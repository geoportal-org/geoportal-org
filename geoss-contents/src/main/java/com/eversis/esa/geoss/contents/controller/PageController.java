package com.eversis.esa.geoss.contents.controller;

import com.eversis.esa.geoss.contents.service.PageService;

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
 * The type Page controller.
 */
@Log4j2
@RepositoryRestController("/page")
@ResponseBody
@Tag(name = "pages")
public class PageController {

    private final PageService pageService;

    /**
     * Instantiates a new Page controller.
     *
     * @param pageService the page service
     */
    public PageController(
            final PageService pageService) {
        this.pageService = pageService;
    }

    /**
     * Delete by ids in.
     *
     * @param ids the ids
     */
    @DeleteMapping("deleteByIdsIn")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteByIdsIn(@RequestParam List<Long> ids) {
        pageService.deleteByIdsIn(ids);
    }

}
