package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.mapper.impl;

import com.eversis.esa.geoss.curatedrelationships.search.model.entity.BoundingBox;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.GeoPoint;
import com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.model.BoundingBoxELK;
import com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.model.GeoPointELK;

import org.elasticsearch.geometry.Geometry;
import org.elasticsearch.geometry.Rectangle;

/**
 * The type Geo shape mapper.
 */
public class GeoShapeMapper {

    private GeoShapeMapper() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Resize bounding box bounding box.
     *
     * @param boundingBox the bounding box
     * @param w the w
     * @param s the s
     * @param e the e
     * @param n the n
     * @return the bounding box
     */
    public static BoundingBox resizeBoundingBox(BoundingBox boundingBox, double w, double s, double e, double n) {
        if (boundingBox != null) {
            GeoPoint originalLeftTopPoint = boundingBox.getLeftTopPoint();
            GeoPoint originalRightBottomPoint = boundingBox.getRightBottomPoint();
            if (originalLeftTopPoint != null && originalRightBottomPoint != null) {
                GeoPoint newLeftTopPoint = new GeoPoint(originalLeftTopPoint.getLongitude() + w,
                        originalLeftTopPoint.getLatitude() + n);
                GeoPoint newRightTopPoint = new GeoPoint(originalRightBottomPoint.getLongitude() + e,
                        originalRightBottomPoint.getLatitude() + s);

                return new BoundingBox(newLeftTopPoint, newRightTopPoint);
            }
        }

        return null;
    }

    /**
     * Map bounding box elk bounding box.
     *
     * @param boundingBoxELK the bounding box elk
     * @return the bounding box
     */
    public static BoundingBox mapBoundingBoxELK(BoundingBoxELK boundingBoxELK) {
        if (boundingBoxELK != null && boundingBoxELK.getCoordinates() != null
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

    /**
     * Map geometry from bounding box envelope builder.
     *
     * @param boundingBox the bounding box
     * @return the envelope builder
     */
    public static Geometry mapGeometryFromBoundingBox(BoundingBox boundingBox) {
        GeoPoint leftTopPoint = boundingBox.getLeftTopPoint();
        GeoPoint rightBottomPoint = boundingBox.getRightBottomPoint();
        return new Rectangle(
                leftTopPoint.getLongitude(),
                rightBottomPoint.getLongitude(),
                leftTopPoint.getLatitude(),
                rightBottomPoint.getLatitude()
        );
    }
}
