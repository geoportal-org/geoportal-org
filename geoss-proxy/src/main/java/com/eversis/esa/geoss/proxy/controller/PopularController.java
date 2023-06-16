package com.eversis.esa.geoss.proxy.controller;

import java.util.List;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.eversis.esa.geoss.proxy.domain.PopularWord;
import com.eversis.esa.geoss.proxy.service.PopularService;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Popular controller.
 */
@Log4j2
@RestController
@RequestMapping(value = "/rest/popular", produces = APPLICATION_JSON_VALUE)
@Validated
public class PopularController {

    private final PopularService popularService;

    /**
     * Instantiates a new Popular controller.
     *
     * @param popularService the popular service
     */
    public PopularController(final PopularService popularService) {
        this.popularService = popularService;
    }

    /**
     * Gets popular.
     *
     * @param query the query
     * @param limit the limit
     * @return the popular
     */
    @GetMapping(value = "")
    public List<PopularWord> getPopularWords(@RequestParam("query") @NotNull @Size(min = 2) String query,
            @RequestParam("limit") @NotNull @Min(1) @Max(10) int limit) {
        log.info("query:{}, limit:{}", query, limit);
        return popularService.getPopularWords(query, limit);
    }

}
