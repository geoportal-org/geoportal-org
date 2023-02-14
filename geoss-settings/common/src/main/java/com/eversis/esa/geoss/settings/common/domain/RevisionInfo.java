package com.eversis.esa.geoss.settings.common.domain;

import com.eversis.esa.geoss.settings.common.event.RevisionInfoRevisionListener;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * The type Revision info.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "revinfo")
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "rev")),
        @AttributeOverride(name = "timestamp", column = @Column(name = "revtstmp"))

})
@RevisionEntity(RevisionInfoRevisionListener.class)
public class RevisionInfo extends DefaultRevisionEntity {

    @Column(nullable = false)
    private String username;
}
