package com.eversis.esa.geoss.proxy.controller;

import java.util.List;
import jakarta.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.eversis.esa.geoss.proxy.domain.GetMostPopularModel;
import com.eversis.esa.geoss.proxy.domain.GetNumberOfSearchesModel;
import com.eversis.esa.geoss.proxy.domain.MostPopular;
import com.eversis.esa.geoss.proxy.domain.Search;
import com.eversis.esa.geoss.proxy.service.StatisticsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Statistics controller.
 */
@Log4j2
@RestController
@RequestMapping(value = "/rest/statistics", produces = APPLICATION_JSON_VALUE)
@Validated
public class StatisticsController {

    private final StatisticsService statisticsService;

    /**
     * Instantiates a new Statistics controller.
     *
     * @param statisticsService the statistics service
     */
    public StatisticsController(final StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    /**
     * Get number of searches list.
     *
     * @param getNumberOfSearchesModel the get number of searches model
     * @return the list
     */
    @GetMapping(value = "getNumberOfSearches")
    @ResponseStatus(HttpStatus.OK)
    public List<Search> getNumberOfSearches(@Valid @RequestBody GetNumberOfSearchesModel getNumberOfSearchesModel) {
        log.info("getNumberOfSearchesModel:{}", getNumberOfSearchesModel);
        return statisticsService.getNumberOfSearches(getNumberOfSearchesModel);
    }

    /**
     * Get most popular resources list.
     *
     * @param getMostPopularModel the get most popular model
     * @return the list
     */
    @GetMapping(value = "getMostPopularResources")
    @ResponseStatus(HttpStatus.OK)
    public List<MostPopular> getMostPopularResources(@Valid @RequestBody GetMostPopularModel getMostPopularModel) {
        log.info("getMostPopularModel:{}", getMostPopularModel);
        return statisticsService.getMostPopularResources(getMostPopularModel);
    }

    /**
     * Get most popular keywords list.
     *
     * @param getMostPopularModel the get most popular model
     * @return the list
     */
    @GetMapping(value = "getMostPopularKeywords")
    @ResponseStatus(HttpStatus.OK)
    public List<MostPopular> getMostPopularKeywords(@Valid @RequestBody GetMostPopularModel getMostPopularModel) {
        log.info("getMostPopularModel:{}", getMostPopularModel);
        return statisticsService.getMostPopularKeywords(getMostPopularModel);
    }

    /**
     * Get most popular catalogs list.
     *
     * @param getMostPopularModel the get most popular model
     * @return the list
     */
    @GetMapping(value = "getMostPopularCatalogs")
    @ResponseStatus(HttpStatus.OK)
    public List<MostPopular> getMostPopularCatalogs(@Valid @RequestBody GetMostPopularModel getMostPopularModel) {
        log.info("getMostPopularModel:{}", getMostPopularModel);
        return statisticsService.getMostPopularCatalogs(getMostPopularModel);
    }

    /**
     * Get most popular organizations list.
     *
     * @param getMostPopularModel the get most popular model
     * @return the list
     */
    @GetMapping(value = "getMostPopularOrganizations")
    @ResponseStatus(HttpStatus.OK)
    public List<MostPopular> getMostPopularOrganizations(@Valid @RequestBody GetMostPopularModel getMostPopularModel) {
        log.info("getMostPopularModel:{}", getMostPopularModel);
        return statisticsService.getMostPopularOrganizations(getMostPopularModel);
    }

    /**
     * Get most popular areas list.
     *
     * @param getMostPopularModel the get most popular model
     * @return the list
     */
    @GetMapping(value = "getMostPopularAreas")
    @ResponseStatus(HttpStatus.OK)
    public List<MostPopular> getMostPopularAreas(@Valid @RequestBody GetMostPopularModel getMostPopularModel) {
        log.info("getMostPopularModel:{}", getMostPopularModel);
        return statisticsService.getMostPopularAreas(getMostPopularModel);
    }

}
