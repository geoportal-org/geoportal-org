package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.service.mapper;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.model.Organisation;

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
    public static Organisation mapOrganisation(
            com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Organisation domainOrganisation) {
        Organisation dbOrganisation = new Organisation();
        dbOrganisation.setTitle(domainOrganisation.getTitle());
        dbOrganisation.setEmail(domainOrganisation.getEmail());
        dbOrganisation.setContact(domainOrganisation.getContact());
        dbOrganisation.setContactEmail(domainOrganisation.getContactEmail());
        return dbOrganisation;
    }

}
