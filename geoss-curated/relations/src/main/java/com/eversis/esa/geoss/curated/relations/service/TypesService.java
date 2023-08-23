package com.eversis.esa.geoss.curated.relations.service;

import com.eversis.esa.geoss.curated.relations.domain.Types;
import com.eversis.esa.geoss.curated.relations.model.TypeModel;

/**
 * The interface Type service.
 */
public interface TypesService {

    /**
     * Gets or create type.
     *
     * @param type the type
     * @return the or create type
     */
    Types getOrCreateType(TypeModel type);

}
