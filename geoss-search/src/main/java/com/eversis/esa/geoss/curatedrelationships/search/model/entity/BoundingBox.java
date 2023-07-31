package com.eversis.esa.geoss.curatedrelationships.search.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class BoundingBox implements Serializable {

    private final GeoPoint leftTopPoint;
    private final GeoPoint rightBottomPoint;

}
