package com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.query;

import com.eversis.esa.geoss.curatedrelationships.search.model.entity.BoundingBox;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * The type Bounding box mapper tests.
 */
public class BoundingBoxMapperTests {

    /**
     * When bbox is null then return mapping returns null.
     */
    @Test
    void whenBboxIsNull_thenReturnMappingReturnsNull() {
        BoundingBox boundingBox = BoundingBoxMapper.mapFromString(null);

        assertThat(boundingBox, is(nullValue()));
    }

    /**
     * When bbox is blank then return mapping returns null.
     *
     * @param bbox the bbox
     */
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    void whenBboxIsBlank_thenReturnMappingReturnsNull(String bbox) {
        BoundingBox boundingBox = BoundingBoxMapper.mapFromString(bbox);

        assertThat(boundingBox, is(nullValue()));
    }

    /**
     * When bbox contains not four number then mapping throws exception.
     *
     * @param input the input
     */
    @ParameterizedTest
    @ValueSource(strings = {"10", "12,11", "12,13,14", "12,13,14,15,16"})
    void whenBboxContainsNotFourNumber_thenMappingThrowsException(String input) {
        assertThrows(IllegalArgumentException.class, () -> BoundingBoxMapper.mapFromString(input));
    }

    /**
     * When bbox contains not only number then mapping throws exception.
     *
     * @param input the input
     */
    @ParameterizedTest
    @ValueSource(strings = {"a", "12,a11", "12c,13,14", "10,11,12,b", "12,13,14,15c"})
    void whenBboxContainsNotOnlyNumber_thenMappingThrowsException(String input) {
        assertThrows(IllegalArgumentException.class, () -> BoundingBoxMapper.mapFromString(input));
    }

    /**
     * When latitude not in valid range then throw exception.
     *
     * @param input the input
     */
    @ParameterizedTest
    @ValueSource(strings = {
            "0,-95,0,0", "0,-95,0,-95", "0,0,0,-95",
            "0,95,0,0", "0,95,0,95", "0,0,0,95"
    })
    void whenLatitudeNotInValidRange_thenThrowException(String input) {
        assertThrows(IllegalArgumentException.class, () -> BoundingBoxMapper.mapFromString(input));
    }

    /**
     * When longitude not in valid range then throw exception.
     *
     * @param input the input
     */
    @ParameterizedTest
    @ValueSource(strings = {
            "-185,0,0,0", "-185,0,-185,0", "0,0,-185,0",
            "185,0,0,0", "185,0,185,0", "0,0,185,0"
    })
    void whenLongitudeNotInValidRange_thenThrowException(String input) {
        assertThrows(IllegalArgumentException.class, () -> BoundingBoxMapper.mapFromString(input));
    }
}
