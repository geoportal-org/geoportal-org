package com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.query;

import com.eversis.esa.geoss.curatedrelationships.search.model.entity.BoundingBox;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.GeoPoint;
import com.eversis.esa.geoss.curatedrelationships.search.utils.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Bounding box mapper.
 */
public class BoundingBoxMapper {

    private BoundingBoxMapper() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Creates bounding box which consists of 2 points: left upper corner, bottom right corner.
     *
     * @param bbox Coordinates in format: W,S,E,N
     * @return the bounding box
     */
    public static BoundingBox mapFromString(String bbox) {
        if (StringUtils.isBlank(bbox)) {
            return null;
        }
        List<Double> coordinates = Arrays.stream(bbox.split("\\s*,\\s*")).map(Double::parseDouble)
                .collect(Collectors.toList());
        if (coordinates.size() != 4) {
            throw new IllegalArgumentException("Bounding box requires 4 coordinates");
        }
        if (latitudeIsNotInRange(coordinates)) {
            throw new IllegalArgumentException("Latitude values must be in range [-90;90]");
        }
        if (longitudeIsNotInRange(coordinates)) {
            throw new IllegalArgumentException("Longitude values must be in range [-180;180]");
        }
        return new BoundingBox(new GeoPoint(coordinates.get(0), coordinates.get(3)),
                new GeoPoint(coordinates.get(2), coordinates.get(1)));
    }

    private static boolean longitudeIsNotInRange(List<Double> convertedToNumbers) {
        return Double.compare(convertedToNumbers.get(0), -180.0) < 0
               || Double.compare(convertedToNumbers.get(0), 180.0) > 0
               || Double.compare(convertedToNumbers.get(2), -180.0) < 0
               || Double.compare(convertedToNumbers.get(2), 180.0) > 0;
    }

    private static boolean latitudeIsNotInRange(List<Double> convertedToNumbers) {
        return Double.compare(convertedToNumbers.get(1), -90.0) < 0
               || Double.compare(convertedToNumbers.get(1), 90.0) > 0
               || Double.compare(convertedToNumbers.get(3), -90.0) < 0
               || Double.compare(convertedToNumbers.get(3), 90.0) > 0;
    }

}
