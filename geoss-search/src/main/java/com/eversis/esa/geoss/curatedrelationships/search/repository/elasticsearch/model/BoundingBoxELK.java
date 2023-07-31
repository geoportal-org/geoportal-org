package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import org.elasticsearch.ElasticsearchParseException;

import java.util.List;

@Getter
public class BoundingBoxELK {

    private static final String ENVELOPE_TYPE = "envelope";
    private List<GeoPointELK> coordinates;

    @JsonCreator
    public BoundingBoxELK(List<GeoPointELK> coordinates) {
        if (coordinates == null || coordinates.size() != 2) {
            throw new ElasticsearchParseException("Bounding box requires 2 geo points");
        }
        this.coordinates = coordinates;
    }

    public List<GeoPointELK> getCoordinates() {
        return coordinates;
    }

    public String getType() {
        return ENVELOPE_TYPE;
    }
}
