package com.eversis.esa.geoss.curated.resources.elasticsearch.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

/**
 * The type Geo point elk.
 */
@Getter
public class GeoPointELK {

    private double latitude;

    private double longitude;

    /**
     * Instantiates a new Geo point elk.
     *
     * @param latitude the latitude
     * @param longitude the longitude
     */
    public GeoPointELK(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Instantiates a new Geo point elk.
     *
     * @param coordinates Elasticsearch stores coordinates in arrays in following order: longitude, latitude
     */
    @JsonCreator
    public GeoPointELK(List<Double> coordinates) {
        if (coordinates != null && coordinates.size() == 2) {
            this.longitude = coordinates.get(0);
            this.latitude = coordinates.get(1);
        }
    }

}
