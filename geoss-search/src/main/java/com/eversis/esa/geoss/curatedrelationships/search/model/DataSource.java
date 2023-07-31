package com.eversis.esa.geoss.curatedrelationships.search.model;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The enum Data source.
 */
public enum DataSource {

    /**
     * Dab data source.
     */
    DAB("dab"),
    /**
     * Geoss cr data source.
     */
    GEOSS_CR("geoss_cr"),
    /**
     * Amerigeoss ckan data source.
     */
    AMERIGEOSS_CKAN("amerigeoss_ckan"),
    /**
     * Wikipedia data source.
     */
    WIKIPEDIA("wikipedia"),
    /**
     * Next geoss data source.
     */
    NEXT_GEOSS("nextgeoss"),
    /**
     * Zenodo data source.
     */
    ZENODO("zenodo");

    private String name;

    DataSource(String name) {
        this.name = name;
    }

    /**
     * From string data source.
     *
     * @param text the text
     * @return the data source
     */
    public static DataSource fromString(String text) {
        for (DataSource b : DataSource.values()) {
            if (b.getName().equalsIgnoreCase(text)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Provided dataSource: " + text + " is not recognized");
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    @JsonValue
    public String getName() {
        return name;
    }

}
