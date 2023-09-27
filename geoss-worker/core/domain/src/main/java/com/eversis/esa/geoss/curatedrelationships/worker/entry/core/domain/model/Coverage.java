package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model;

/**
 * The enum Coverage.
 */
public enum Coverage {

    /**
     * World coverage.
     */
    WORLD("[-180,90],[180,-90]");

    private final String coordinates;

    Coverage(String coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Gets coordinates.
     *
     * @return the coordinates
     */
    public String getCoordinates() {
        return coordinates;
    }
}
