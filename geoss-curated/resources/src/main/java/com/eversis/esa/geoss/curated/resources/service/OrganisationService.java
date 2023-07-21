package com.eversis.esa.geoss.curated.resources.service;

import com.eversis.esa.geoss.curated.resources.domain.Organisation;
import com.eversis.esa.geoss.curated.resources.model.OrganisationModel;

/**
 * The interface Organisation service.
 */
public interface OrganisationService {

    /**
     * Gets or create organisation.
     *
     * @param organisation the organisation
     * @return the or create organisation
     */
    Organisation getOrCreateOrganisation(OrganisationModel organisation);

}
