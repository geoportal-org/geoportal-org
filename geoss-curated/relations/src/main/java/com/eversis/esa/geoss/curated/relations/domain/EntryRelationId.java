package com.eversis.esa.geoss.curated.relations.domain;

import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

/**
 * The type Entry relation id.
 */
@Setter
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

    /**
     * Instantiates a new Entry relation id.
     */
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
    public EntryRelationId(String srcId, Integer srcDataSourceId, String destId, Integer destDataSourceId, Integer relationTypeId) {
        this.srcId = srcId;
        this.srcDataSourceId = srcDataSourceId;
        this.destId = destId;
        this.destDataSourceId = destDataSourceId;
        this.relationTypeId = relationTypeId;
    }

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

    @Override
    public int hashCode() {
        return Objects.hash(getSrcId(), getSrcDataSourceId(), getDestId(), getDestDataSourceId(), getRelationTypeId());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EntryRelationId{");
        sb.append("srcId='").append(srcId).append('\'');
        sb.append(", srcDataSourceId=").append(srcDataSourceId);
        sb.append(", destId='").append(destId).append('\'');
        sb.append(", destDataSourceId=").append(destDataSourceId);
        sb.append(", relationTypeId=").append(relationTypeId);
        sb.append('}');
        return sb.toString();
    }

}
