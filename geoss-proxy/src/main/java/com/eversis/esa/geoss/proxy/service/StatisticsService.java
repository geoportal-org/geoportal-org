package com.eversis.esa.geoss.proxy.service;

import java.util.List;

import com.eversis.esa.geoss.proxy.domain.MostPopular;
import com.eversis.esa.geoss.proxy.domain.MostPopularAreasModel;
import com.eversis.esa.geoss.proxy.domain.MostPopularCatalogsModel;
import com.eversis.esa.geoss.proxy.domain.MostPopularKeywordsModel;
import com.eversis.esa.geoss.proxy.domain.MostPopularOrganisationsModel;
import com.eversis.esa.geoss.proxy.domain.MostPopularResourcesModel;
import com.eversis.esa.geoss.proxy.domain.NumberOfSearchesModel;
import com.eversis.esa.geoss.proxy.domain.Search;

/**
 * The interface Statistics service.
 */
public interface StatisticsService {

    /**
     * Gets number of searches.
     *
     * @param numberOfSearchesModel the number of searches model
     * @return the number of searches
     */
    List<Search> getNumberOfSearches(NumberOfSearchesModel numberOfSearchesModel);

    /**
     * Gets most popular resources.
     *
     * @param mostPopularResourcesModel the most popular resources model
     * @return the most popular resources
     */
    List<MostPopular> getMostPopularResources(MostPopularResourcesModel mostPopularResourcesModel);

    /**
     * Gets most popular keywords.
     *
     * @param mostPopularKeywordsModel the most popular keywords model
     * @return the most popular keywords
     */
    List<MostPopular> getMostPopularKeywords(MostPopularKeywordsModel mostPopularKeywordsModel);

    /**
     * Gets most popular catalogs.
     *
     * @param mostPopularCatalogsModel the most popular catalogs model
     * @return the most popular catalogs
     */
    List<MostPopular> getMostPopularCatalogs(MostPopularCatalogsModel mostPopularCatalogsModel);

    /**
     * Gets most popular organizations.
     *
     * @param mostPopularOrganisationsModel the most popular organisations model
     * @return the most popular organizations
     */
    List<MostPopular> getMostPopularOrganizations(MostPopularOrganisationsModel mostPopularOrganisationsModel);

    /**
     * Gets most popular areas.
     *
     * @param mostPopularAreasModel the most popular areas model
     * @return the most popular areas
     */
    List<MostPopular> getMostPopularAreas(MostPopularAreasModel mostPopularAreasModel);
}
