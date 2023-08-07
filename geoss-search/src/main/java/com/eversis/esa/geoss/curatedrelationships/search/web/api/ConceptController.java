package com.eversis.esa.geoss.curatedrelationships.search.web.api;

import com.eversis.esa.geoss.curatedrelationships.search.service.concept.ConceptSearchService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

/**
 * The type Concept controller.
 */
@Log4j2
@Tag(name = "CONCEPTS")
@RequestMapping("${spring.data.rest.base-path}/concepts")
@RestController
class ConceptController {

    private final ConceptSearchService conceptSearchService;

    /**
     * Instantiates a new Concept controller.
     *
     * @param conceptSearchService the concept search service
     */
    @Autowired
    public ConceptController(ConceptSearchService conceptSearchService) {
        this.conceptSearchService = conceptSearchService;
    }

    /**
     * Find concept names list.
     *
     * @param phrase the phrase
     * @param size the size
     * @return the list
     */
    @Operation(summary = "Search for concepts related to search phrase")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> findConceptNames(
            @Parameter(description = "Search phrase") @RequestParam(name = "st") @NotBlank String phrase,
            @Parameter(description = "Max items count") @RequestParam(name = "ct", defaultValue = "10") @Min(
                    1) Integer size) {
        if (log.isDebugEnabled()) {
            log.debug("Searching for concepts related to phrase: {} with max size: {}", phrase, size);
        }
        return conceptSearchService.findConceptNames(phrase, size);
    }
}
