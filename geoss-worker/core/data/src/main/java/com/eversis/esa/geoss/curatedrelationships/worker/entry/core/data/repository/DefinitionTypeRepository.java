package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.repository;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.model.DefinitionType;

import org.hibernate.jpa.HibernateHints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

import jakarta.persistence.QueryHint;
import java.util.Optional;

/**
 * The interface Definition type repository.
 */
public interface DefinitionTypeRepository extends JpaRepository<DefinitionType, Integer> {

    /**
     * Find by code optional.
     *
     * @param code the code
     * @return the optional
     */
    @QueryHints(@QueryHint(name = HibernateHints.HINT_CACHEABLE, value = "true"))
    Optional<DefinitionType> findByCode(int code);

}
