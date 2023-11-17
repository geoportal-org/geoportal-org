package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.repository;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.model.EntryEvent;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The interface Entry event repository.
 */
public interface EntryEventRepository extends JpaRepository<EntryEvent, Long> {

}
