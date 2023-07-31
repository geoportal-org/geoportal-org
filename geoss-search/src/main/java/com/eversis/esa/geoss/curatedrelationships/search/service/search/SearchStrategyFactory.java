package com.eversis.esa.geoss.curatedrelationships.search.service.search;

import com.eversis.esa.geoss.curatedrelationships.search.model.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The type Search strategy factory.
 */
@Component
public class SearchStrategyFactory {

    private final CkanSearchStrategy ckanSearchStrategy;
    private final GeossSearchStrategy geossSearchStrategy;
    private final WikipediaSearchStrategy wikipediaSearchStrategy;
    private final ZenodoSearchStrategy zenodoSearchStrategy;

    /**
     * Instantiates a new Search strategy factory.
     *
     * @param ckanSearchStrategy the ckan search strategy
     * @param geossSearchStrategy the geoss search strategy
     * @param wikipediaSearchStrategy the wikipedia search strategy
     * @param zenodoSearchStrategy the zenodo search strategy
     */
    @Autowired
    public SearchStrategyFactory(
            CkanSearchStrategy ckanSearchStrategy,
            GeossSearchStrategy geossSearchStrategy,
            WikipediaSearchStrategy wikipediaSearchStrategy,
            ZenodoSearchStrategy zenodoSearchStrategy) {
        this.ckanSearchStrategy = ckanSearchStrategy;
        this.geossSearchStrategy = geossSearchStrategy;
        this.wikipediaSearchStrategy = wikipediaSearchStrategy;
        this.zenodoSearchStrategy = zenodoSearchStrategy;
    }

    /**
     * Factory method, which provides appropriate search strategy for specified source type.
     *
     * @param searchStrategyType source type
     * @return SearchStrategy selected for specified source type
     */
    public SearchStrategy getSearchStrategy(DataSource searchStrategyType) {
        switch (searchStrategyType) {
            case AMERIGEOSS_CKAN:
                return ckanSearchStrategy;
            case WIKIPEDIA:
                return wikipediaSearchStrategy;
            case ZENODO:
                return zenodoSearchStrategy;
            case GEOSS_CR:
            default:
                return geossSearchStrategy;
        }
    }

}
