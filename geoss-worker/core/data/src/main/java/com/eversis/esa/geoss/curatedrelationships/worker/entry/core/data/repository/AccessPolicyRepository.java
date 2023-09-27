package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.repository;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.model.AccessPolicy;

import org.hibernate.jpa.HibernateHints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

import jakarta.persistence.QueryHint;
import java.util.Optional;

/**
 * The interface Access policy repository.
 */
public interface AccessPolicyRepository extends JpaRepository<AccessPolicy, Integer> {

    /**
     * Find by code optional.
     *
     * @param value the value
     * @return the optional
     */
    @QueryHints(@QueryHint(name = HibernateHints.HINT_CACHEABLE, value = "true"))
    Optional<AccessPolicy> findByCode(String value);

}
