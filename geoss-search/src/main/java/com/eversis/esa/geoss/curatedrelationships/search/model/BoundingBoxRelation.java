package com.eversis.esa.geoss.curatedrelationships.search.model;

/**
 * The enum Bounding box relation.
 */
public enum BoundingBoxRelation {
    /**
     * Contains bounding box relation.
     */
    CONTAINS,
    /**
     * Overlaps bounding box relation.
     */
    OVERLAPS,
    /**
     * Disjoint bounding box relation.
     */
    DISJOINT;

    /**
     * From string bounding box relation.
     *
     * @param text the text
     * @return the bounding box relation
     */
    public static BoundingBoxRelation fromString(String text) {
        for (BoundingBoxRelation b : BoundingBoxRelation.values()) {
            if (b.toString().equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}
