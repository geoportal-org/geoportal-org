package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.repository;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.model.EntryRelation;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.model.EntryRelationId;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The interface Entry relation repository.
 */
public interface EntryRelationRepository extends JpaRepository<EntryRelation, EntryRelationId> {

}
