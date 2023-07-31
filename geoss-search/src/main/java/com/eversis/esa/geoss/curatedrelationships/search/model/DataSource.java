package com.eversis.esa.geoss.curatedrelationships.search.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum DataSource {

    DAB("dab"),
    GEOSS_CR("geoss_cr"),
    AMERIGEOSS_CKAN("amerigeoss_ckan"),
    WIKIPEDIA("wikipedia"),
    NEXT_GEOSS("nextgeoss"),
    ZENODO("zenodo");

    private String name;

    DataSource(String name) {
        this.name = name;
    }

    public static DataSource fromString(String text) {
        for (DataSource b : DataSource.values()) {
            if (b.getName().equalsIgnoreCase(text)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Provided dataSource: " + text + " is not recognized");
    }

    @JsonValue
    public String getName() {
        return name;
    }

}
