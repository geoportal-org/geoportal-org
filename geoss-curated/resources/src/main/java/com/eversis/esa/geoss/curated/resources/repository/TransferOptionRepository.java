package com.eversis.esa.geoss.curated.resources.repository;

import java.util.Set;

import com.eversis.esa.geoss.curated.resources.domain.Entry;
import com.eversis.esa.geoss.curated.resources.domain.TransferOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * The interface Transfer option repository.
 */
@RepositoryRestResource(exported = false)
public interface TransferOptionRepository extends JpaRepository<TransferOption, Long> {

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
    Set<TransferOption> findByEntryId(long entryId);

}
