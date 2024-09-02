package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.model;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.util.Identifiable;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.Table;

/**
 * The type Transfer option.
 */
@Getter
@Setter
@NamedEntityGraph(name = "graph.TransferOption.endpoint",
                  attributeNodes = @NamedAttributeNode("endpoint"))
@Entity
@Table(name = "transferoptions")
public class TransferOption extends Auditable implements Identifiable<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private String title;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "protocolid")
    private Protocol protocol;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "endpointid")
    private Endpoint endpoint;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "entryid")
    private Entry entry;

    /**
     * Hash code int.
     *
     * @return the int
     */
    @Override
    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(name);
        hcb.append(description);
        hcb.append(title);
        hcb.append(protocol);
        hcb.append(endpoint);
        hcb.append(entry);
        return hcb.toHashCode();
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
        if (!(obj instanceof TransferOption)) {
            return false;
        }
        TransferOption that = (TransferOption) obj;
        EqualsBuilder eb = new EqualsBuilder();
        eb.append(name, that.name);
        eb.append(description, that.description);
        eb.append(title, that.title);
        eb.append(protocol, that.protocol);
        eb.append(endpoint, that.endpoint);
        eb.append(entry, that.entry);
        return eb.isEquals();
    }

}
