package com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.query;

import com.eversis.esa.geoss.curatedrelationships.search.model.entity.EntryType;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * The type Entry type mapper tests.
 */
public class EntryTypeMapperTests {

    /**
     * When entry types string is not valid then return empty set.
     *
     * @param entryTypesInput the entry types input
     */
    @ParameterizedTest
    @ValueSource(strings = {"", " ", " ,", "data_res"})
    void whenEntryTypesStringIsNotValid_thenReturnEmptySet(String entryTypesInput) {
        Set<EntryType> matchedTypes = EntryTypeMapper.mapFromString(entryTypesInput);

        assertThat(matchedTypes, is(notNullValue()));
        assertThat(matchedTypes, is(empty()));
    }

    /**
     * When entry types string contains single valid value then return set with single item.
     *
     * @param entryTypesInput the entry types input
     */
    @ParameterizedTest
    @ValueSource(strings = {"data_resource", "DATA_RESOURCE", "information_resource", "service_resource"})
    void whenEntryTypesStringContainsSingleValidValue_thenReturnSetWithSingleItem(String entryTypesInput) {
        Set<EntryType> matchedTypes = EntryTypeMapper.mapFromString(entryTypesInput);

        assertThat(matchedTypes, is(notNullValue()));
        assertThat(matchedTypes, hasSize(1));
    }

    /**
     * When entry types string contains multiple valid values then return set with mulitple items.
     *
     * @param entryTypesInput the entry types input
     */
    @ParameterizedTest
    @ValueSource(strings = {
            "data_resource,information_resource",
            "DATA_RESOURCE, information_resource",
            "information_resource   ,service_resource",
            "service_resource , data_resource"})
    void whenEntryTypesStringContainsMultipleValidValues_thenReturnSetWithMulitpleItems(String entryTypesInput) {
        Set<EntryType> matchedTypes = EntryTypeMapper.mapFromString(entryTypesInput);

        assertThat(matchedTypes, is(notNullValue()));
        assertThat(matchedTypes, hasSize(2));
    }

}
