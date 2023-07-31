package com.eversis.esa.geoss.curatedrelationships.search.model.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EntryType {

    DATA("data_resource"),
    SERVICE("service_resource"),
    INFORMATION("information_resource");

    @JsonValue
    private String name;

    EntryType(String name) {
        this.name = name;
    }

    @JsonCreator
    public static EntryType fromString(String text) {
        for (EntryType b : EntryType.values()) {
            if (b.getName().equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }
}
