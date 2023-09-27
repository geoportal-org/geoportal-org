package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.repository;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.model.Source;

import org.hibernate.jpa.HibernateHints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

import jakarta.persistence.QueryHint;
import java.util.Optional;

/**
 * The interface Source repository.
 */
public interface SourceRepository extends JpaRepository<Source, Integer> {

    /**
     * Find by code optional.
     *
     * @param sourceId the source id
     * @return the optional
     */
    @QueryHints(@QueryHint(name = HibernateHints.HINT_CACHEABLE, value = "true"))
    Optional<Source> findByCode(String sourceId);

}
