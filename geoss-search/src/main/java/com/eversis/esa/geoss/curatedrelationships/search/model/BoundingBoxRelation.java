package com.eversis.esa.geoss.curatedrelationships.search.model;

public enum BoundingBoxRelation {
    CONTAINS,
    OVERLAPS,
    DISJOINT;

    public static BoundingBoxRelation fromString(String text) {
        for (BoundingBoxRelation b : BoundingBoxRelation.values()) {
            if (b.toString().equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}
