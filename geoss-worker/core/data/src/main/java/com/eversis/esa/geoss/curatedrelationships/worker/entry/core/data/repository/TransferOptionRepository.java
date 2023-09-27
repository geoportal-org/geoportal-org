package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.repository;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.model.Entry;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.model.TransferOption;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

/**
 * The interface Transfer option repository.
 */
public interface TransferOptionRepository extends JpaRepository<TransferOption, Integer> {

    /**
     * Find by entry set.
     *
     * @param entry the entry
     * @return the set
     */
    Set<TransferOption> findByEntry(Entry entry);

    /**
     * Find by entry id set.
     *
     * @param entryId the entry id
     * @return the set
     */
    @EntityGraph(value = "graph.TransferOption.endpoint", type = EntityGraphType.LOAD)
    Set<TransferOption> findByEntryId(int entryId);

}
