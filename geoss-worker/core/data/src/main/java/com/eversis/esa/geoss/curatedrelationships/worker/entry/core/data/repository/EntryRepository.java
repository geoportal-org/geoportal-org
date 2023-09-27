package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.repository;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.model.Entry;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * The interface Entry repository.
 */
public interface EntryRepository extends JpaRepository<Entry, Integer> {

    /**
     * Find by code optional.
     *
     * @param code the code
     * @return the optional
     */
    Optional<Entry> findByCode(String code);

}
