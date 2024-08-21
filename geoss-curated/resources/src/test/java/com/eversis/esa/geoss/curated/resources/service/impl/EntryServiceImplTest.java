package com.eversis.esa.geoss.curated.resources.service.impl;

import com.eversis.esa.geoss.curated.common.domain.DataSource;
import com.eversis.esa.geoss.curated.common.domain.Type;
import com.eversis.esa.geoss.curated.common.model.DataSourceModel;
import com.eversis.esa.geoss.curated.common.model.TypeModel;
import com.eversis.esa.geoss.curated.resources.domain.AccessPolicy;
import com.eversis.esa.geoss.curated.resources.domain.DashboardContents;
import com.eversis.esa.geoss.curated.resources.domain.DefinitionType;
import com.eversis.esa.geoss.curated.resources.domain.Entry;
import com.eversis.esa.geoss.curated.resources.domain.Organisation;
import com.eversis.esa.geoss.curated.resources.domain.Source;
import com.eversis.esa.geoss.curated.resources.mapper.EntryMapper;
import com.eversis.esa.geoss.curated.resources.model.AccessPolicyModel;
import com.eversis.esa.geoss.curated.resources.model.DashboardContentsModel;
import com.eversis.esa.geoss.curated.resources.model.DefinitionTypeModel;
import com.eversis.esa.geoss.curated.resources.model.EntryModel;
import com.eversis.esa.geoss.curated.resources.model.OrganisationModel;
import com.eversis.esa.geoss.curated.resources.model.SourceModel;
import com.eversis.esa.geoss.curated.resources.repository.EntryRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * The type Entry service impl test.
 */
@ContextConfiguration(classes = {EntryServiceImpl.class})
@ExtendWith(SpringExtension.class)
class EntryServiceImplTest {

    @MockBean
    private EntryMapper entryMapper;

    @MockBean
    private EntryRepository entryRepository;

    @Autowired
    private EntryServiceImpl entryServiceImpl;

    /**
     * Test get or create entry.
     */
    @Test
    void testGetOrCreateEntry() {
        AccessPolicy accessPolicy = new AccessPolicy();
        accessPolicy.setCode("open");
        accessPolicy.setId(1L);
        accessPolicy.setIsCustom(0);
        accessPolicy.setName("open");

        DashboardContents dashboardContents = new DashboardContents();
        dashboardContents.setContent("Not all who wander are lost");
        dashboardContents.setId(1L);

        DataSource dataSource = new DataSource();
        dataSource.setCode("wikipedia");
        dataSource.setId(2L);
        dataSource.setIsCustom(0);
        dataSource.setName("Wikipedia");

        DefinitionType definitionType = new DefinitionType();
        definitionType.setCode(0);
        definitionType.setId(1L);
        definitionType.setName("Predefined");

        DataSource displayDataSource = new DataSource();
        displayDataSource.setCode("wikipedia");
        displayDataSource.setId(2L);
        displayDataSource.setIsCustom(0);
        displayDataSource.setName("Wikipedia");

        Organisation organisation = new Organisation();
        organisation.setContact("");
        organisation.setContactEmail("jane.doe@example.org");
        organisation.setEmail("jane.doe@example.org");
        organisation.setId(17L);
        organisation.setIsCustom(10);
        organisation.setTitle("MINES ParisTech - ARMINES - Transvalor");

        Source source = new Source();
        source.setCode("wikipedia");
        source.setId(5L);
        source.setIsCustom(0);
        source.setTerm("Wikipedia");

        Type type = new Type();
        type.setCode("data_resource");
        type.setId(2L);
        type.setIsCustom(0);
        type.setName("Data");

        Entry entry = new Entry();
        entry.setAccessPolicy(accessPolicy);
        entry.setCode("sdg_7.3.1_indicator_19db1277-2450-49ec-96e2-5e335868b483_1699823100880");
        entry.setCoverage("[-180,90],[180,-90]");
        entry.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        entry.setDashboardContents(dashboardContents);
        entry.setDataSource(dataSource);
        entry.setDefinitionType(definitionType);
        entry.setDeleted(0);
        entry.setDisplayDataSource(displayDataSource);
        entry.setId(1L);
        entry.setKeywords("SDG Indicators,SDG");
        entry.setLogo("https://unstats.un.org/home/assets/img/logos/logo_StatisticsDivision.png");
        entry.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        entry.setOrganisation(organisation);
        entry.setPublishDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        entry.setScoreWeight(1.0d);
        entry.setSource(source);
        entry.setSummary("Energy intensity measured in terms of primary energy and GDP");
        entry.setTags("sustainable development goal observations,sdg indicators,observations,sdg observations,indicators");
        entry.setTitle("SDG 7.3.1 Indicator");
        entry.setType(type);
        entry.setWorkflowInstanceId(null);
        Optional<Entry> ofResult = Optional.of(entry);
        when(entryRepository.findByCode(Mockito.<String>any())).thenReturn(ofResult);

        AccessPolicyModel accessPolicy2 = new AccessPolicyModel();
        accessPolicy2.setCode("open");
        accessPolicy2.setName("open");

        DashboardContentsModel dashboardContents2 = new DashboardContentsModel();
        dashboardContents2.setContent("Not all who wander are lost");

        OrganisationModel organisation2 = new OrganisationModel();
        organisation2.setContact("");
        organisation2.setContactEmail("jane.doe@example.org");
        organisation2.setEmail("jane.doe@example.org");
        organisation2.setTitle("MINES ParisTech - ARMINES - Transvalor");

        SourceModel source2 = new SourceModel();
        source2.setCode("wikipedia");
        source2.setTerm("Wikipedia");

        EntryModel entry2 = new EntryModel();
        entry2.setAccessPolicy(accessPolicy2);
        entry2.setCode("sdg_7.3.1_indicator_19db1277-2450-49ec-96e2-5e335868b483_1699823100880");
        entry2.setCoverage("[-180,90],[180,-90]");
        entry2.setDashboardContents(dashboardContents2);
        entry2.setDataSource(DataSourceModel.WIKIPEDIA);
        entry2.setDefinitionType(DefinitionTypeModel.PREDEFINED);
        entry2.setDisplayDataSource(DataSourceModel.WIKIPEDIA);
        entry2.setKeywords("SDG Indicators,SDG");
        entry2.setLogo("https://unstats.un.org/home/assets/img/logos/logo_StatisticsDivision.png");
        entry2.setOrganisation(organisation2);
        entry2.setSource(source2);
        entry2.setSummary("Energy intensity measured in terms of primary energy and GDP");
        entry2.setTags("sustainable development goal observations,sdg indicators,observations,sdg observations,indicators");
        entry2.setTitle("SDG 7.3.1 Indicator");
        entry2.setTransferOptions(new ArrayList<>());
        entry2.setType(TypeModel.DATA);
        entry2.setUserId("19db1277-2450-49ec-96e2-5e335868b483");
        Entry actualOrCreateEntry = entryServiceImpl.getOrCreateEntry(entry2);
        verify(entryRepository).findByCode(Mockito.<String>any());
        assertSame(entry, actualOrCreateEntry);
    }
}
