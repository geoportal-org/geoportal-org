package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import org.elasticsearch.ElasticsearchParseException;

import java.util.List;

/**
 * The type Bounding box elk.
 */
@Getter
public class BoundingBoxELK {

    private static final String ENVELOPE_TYPE = "envelope";
    private List<GeoPointELK> coordinates;

    /**
     * Instantiates a new Bounding box elk.
     *
     * @param coordinates the coordinates
     */
    @JsonCreator
    public BoundingBoxELK(List<GeoPointELK> coordinates) {
        if (coordinates == null || coordinates.size() != 2) {
            throw new ElasticsearchParseException("Bounding box requires 2 geo points");
        }
        this.coordinates = coordinates;
    }

    /**
     * Gets coordinates.
     *
     * @return the coordinates
     */
    public List<GeoPointELK> getCoordinates() {
        return coordinates;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public String getType() {
        return ENVELOPE_TYPE;
    }
}
