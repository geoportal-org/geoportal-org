package com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.query;

import com.eversis.esa.geoss.curatedrelationships.search.model.entity.BoundingBox;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BoundingBoxMapperTests {

    @Test
    void whenBboxIsNull_thenReturnMappingReturnsNull() {
        BoundingBox boundingBox = BoundingBoxMapper.mapFromString(null);

        assertThat(boundingBox, is(nullValue()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    void whenBboxIsBlank_thenReturnMappingReturnsNull(String bbox) {
        BoundingBox boundingBox = BoundingBoxMapper.mapFromString(bbox);

        assertThat(boundingBox, is(nullValue()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"10", "12,11", "12,13,14", "12,13,14,15,16"})
    void whenBboxContainsNotFourNumber_thenMappingThrowsException(String input) {
        assertThrows(IllegalArgumentException.class, () -> BoundingBoxMapper.mapFromString(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "12,a11", "12c,13,14", "10,11,12,b", "12,13,14,15c"})
    void whenBboxContainsNotOnlyNumber_thenMappingThrowsException(String input) {
        assertThrows(IllegalArgumentException.class, () -> BoundingBoxMapper.mapFromString(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "0,-95,0,0", "0,-95,0,-95", "0,0,0,-95",
            "0,95,0,0", "0,95,0,95", "0,0,0,95"
    })
    void whenLatitudeNotInValidRange_thenThrowException(String input) {
        assertThrows(IllegalArgumentException.class, () -> BoundingBoxMapper.mapFromString(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "-185,0,0,0", "-185,0,-185,0", "0,0,-185,0",
            "185,0,0,0", "185,0,185,0", "0,0,185,0"
    })
    void whenLongitudeNotInValidRange_thenThrowException(String input) {
        assertThrows(IllegalArgumentException.class, () -> BoundingBoxMapper.mapFromString(input));
    }
}
