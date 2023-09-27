package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.model;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.util.Identifiable;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * The type Entry.
 */
@Getter
@Setter
@Entity
@Table(name = "entry")
public class Entry extends Auditable implements Identifiable<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native")
    private Integer id;

    @Column
    private String title;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column
    private String logo;

    @Column
    private String coverage;

    @Column
    private String keywords;

    @Column
    private String tags;

    @NaturalId
    @Column(unique = true)
    private String code;

    @Column(name = "scoreweight")
    private BigDecimal scoreWeight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "typeid")
    private Type type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "definitiontypeid")
    private DefinitionType definitionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sourceid")
    private Source source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accesspolicyid")
    private AccessPolicy accessPolicy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organisationid")
    private Organisation organisation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "datasourceid")
    private DataSource dataSource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "displaydatasourceid")
    private DataSource displayDataSource;

    /**
     * Instantiates a new Entry.
     *
     * @param code the code
     */
    public Entry(String code) {
        this.code = code;
    }

    protected Entry() {
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

    /**
     * To string string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "Entry{"
                + "id=" + id
                + ", title='" + title + '\''
                + ", code='" + code + '\''
                + '}';
    }
}
