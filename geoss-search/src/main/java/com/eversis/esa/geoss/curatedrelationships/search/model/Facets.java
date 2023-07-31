package com.eversis.esa.geoss.curatedrelationships.search.model;

/**
 * The enum Facets.
 */
public enum Facets {
    /**
     * Organisation facets.
     */
    ORGANISATION("organisationName"),
    /**
     * Protocol facets.
     */
    PROTOCOL("protocol"),
    /**
     * Keywords facets.
     */
    KEYWORDS("keyword"),
    /**
     * Source facets.
     */
    SOURCE("source"),
    /**
     * Format facets.
     */
    FORMAT("format");

    private final String name;

    Facets(String name) {
        this.name = name;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }
}
