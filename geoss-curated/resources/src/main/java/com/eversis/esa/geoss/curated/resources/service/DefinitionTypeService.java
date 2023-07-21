package com.eversis.esa.geoss.curated.resources.service;

import com.eversis.esa.geoss.curated.resources.domain.DefinitionType;
import com.eversis.esa.geoss.curated.resources.model.DefinitionTypeModel;

/**
 * The interface Definition type service.
 */
public interface DefinitionTypeService {

    /**
     * Gets or create definition type.
     *
     * @param definitionType the definition type
     * @return the or create definition type
     */
    DefinitionType getOrCreateDefinitionType(DefinitionTypeModel definitionType);

}
