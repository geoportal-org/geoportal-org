package com.eversis.esa.geoss.proxy.service;

import java.util.List;

import com.eversis.esa.geoss.proxy.domain.GetMostPopularModel;
import com.eversis.esa.geoss.proxy.domain.GetNumberOfSearchesModel;
import com.eversis.esa.geoss.proxy.domain.MostPopular;
import com.eversis.esa.geoss.proxy.domain.Search;

/**
 * The interface Statistics service.
 */
public interface StatisticsService {

    /**
     * Gets number of searches.
     *
     * @param getNumberOfSearchesModel the get number of searches model
     * @return the number of searches
     */
    List<Search> getNumberOfSearches(GetNumberOfSearchesModel getNumberOfSearchesModel);

    /**
     * Gets most popular resources.
     *
     * @param getMostPopularModel the get most popular model
     * @return the most popular resources
     */
    List<MostPopular> getMostPopularResources(GetMostPopularModel getMostPopularModel);

    /**
     * Gets most popular keywords.
     *
     * @param getMostPopularModel the get most popular model
     * @return the most popular keywords
     */
    List<MostPopular> getMostPopularKeywords(GetMostPopularModel getMostPopularModel);

    /**
     * Gets most popular catalogs.
     *
     * @param getMostPopularModel the get most popular model
     * @return the most popular catalogs
     */
    List<MostPopular> getMostPopularCatalogs(GetMostPopularModel getMostPopularModel);

    /**
     * Gets most popular organizations.
     *
     * @param getMostPopularModel the get most popular model
     * @return the most popular organizations
     */
    List<MostPopular> getMostPopularOrganizations(GetMostPopularModel getMostPopularModel);

    /**
     * Gets most popular areas.
     *
     * @param getMostPopularModel the get most popular model
     * @return the most popular areas
     */
    List<MostPopular> getMostPopularAreas(GetMostPopularModel getMostPopularModel);
}
