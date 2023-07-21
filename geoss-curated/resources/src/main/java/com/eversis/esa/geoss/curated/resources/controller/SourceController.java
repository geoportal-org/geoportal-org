package com.eversis.esa.geoss.curated.resources.controller;

import java.util.List;

import com.eversis.esa.geoss.curated.resources.domain.Source;
import com.eversis.esa.geoss.curated.resources.repository.SourceRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The type Source controller.
 */
@Log4j2
@BasePathAwareController("/source")
@ResponseBody
@Tag(name = "source")
public class SourceController {

    private final SourceRepository sourceRepository;

    /**
     * Instantiates a new Source controller.
     *
     * @param sourceRepository the source repository
     */
    public SourceController(SourceRepository sourceRepository) {
        this.sourceRepository = sourceRepository;
    }

    /**
     * Gets source values.
     *
     * @return the source values
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Source> getSourceValues() {
        log.info("Getting source values...");
        return sourceRepository.findByIsCustomOrderByTerm(0);
    }

}
