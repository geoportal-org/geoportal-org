package com.eversis.esa.geoss.curatedrelationships.search.model;

public enum Facets {
    ORGANISATION("organisationName"),
    PROTOCOL("protocol"),
    KEYWORDS("keyword"),
    SOURCE("source"),
    FORMAT("format");

    private final String name;

    Facets(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
