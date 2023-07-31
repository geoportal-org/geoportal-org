package com.eversis.esa.geoss.curatedrelationships.search.repository;

import com.eversis.esa.geoss.curatedrelationships.search.model.DataSource;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.Page;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.Pageable;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.Extension;

import java.util.Set;
import javax.validation.constraints.NotNull;

public interface ExtensionRepository {

    /**
     * Find extensions for provided collection of resource's identifiers.
     *
     * @param ids set of entry's ids filter parameters
     * @param dataSource source which should
     * @param pageable pagination information
     */
    Page<Extension> findExtensions(@NotNull Set<String> ids, @NotNull DataSource dataSource, @NotNull Pageable pageable);

}
