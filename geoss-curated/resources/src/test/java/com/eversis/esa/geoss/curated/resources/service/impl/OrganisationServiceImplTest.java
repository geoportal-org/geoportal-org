package com.eversis.esa.geoss.curated.resources.service.impl;

import com.eversis.esa.geoss.curated.resources.domain.Organisation;
import com.eversis.esa.geoss.curated.resources.model.OrganisationModel;
import com.eversis.esa.geoss.curated.resources.repository.OrganisationRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * The type Organisation service impl test.
 */
@ContextConfiguration(classes = {OrganisationServiceImpl.class})
@ExtendWith(SpringExtension.class)
class OrganisationServiceImplTest {

    @MockBean
    private OrganisationRepository organisationRepository;

    @Autowired
    private OrganisationServiceImpl organisationServiceImpl;

    /**
     * Test get or create organisation.
     */
    @Test
    void testGetOrCreateOrganisation() {
        Organisation organisation = new Organisation();
        organisation.setContact("");
        organisation.setContactEmail("jane.doe@example.org");
        organisation.setEmail("jane.doe@example.org");
        organisation.setId(18L);
        organisation.setIsCustom(0);
        organisation.setTitle("IspRA, Italian Environment Protection and Technical Services Agency - Monitoring Network");
        Optional<Organisation> ofResult = Optional.of(organisation);
        when(organisationRepository.findByTitle(Mockito.<String>any())).thenReturn(ofResult);

        OrganisationModel organisation2 = new OrganisationModel();
        organisation2.setContact("");
        organisation2.setContactEmail("jane.doe@example.org");
        organisation2.setEmail("jane.doe@example.org");
        organisation2.setTitle("IspRA, Italian Environment Protection and Technical Services Agency - Monitoring Network");
        Organisation actualOrCreateOrganisation = organisationServiceImpl.getOrCreateOrganisation(organisation2);
        verify(organisationRepository).findByTitle(Mockito.<String>any());
        assertSame(organisation, actualOrCreateOrganisation);
    }

    /**
     * Test get or create organisation 2.
     */
    @Test
    void testGetOrCreateOrganisation2() {
        Organisation organisation = new Organisation();
        organisation.setContact("");
        organisation.setContactEmail("jane.doe@example.org");
        organisation.setEmail("jane.doe@example.org");
        organisation.setId(17L);
        organisation.setIsCustom(10);
        organisation.setTitle("MINES ParisTech - ARMINES - Transvalor");
        when(organisationRepository.save(Mockito.<Organisation>any())).thenReturn(organisation);
        Optional<Organisation> emptyResult = Optional.empty();
        when(organisationRepository.findByTitle(Mockito.<String>any())).thenReturn(emptyResult);

        OrganisationModel organisation2 = new OrganisationModel();
        organisation2.setContact("");
        organisation2.setContactEmail("jane.doe@example.org");
        organisation2.setEmail("jane.doe@example.org");
        organisation2.setTitle("MINES ParisTech - ARMINES - Transvalor");
        Organisation actualOrCreateOrganisation = organisationServiceImpl.getOrCreateOrganisation(organisation2);
        verify(organisationRepository).findByTitle(Mockito.<String>any());
        verify(organisationRepository).save(Mockito.<Organisation>any());
        assertSame(organisation, actualOrCreateOrganisation);
    }
}
