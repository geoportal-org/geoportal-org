package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.mapper.impl;

import com.eversis.esa.geoss.curatedrelationships.search.model.entity.BoundingBox;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.GeoPoint;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

public class GeoShapeMapperTests {

    @Test
    void whenBboxIsNull_thenResizedBboxIsNull() {
        BoundingBox boundingBox = null;

        BoundingBox resizedBoundingBox = GeoShapeMapper.resizeBoundingBox(boundingBox, 0, 0, 0, 0);

        assertThat(resizedBoundingBox, is(nullValue()));
    }

    @Test
    void whenBboxIsShrunken_thenReturnShrunkenBbox() {
        double w = 0.0010;
        double s = 0.005;
        double e = -0.005;
        double n = -0.010;
        BoundingBox originalBoundingBox = new BoundingBox(new GeoPoint(50, 50), new GeoPoint(100, 10));

        BoundingBox resizedBoundingBox = GeoShapeMapper.resizeBoundingBox(originalBoundingBox, w, s, e, n);

        assertThat(resizedBoundingBox, is(notNullValue()));
        assertThat(resizedBoundingBox.getLeftTopPoint().getLatitude(), equalTo(originalBoundingBox.getLeftTopPoint().getLatitude() + n));
        assertThat(resizedBoundingBox.getLeftTopPoint().getLongitude(), equalTo(originalBoundingBox.getLeftTopPoint().getLongitude() + w));
        assertThat(resizedBoundingBox.getRightBottomPoint().getLatitude(), equalTo(originalBoundingBox.getRightBottomPoint().getLatitude() + s));
        assertThat(resizedBoundingBox.getRightBottomPoint().getLongitude(), equalTo(originalBoundingBox.getRightBottomPoint().getLongitude() + e));
    }

    @Test
    void whenBboxIsExpanded_thenReturnExpandedBbox() {
        double w = -0.0010;
        double s = -0.005;
        double e = 0.005;
        double n = 0.010;
        BoundingBox originalBoundingBox = new BoundingBox(new GeoPoint(50, 50), new GeoPoint(100, 10));

        BoundingBox resizedBoundingBox = GeoShapeMapper.resizeBoundingBox(originalBoundingBox, w, s, e, n);

        assertThat(resizedBoundingBox, is(notNullValue()));
        assertThat(resizedBoundingBox.getLeftTopPoint().getLatitude(), equalTo(originalBoundingBox.getLeftTopPoint().getLatitude() + n));
        assertThat(resizedBoundingBox.getLeftTopPoint().getLongitude(), equalTo(originalBoundingBox.getLeftTopPoint().getLongitude() + w));
        assertThat(resizedBoundingBox.getRightBottomPoint().getLatitude(), equalTo(originalBoundingBox.getRightBottomPoint().getLatitude() + s));
        assertThat(resizedBoundingBox.getRightBottomPoint().getLongitude(), equalTo(originalBoundingBox.getRightBottomPoint().getLongitude() + e));
    }

}
