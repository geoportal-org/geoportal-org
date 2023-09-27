package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.repository;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.model.RelationType;

import org.hibernate.jpa.HibernateHints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

import jakarta.persistence.QueryHint;
import java.util.Optional;

/**
 * The interface Relation type repository.
 */
public interface RelationTypeRepository extends JpaRepository<RelationType, Integer> {

    /**
     * Find by code optional.
     *
     * @param name the name
     * @return the optional
     */
    @QueryHints(@QueryHint(name = HibernateHints.HINT_CACHEABLE, value = "true"))
    Optional<RelationType> findByCode(String name);

}
