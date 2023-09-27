package com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.esathesaurus.service.model;

/**
 * The enum Entry type.
 */
public enum EntryType {

    /**
     * Concept entry type.
     */
    CONCEPT("skos:Concept");

    private final String name;

    EntryType(String name) {
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

    /**
     * From string entry type.
     *
     * @param text the text
     * @return the entry type
     */
    public static EntryType fromString(String text) {
        for (EntryType type : EntryType.values()) {
            if (type.getName().equalsIgnoreCase(text)) {
                return type;
            }
        }
        return null;
    }
}
