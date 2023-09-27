package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.service;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.model.Organisation;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.repository.OrganisationRepository;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.service.mapper.OrganisationMapper;

import org.springframework.stereotype.Service;

/**
 * The type Organisation service.
 */
@Service
public class OrganisationService {

    private final OrganisationRepository organisationRepository;

    /**
     * Instantiates a new Organisation service.
     *
     * @param organisationRepository the organisation repository
     */
    public OrganisationService(OrganisationRepository organisationRepository) {
        this.organisationRepository = organisationRepository;
    }

    /**
     * Gets or create organisation.
     *
     * @param organisation the organisation
     * @return the or create organisation
     */
    public Organisation getOrCreateOrganisation(
            com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Organisation organisation) {
        return organisationRepository.findByTitle(organisation.getTitle())
                .orElseGet(() -> organisationRepository.save(OrganisationMapper.mapOrganisation(organisation)));
    }

}
