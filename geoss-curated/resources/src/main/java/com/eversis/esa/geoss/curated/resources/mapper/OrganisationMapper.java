package com.eversis.esa.geoss.curated.resources.mapper;

import com.eversis.esa.geoss.curated.resources.domain.Organisation;
import com.eversis.esa.geoss.curated.resources.model.OrganisationModel;

/**
 * The type Organisation mapper.
 */
public class OrganisationMapper {

    private OrganisationMapper() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Map organisation organisation.
     *
     * @param domainOrganisation the domain organisation
     * @return the organisation
     */
    public static Organisation mapOrganisation(OrganisationModel domainOrganisation) {
        Organisation dbOrganisation = new Organisation();
        dbOrganisation.setTitle(domainOrganisation.getTitle());
        dbOrganisation.setEmail(domainOrganisation.getEmail());
        dbOrganisation.setContact(domainOrganisation.getContact());
        dbOrganisation.setContactEmail(domainOrganisation.getContactEmail());
        dbOrganisation.setIsCustom(1);
        return dbOrganisation;
    }

}
