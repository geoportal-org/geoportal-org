package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model;

/**
 * The enum Data source.
 */
public enum DataSource {

    /**
     * The Dab.
     */
    DAB("dab", "GEO DAB"),
    /**
     * The Geoss curated.
     */
    GEOSS_CURATED("geoss_cr", "GEOSS Curated"),
    /**
     * Wikipedia data source.
     */
    WIKIPEDIA("wikipedia", "Wikipedia"),
    /**
     * Amerigeoss ckan data source.
     */
    AMERIGEOSS_CKAN("amerigeoss_ckan", "AmeriGEO"),
    /**
     * Next geoss data source.
     */
    NEXT_GEOSS("nextgeoss", "NextGEOSS"),
    /**
     * Zenodo data source.
     */
    ZENODO("zenodo", "Zenodo");

    private final String code;
    private final String name;

    DataSource(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public String getCode() {
        return code;
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
