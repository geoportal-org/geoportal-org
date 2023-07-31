package com.eversis.esa.geoss.curatedrelationships.search.service.search;

import com.eversis.esa.geoss.curatedrelationships.search.model.DataSource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

@ExtendWith(MockitoExtension.class)
public class SearchStrategyFactoryTests {

    @Mock
    private CkanSearchStrategy ckanSearchStrategy;
    @Mock
    private GeossSearchStrategy geossSearchStrategy;

    @InjectMocks
    private SearchStrategyFactory searchStrategyFactory;

    @Test
    void whenGeossSourceType_thenReturnGeossSearchStrategy() {
        DataSource dataSource = DataSource.GEOSS_CR;

        SearchStrategy searchStrategy = searchStrategyFactory.getSearchStrategy(dataSource);

        assertThat(searchStrategy, instanceOf(GeossSearchStrategy.class));
    }

    @Test
    void whenAmeriGeossSourceType_thenReturnCkanSearchStrategy() {
        DataSource dataSource = DataSource.AMERIGEOSS_CKAN;

        SearchStrategy searchStrategy = searchStrategyFactory.getSearchStrategy(dataSource);

        assertThat(searchStrategy, instanceOf(CkanSearchStrategy.class));
    }

}
