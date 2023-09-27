package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.model;

import lombok.Getter;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * The type Entry relation id.
 */
@Getter
@Embeddable
public class EntryRelationId implements Serializable {

    @Column(name = "srcid")
    private String srcId;

    @Column(name = "srcdatasourceid")
    private Integer srcDataSourceId;

    @Column(name = "destid")
    private String destId;

    @Column(name = "destdatasourceid")
    private Integer destDataSourceId;

    @Column(name = "relationtypeid")
    private Integer relationTypeId;

    protected EntryRelationId() {
    }

    /**
     * Instantiates a new Entry relation id.
     *
     * @param srcId the src id
     * @param srcDataSourceId the src data source id
     * @param destId the dest id
     * @param destDataSourceId the dest data source id
     * @param relationTypeId the relation type id
     */
    public EntryRelationId(String srcId, Integer srcDataSourceId, String destId, Integer destDataSourceId,
            Integer relationTypeId) {
        this.srcId = srcId;
        this.srcDataSourceId = srcDataSourceId;
        this.destId = destId;
        this.destDataSourceId = destDataSourceId;
        this.relationTypeId = relationTypeId;
    }

    /**
     * Equals boolean.
     *
     * @param o the o
     * @return the boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EntryRelationId)) {
            return false;
        }
        EntryRelationId that = (EntryRelationId) o;
        return Objects.equals(getSrcId(), that.getSrcId())
               && Objects.equals(getSrcDataSourceId(), that.getSrcDataSourceId())
               && Objects.equals(getDestId(), that.getDestId())
               && Objects.equals(getDestDataSourceId(), that.getDestDataSourceId())
               && Objects.equals(getRelationTypeId(), that.getRelationTypeId());
    }

    /**
     * Hash code int.
     *
     * @return the int
     */
    @Override
    public int hashCode() {
        return Objects.hash(getSrcId(), getSrcDataSourceId(), getDestId(), getDestDataSourceId(), getRelationTypeId());
    }
}
