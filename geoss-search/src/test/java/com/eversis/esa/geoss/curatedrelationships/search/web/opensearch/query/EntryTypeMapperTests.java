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

public class EntryTypeMapperTests {

    @ParameterizedTest
    @ValueSource(strings = {"", " ", " ,", "data_res"})
    void whenEntryTypesStringIsNotValid_thenReturnEmptySet(String entryTypesInput) {
        Set<EntryType> matchedTypes = EntryTypeMapper.mapFromString(entryTypesInput);

        assertThat(matchedTypes, is(notNullValue()));
        assertThat(matchedTypes, is(empty()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"data_resource", "DATA_RESOURCE", "information_resource", "service_resource"})
    void whenEntryTypesStringContainsSingleValidValue_thenReturnSetWithSingleItem(String entryTypesInput) {
        Set<EntryType> matchedTypes = EntryTypeMapper.mapFromString(entryTypesInput);

        assertThat(matchedTypes, is(notNullValue()));
        assertThat(matchedTypes, hasSize(1));
    }

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
