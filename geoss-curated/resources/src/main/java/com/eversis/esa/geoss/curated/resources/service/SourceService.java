package com.eversis.esa.geoss.curated.resources.service;

import com.eversis.esa.geoss.curated.resources.domain.Source;
import com.eversis.esa.geoss.curated.resources.model.SourceModel;

/**
 * The interface Source service.
 */
public interface SourceService {

    /**
     * Gets or create source.
     *
     * @param source the source
     * @return the or create source
     */
    Source getOrCreateSource(SourceModel source);

}
