package com.eversis.esa.geoss.curatedrelationships.search.service.search;

import com.eversis.esa.geoss.curatedrelationships.search.model.DataSource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

/**
 * The type Search strategy factory tests.
 */
@ExtendWith(MockitoExtension.class)
public class SearchStrategyFactoryTests {

    @Mock
    private CkanSearchStrategy ckanSearchStrategy;
    @Mock
    private GeossSearchStrategy geossSearchStrategy;

    @InjectMocks
    private SearchStrategyFactory searchStrategyFactory;

    /**
     * When geoss source type then return geoss search strategy.
     */
    @Test
    void whenGeossSourceType_thenReturnGeossSearchStrategy() {
        DataSource dataSource = DataSource.GEOSS_CR;

        SearchStrategy searchStrategy = searchStrategyFactory.getSearchStrategy(dataSource);

        assertThat(searchStrategy, instanceOf(GeossSearchStrategy.class));
    }

    /**
     * When ameri geoss source type then return ckan search strategy.
     */
    @Test
    void whenAmeriGeossSourceType_thenReturnCkanSearchStrategy() {
        DataSource dataSource = DataSource.AMERIGEOSS_CKAN;

        SearchStrategy searchStrategy = searchStrategyFactory.getSearchStrategy(dataSource);

        assertThat(searchStrategy, instanceOf(CkanSearchStrategy.class));
    }

}
