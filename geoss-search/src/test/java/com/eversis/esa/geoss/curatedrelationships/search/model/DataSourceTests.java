package com.eversis.esa.geoss.curatedrelationships.search.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DataSourceTests {

    @ParameterizedTest
    @ValueSource(strings = {"geoss_cr", "amerigeoss_ckan", "GEOSS_CR", "AMERIGEOSS_CKAN"})
    void whenSourceTypeStringIsValid_thenReturnSourceType(String sourceTypeInput) {
        DataSource dataSource = DataSource.fromString(sourceTypeInput);
        assertThat(dataSource, is(not(nullValue())));
    }

    @ParameterizedTest
    @ValueSource(strings = {"geoss_cr_", "amerigeoss_ckan_", "", " ", "wdj"})
    void whenSourceTypeStringIsNotValid_thenThrowException(String sourceTypeInput) {
        assertThrows(IllegalArgumentException.class, () -> DataSource.fromString(sourceTypeInput));
    }

}
