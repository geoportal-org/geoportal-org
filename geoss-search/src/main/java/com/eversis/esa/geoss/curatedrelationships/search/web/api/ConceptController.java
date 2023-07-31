package com.eversis.esa.geoss.curatedrelationships.search.web.api;

import com.eversis.esa.geoss.curatedrelationships.search.service.concept.ConceptSearchService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Slf4j
@Api(tags = {"CONCEPTS"})
@RequestMapping("/api/concepts")
@RestController
class ConceptController {

    private final ConceptSearchService conceptSearchService;

    @Autowired
    public ConceptController(ConceptSearchService conceptSearchService) {
        this.conceptSearchService = conceptSearchService;
    }

    @ApiOperation(value = "Search for concepts related to search phrase")
    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<String> findConceptNames(
            @ApiParam(value = "Search phrase") @RequestParam(name = "st") @NotBlank String phrase,
            @ApiParam(value = "Max items count") @RequestParam(name = "ct", defaultValue = "10") @Min(1) Integer size) {
        if (log.isDebugEnabled()) {
            log.debug("Searching for concepts related to phrase: {} with max size: {}", phrase, size);
        }
        return conceptSearchService.findConceptNames(phrase, size);
    }

}
