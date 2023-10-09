package com.eversis.esa.geoss.curatedrelationships.thesaurusworker.model;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The enum Thesaurus type.
 */
public enum ThesaurusType {
    /**
     * Esa thesaurus type.
     */
    ESA,
    /**
     * Eosterm thesaurus type.
     */
    EOSTERM,
    /**
     * Earth thesaurus type.
     */
    EARTH;

    @JsonValue
    @Override
    public String toString() {
        return name().toLowerCase();
    }

    /**
     * From string thesaurus type.
     *
     * @param type the type
     * @return the thesaurus type
     */
    public static ThesaurusType fromString(String type) {
        return ThesaurusType.valueOf(type.toUpperCase());
    }
}
