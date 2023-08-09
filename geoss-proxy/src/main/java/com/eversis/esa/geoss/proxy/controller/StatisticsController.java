package com.eversis.esa.geoss.proxy.controller;

import com.eversis.esa.geoss.proxy.domain.MostPopular;
import com.eversis.esa.geoss.proxy.domain.MostPopularAreasModel;
import com.eversis.esa.geoss.proxy.domain.MostPopularCatalogsModel;
import com.eversis.esa.geoss.proxy.domain.MostPopularKeywordsModel;
import com.eversis.esa.geoss.proxy.domain.MostPopularOrganisationsModel;
import com.eversis.esa.geoss.proxy.domain.MostPopularResourcesModel;
import com.eversis.esa.geoss.proxy.domain.NumberOfSearchesModel;
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

import jakarta.validation.Valid;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

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
     * Gets number of searches.
     *
     * @param numberOfSearchesModel the number of searches model
     * @return the number of searches
     */
    @GetMapping(value = "getNumberOfSearches")
    @ResponseStatus(HttpStatus.OK)
    public List<Search> getNumberOfSearches(@Valid @RequestBody NumberOfSearchesModel numberOfSearchesModel) {
        log.info("numberOfSearchesModel:{}", numberOfSearchesModel);
        return statisticsService.getNumberOfSearches(numberOfSearchesModel);
    }

    /**
     * Gets most popular resources.
     *
     * @param mostPopularResourcesModel the most popular resources model
     * @return the most popular resources
     */
    @GetMapping(value = "getMostPopularResources")
    @ResponseStatus(HttpStatus.OK)
    public List<MostPopular> getMostPopularResources(
            @Valid @RequestBody MostPopularResourcesModel mostPopularResourcesModel) {
        log.info("mostPopularResourcesModel:{}", mostPopularResourcesModel);
        return statisticsService.getMostPopularResources(mostPopularResourcesModel);
    }

    /**
     * Gets most popular keywords.
     *
     * @param mostPopularKeywordsModel the most popular keywords model
     * @return the most popular keywords
     */
    @GetMapping(value = "getMostPopularKeywords")
    @ResponseStatus(HttpStatus.OK)
    public List<MostPopular> getMostPopularKeywords(
            @Valid @RequestBody MostPopularKeywordsModel mostPopularKeywordsModel) {
        log.info("mostPopularKeywordsModel:{}", mostPopularKeywordsModel);
        return statisticsService.getMostPopularKeywords(mostPopularKeywordsModel);
    }

    /**
     * Gets most popular catalogs.
     *
     * @param mostPopularCatalogsModel the most popular catalogs model
     * @return the most popular catalogs
     */
    @GetMapping(value = "getMostPopularCatalogs")
    @ResponseStatus(HttpStatus.OK)
    public List<MostPopular> getMostPopularCatalogs(
            @Valid @RequestBody MostPopularCatalogsModel mostPopularCatalogsModel) {
        log.info("mostPopularCatalogsModel:{}", mostPopularCatalogsModel);
        return statisticsService.getMostPopularCatalogs(mostPopularCatalogsModel);
    }

    /**
     * Gets most popular organizations.
     *
     * @param mostPopularOrganisationsModel the most popular organisations model
     * @return the most popular organizations
     */
    @GetMapping(value = "getMostPopularOrganizations")
    @ResponseStatus(HttpStatus.OK)
    public List<MostPopular> getMostPopularOrganizations(
            @Valid @RequestBody MostPopularOrganisationsModel mostPopularOrganisationsModel) {
        log.info("mostPopularOrganisationsModel:{}", mostPopularOrganisationsModel);
        return statisticsService.getMostPopularOrganizations(mostPopularOrganisationsModel);
    }

    /**
     * Gets most popular areas.
     *
     * @param mostPopularAreasModel the most popular areas model
     * @return the most popular areas
     */
    @GetMapping(value = "getMostPopularAreas")
    @ResponseStatus(HttpStatus.OK)
    public List<MostPopular> getMostPopularAreas(@Valid @RequestBody MostPopularAreasModel mostPopularAreasModel) {
        log.info("mostPopularAreasModel:{}", mostPopularAreasModel);
        return statisticsService.getMostPopularAreas(mostPopularAreasModel);
    }

}
