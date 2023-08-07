package com.eversis.esa.geoss.curatedrelationships.search.repository;

import com.eversis.esa.geoss.curatedrelationships.search.model.DataSource;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.Page;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.Pageable;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.Extension;

import jakarta.validation.constraints.NotNull;
import java.util.Set;

/**
 * The interface Extension repository.
 */
public interface ExtensionRepository {

    /**
     * Find extensions for provided collection of resource's identifiers.
     *
     * @param ids set of entry's ids filter parameters
     * @param dataSource source which should
     * @param pageable pagination information
     * @return the page
     */
    Page<Extension> findExtensions(@NotNull Set<String> ids, @NotNull DataSource dataSource,
            @NotNull Pageable pageable);

}
