package com.eversis.esa.geoss.curated.resources.service;

import com.eversis.esa.geoss.curated.resources.domain.Type;
import com.eversis.esa.geoss.curated.resources.model.TypeModel;

/**
 * The interface Type service.
 */
public interface TypeService {

    /**
     * Gets or create type.
     *
     * @param type the type
     * @return the or create type
     */
    Type getOrCreateType(TypeModel type);

}
