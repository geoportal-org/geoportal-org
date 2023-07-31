package com.eversis.esa.geoss.curatedrelationships.search.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * The type Data source tests.
 */
public class DataSourceTests {

    /**
     * When source type string is valid then return source type.
     *
     * @param sourceTypeInput the source type input
     */
    @ParameterizedTest
    @ValueSource(strings = {"geoss_cr", "amerigeoss_ckan", "GEOSS_CR", "AMERIGEOSS_CKAN"})
    void whenSourceTypeStringIsValid_thenReturnSourceType(String sourceTypeInput) {
        DataSource dataSource = DataSource.fromString(sourceTypeInput);
        assertThat(dataSource, is(not(nullValue())));
    }

    /**
     * When source type string is not valid then throw exception.
     *
     * @param sourceTypeInput the source type input
     */
    @ParameterizedTest
    @ValueSource(strings = {"geoss_cr_", "amerigeoss_ckan_", "", " ", "wdj"})
    void whenSourceTypeStringIsNotValid_thenThrowException(String sourceTypeInput) {
        assertThrows(IllegalArgumentException.class, () -> DataSource.fromString(sourceTypeInput));
    }

}
