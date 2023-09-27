package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.repository;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.model.Organisation;

import org.hibernate.jpa.HibernateHints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

import jakarta.persistence.QueryHint;
import java.util.Optional;

/**
 * The interface Organisation repository.
 */
public interface OrganisationRepository extends JpaRepository<Organisation, Integer> {

    /**
     * Find by title optional.
     *
     * @param title the title
     * @return the optional
     */
    @QueryHints(@QueryHint(name = HibernateHints.HINT_CACHEABLE, value = "true"))
    Optional<Organisation> findByTitle(String title);

}
