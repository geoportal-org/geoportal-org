package com.eversis.esa.geoss.curated.extensions.repository;

import java.util.Set;

import com.eversis.esa.geoss.curated.extensions.domain.EntryExtension;
import com.eversis.esa.geoss.curated.extensions.domain.TransferOptionExtension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * The interface Transfer option extension repository.
 */
@RepositoryRestResource(exported = false)
public interface TransferOptionExtensionRepository extends JpaRepository<TransferOptionExtension, Long> {

    /**
     * Find by entry extension set.
     *
     * @param entryExtension the entry extension
     * @return the set
     */
    Set<TransferOptionExtension> findByEntryExtension(EntryExtension entryExtension);

    /**
     * Find by entry extension id set.
     *
     * @param entryExtensionId the entry extension id
     * @return the set
     */
    Set<TransferOptionExtension> findByEntryExtensionId(long entryExtensionId);

}
