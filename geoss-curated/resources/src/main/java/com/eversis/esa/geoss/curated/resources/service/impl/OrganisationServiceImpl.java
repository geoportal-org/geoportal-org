package com.eversis.esa.geoss.curated.resources.service.impl;

import com.eversis.esa.geoss.curated.resources.domain.Organisation;
import com.eversis.esa.geoss.curated.resources.mapper.OrganisationMapper;
import com.eversis.esa.geoss.curated.resources.model.OrganisationModel;
import com.eversis.esa.geoss.curated.resources.repository.OrganisationRepository;
import com.eversis.esa.geoss.curated.resources.service.OrganisationService;

import org.springframework.stereotype.Service;

/**
 * The type Organisation service.
 */
@Service
public class OrganisationServiceImpl implements OrganisationService {

    private final OrganisationRepository organisationRepository;

    /**
     * Instantiates a new Organisation service.
     *
     * @param organisationRepository the organisation repository
     */
    public OrganisationServiceImpl(OrganisationRepository organisationRepository) {
        this.organisationRepository = organisationRepository;
    }

    @Override
    public Organisation getOrCreateOrganisation(OrganisationModel organisation) {
        return organisationRepository.findByTitle(organisation.getTitle())
                .orElseGet(() -> organisationRepository.save(OrganisationMapper.mapOrganisation(organisation)));
    }

}
