package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.mapper.impl;

import com.eversis.esa.geoss.curatedrelationships.search.model.entity.BoundingBox;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.GeoPoint;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

/**
 * The type Geo shape mapper tests.
 */
public class GeoShapeMapperTests {

    /**
     * When bbox is null then resized bbox is null.
     */
    @Test
    void whenBboxIsNull_thenResizedBboxIsNull() {
        BoundingBox boundingBox = null;

        BoundingBox resizedBoundingBox = GeoShapeMapper.resizeBoundingBox(boundingBox, 0, 0, 0, 0);

        assertThat(resizedBoundingBox, is(nullValue()));
    }

    /**
     * When bbox is shrunken then return shrunken bbox.
     */
    @Test
    void whenBboxIsShrunken_thenReturnShrunkenBbox() {
        double w = 0.0010;
        double s = 0.005;
        double e = -0.005;
        double n = -0.010;
        BoundingBox originalBoundingBox = new BoundingBox(new GeoPoint(50, 50), new GeoPoint(100, 10));

        BoundingBox resizedBoundingBox = GeoShapeMapper.resizeBoundingBox(originalBoundingBox, w, s, e, n);

        assertThat(resizedBoundingBox, is(notNullValue()));
        assertThat(resizedBoundingBox.getLeftTopPoint().getLatitude(),
                equalTo(originalBoundingBox.getLeftTopPoint().getLatitude() + n));
        assertThat(resizedBoundingBox.getLeftTopPoint().getLongitude(),
                equalTo(originalBoundingBox.getLeftTopPoint().getLongitude() + w));
        assertThat(resizedBoundingBox.getRightBottomPoint().getLatitude(),
                equalTo(originalBoundingBox.getRightBottomPoint().getLatitude() + s));
        assertThat(resizedBoundingBox.getRightBottomPoint().getLongitude(),
                equalTo(originalBoundingBox.getRightBottomPoint().getLongitude() + e));
    }

    /**
     * When bbox is expanded then return expanded bbox.
     */
    @Test
    void whenBboxIsExpanded_thenReturnExpandedBbox() {
        double w = -0.0010;
        double s = -0.005;
        double e = 0.005;
        double n = 0.010;
        BoundingBox originalBoundingBox = new BoundingBox(new GeoPoint(50, 50), new GeoPoint(100, 10));

        BoundingBox resizedBoundingBox = GeoShapeMapper.resizeBoundingBox(originalBoundingBox, w, s, e, n);

        assertThat(resizedBoundingBox, is(notNullValue()));
        assertThat(resizedBoundingBox.getLeftTopPoint().getLatitude(),
                equalTo(originalBoundingBox.getLeftTopPoint().getLatitude() + n));
        assertThat(resizedBoundingBox.getLeftTopPoint().getLongitude(),
                equalTo(originalBoundingBox.getLeftTopPoint().getLongitude() + w));
        assertThat(resizedBoundingBox.getRightBottomPoint().getLatitude(),
                equalTo(originalBoundingBox.getRightBottomPoint().getLatitude() + s));
        assertThat(resizedBoundingBox.getRightBottomPoint().getLongitude(),
                equalTo(originalBoundingBox.getRightBottomPoint().getLongitude() + e));
    }

}
