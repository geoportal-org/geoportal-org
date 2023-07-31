package com.eversis.esa.geoss.curatedrelationships.search.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * The type Geo point.
 */
@Getter
@AllArgsConstructor
public class GeoPoint implements Serializable {

    private final double longitude;
    private final double latitude;

}
