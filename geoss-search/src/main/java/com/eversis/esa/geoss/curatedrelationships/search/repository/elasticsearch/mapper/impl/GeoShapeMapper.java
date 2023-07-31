package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.mapper.impl;

import com.eversis.esa.geoss.curatedrelationships.search.model.entity.BoundingBox;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.GeoPoint;
import com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.model.BoundingBoxELK;
import com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.model.GeoPointELK;

import org.elasticsearch.common.geo.builders.EnvelopeBuilder;
import org.locationtech.jts.geom.Coordinate;

public class GeoShapeMapper {

    private GeoShapeMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static BoundingBox resizeBoundingBox(BoundingBox boundingBox, double w, double s, double e, double n) {
        if (boundingBox != null) {
            GeoPoint originalLeftTopPoint = boundingBox.getLeftTopPoint();
            GeoPoint originalRightBottomPoint = boundingBox.getRightBottomPoint();
            if (originalLeftTopPoint != null && originalRightBottomPoint != null) {
                GeoPoint newLeftTopPoint = new GeoPoint(originalLeftTopPoint.getLongitude() + w, originalLeftTopPoint.getLatitude() + n);
                GeoPoint newRightTopPoint = new GeoPoint(originalRightBottomPoint.getLongitude() + e, originalRightBottomPoint.getLatitude() + s);

                return new BoundingBox(newLeftTopPoint, newRightTopPoint);
            }
        }

        return null;
    }

    public static BoundingBox mapBoundingBoxELK(BoundingBoxELK boundingBoxELK) {
        if (boundingBoxELK != null
                && boundingBoxELK.getCoordinates() != null
                && boundingBoxELK.getCoordinates().size() == 2) {
            GeoPoint leftTopPoint = mapGeoPointELK(boundingBoxELK.getCoordinates().get(0));
            GeoPoint rightBottomPoint = mapGeoPointELK(boundingBoxELK.getCoordinates().get(1));
            if (leftTopPoint != null && rightBottomPoint != null) {
                return new BoundingBox(leftTopPoint, rightBottomPoint);
            }
        }

        return null;
    }

    private static GeoPoint mapGeoPointELK(GeoPointELK geoPointELK) {
        if (geoPointELK == null) {
            return null;
        }
        return new GeoPoint(geoPointELK.getLongitude(), geoPointELK.getLatitude());
    }

    public static EnvelopeBuilder mapEnvelopeFromBoundingBox(BoundingBox boundingBox) {
        return new EnvelopeBuilder(
                mapCoordinateFromGeoPoint(boundingBox.getLeftTopPoint()),
                mapCoordinateFromGeoPoint(boundingBox.getRightBottomPoint()));
    }

    private static Coordinate mapCoordinateFromGeoPoint(GeoPoint geoPoint) {
        if (geoPoint == null) {
            return new Coordinate();
        }
        return new Coordinate(geoPoint.getLongitude(), geoPoint.getLatitude());
    }

}
