package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * The type Entry.
 */
@Getter
@Setter
@ToString
public class Entry {

    private final String code;
    private final String title;
    private final Type type;
    private final DefinitionType definitionType;
    private final DataSource dataSource;
    private final DataSource displayDataSource;
    private Double scoreWeight;
    private String summary;
    private String logo;
    private String coverage;
    private Source source;
    private AccessPolicy accessPolicy;
    private Organisation organisation;
    private Set<String> keywords = new HashSet<>();
    private Set<String> tags = new HashSet<>();
    private final List<TransferOption> transferOptions = new ArrayList<>();
    private final List<EntryRelation> relations = new ArrayList<>();

    /**
     * Instantiates a new Entry.
     *
     * @param code the code
     * @param title the title
     * @param type the type
     * @param definitionType the definition type
     * @param dataSource the data source
     * @param displayDataSource the display data source
     */
    public Entry(String code, String title, Type type, DefinitionType definitionType, DataSource dataSource,
            DataSource displayDataSource) {
        this.code = code;
        this.title = title;
        this.type = type;
        this.definitionType = definitionType;
        this.dataSource = dataSource;
        this.displayDataSource = displayDataSource;
    }

    /**
     * Add transfer option.
     *
     * @param transferOption the transfer option
     */
    public void addTransferOption(TransferOption transferOption) {
        transferOptions.add(transferOption);
        transferOption.setEntry(this);
    }

    /**
     * Remove transfer option.
     *
     * @param transferOption the transfer option
     */
    public void removeTransferOption(TransferOption transferOption) {
        transferOptions.remove(transferOption);
        transferOption.setEntry(null);
    }

    /**
     * Add relation.
     *
     * @param entryRelation the entry relation
     */
    public void addRelation(EntryRelation entryRelation) {
        relations.add(entryRelation);
    }

    /**
     * Remove relation.
     *
     * @param entryRelation the entry relation
     */
    public void removeRelation(EntryRelation entryRelation) {
        relations.remove(entryRelation);
    }

    /**
     * Hash code int.
     *
     * @return the int
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(code);
    }

    /**
     * Equals boolean.
     *
     * @param obj the obj
     * @return the boolean
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Entry other = (Entry) obj;
        return Objects.equals(code, other.getCode());
    }

}
