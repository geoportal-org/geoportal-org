package com.eversis.esa.geoss.curatedrelationships.search.service.extension;

import com.eversis.esa.geoss.curatedrelationships.search.model.DataSource;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.Extension;

import java.util.List;
import java.util.Set;
import javax.validation.constraints.NotNull;

public interface ExtensionSearchService {

    /**
     * Search for extensions for provided collection of resource's identifiers.
     *
     * @param ids set of entry's ids filter parameters
     * @param dataSource source which should
     */
    List<Extension> findExtensions(@NotNull Set<String> ids, @NotNull DataSource dataSource);

}
