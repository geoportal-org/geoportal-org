package com.eversis.esa.geoss.curated.resources.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eversis.esa.geoss.curated.common.domain.DataSource;
import com.eversis.esa.geoss.curated.common.domain.Type;
import com.eversis.esa.geoss.curated.common.model.DataSourceModel;
import com.eversis.esa.geoss.curated.common.model.TypeModel;
import com.eversis.esa.geoss.curated.common.service.DataSourceService;
import com.eversis.esa.geoss.curated.common.service.TypeService;
import com.eversis.esa.geoss.curated.resources.domain.AccessPolicy;
import com.eversis.esa.geoss.curated.resources.domain.DashboardContents;
import com.eversis.esa.geoss.curated.resources.domain.DefinitionType;
import com.eversis.esa.geoss.curated.resources.domain.Entry;
import com.eversis.esa.geoss.curated.resources.domain.Organisation;
import com.eversis.esa.geoss.curated.resources.domain.Source;
import com.eversis.esa.geoss.curated.resources.model.AccessPolicyModel;
import com.eversis.esa.geoss.curated.resources.model.DashboardContentsModel;
import com.eversis.esa.geoss.curated.resources.model.DefinitionTypeModel;
import com.eversis.esa.geoss.curated.resources.model.EntryModel;
import com.eversis.esa.geoss.curated.resources.model.OrganisationModel;
import com.eversis.esa.geoss.curated.resources.model.SourceModel;
import com.eversis.esa.geoss.curated.resources.service.AccessPolicyService;
import com.eversis.esa.geoss.curated.resources.service.DashboardContentsService;
import com.eversis.esa.geoss.curated.resources.service.DefinitionTypeService;
import com.eversis.esa.geoss.curated.resources.service.OrganisationService;
import com.eversis.esa.geoss.curated.resources.service.SourceService;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * The type Entry mapper test.
 */
@ContextConfiguration(classes = {EntryMapper.class})
@ExtendWith(SpringExtension.class)
class EntryMapperTest {

    @MockBean
    private AccessPolicyService accessPolicyService;

    @MockBean
    private DashboardContentsService dashboardContentsService;

    @MockBean
    private DataSourceService dataSourceService;

    @MockBean
    private DefinitionTypeService definitionTypeService;

    @Autowired
    private EntryMapper entryMapper;

    @MockBean
    private OrganisationService organisationService;

    @MockBean
    private SourceService sourceService;

    @MockBean
    private TypeService typeService;

    /**
     * Method under test: {@link EntryMapper#mapToEntry(EntryModel)}
     */
    @Test
    void testMapToEntry() {
        DataSource dataSource = new DataSource();
        dataSource.setCode("Code");
        dataSource.setId(1L);
        dataSource.setIsCustom(1);
        dataSource.setName("Name");
        when(dataSourceService.getOrCreateDataSource(Mockito.<DataSourceModel>any())).thenReturn(dataSource);

        Source source = new Source();
        source.setCode("Code");
        source.setId(1L);
        source.setIsCustom(1);
        source.setTerm("Term");
        when(sourceService.getOrCreateSource(Mockito.<SourceModel>any())).thenReturn(source);

        AccessPolicy accessPolicy = new AccessPolicy();
        accessPolicy.setCode("Code");
        accessPolicy.setId(1L);
        accessPolicy.setIsCustom(1);
        accessPolicy.setName("Name");
        when(accessPolicyService.getOrCreateAccessPolicy(Mockito.<AccessPolicyModel>any())).thenReturn(accessPolicy);

        Type type = new Type();
        type.setCode("Code");
        type.setId(1L);
        type.setIsCustom(1);
        type.setName("Name");
        when(typeService.getOrCreateType(Mockito.<TypeModel>any())).thenReturn(type);

        DefinitionType definitionType = new DefinitionType();
        definitionType.setCode(1);
        definitionType.setId(1L);
        definitionType.setName("Name");
        when(definitionTypeService.getOrCreateDefinitionType(Mockito.<DefinitionTypeModel>any()))
                .thenReturn(definitionType);

        Organisation organisation = new Organisation();
        organisation.setContact("Contact");
        organisation.setContactEmail("jane.doe@example.org");
        organisation.setEmail("jane.doe@example.org");
        organisation.setId(1L);
        organisation.setIsCustom(1);
        organisation.setTitle("Dr");
        when(organisationService.getOrCreateOrganisation(Mockito.<OrganisationModel>any())).thenReturn(organisation);

        DashboardContents dashboardContents = new DashboardContents();
        dashboardContents.setContent("Not all who wander are lost");
        dashboardContents.setId(1L);
        when(dashboardContentsService.getOrCreateDashboardContents(Mockito.<DashboardContentsModel>any()))
                .thenReturn(dashboardContents);

        AccessPolicyModel accessPolicy2 = new AccessPolicyModel();
        accessPolicy2.setCode("Code");
        accessPolicy2.setName("Name");

        DashboardContentsModel dashboardContents2 = new DashboardContentsModel();
        dashboardContents2.setContent("Not all who wander are lost");

        OrganisationModel organisation2 = new OrganisationModel();
        organisation2.setContact("Contact");
        organisation2.setContactEmail("jane.doe@example.org");
        organisation2.setEmail("jane.doe@example.org");
        organisation2.setTitle("Dr");

        SourceModel source2 = new SourceModel();
        source2.setCode("Code");
        source2.setTerm("Term");

        EntryModel model = new EntryModel();
        model.setAccessPolicy(accessPolicy2);
        model.setCode("Code");
        model.setCoverage("Coverage");
        model.setDashboardContents(dashboardContents2);
        model.setDataSource(DataSourceModel.DAB);
        model.setDefinitionType(DefinitionTypeModel.PREDEFINED);
        model.setDisplayDataSource(DataSourceModel.DAB);
        model.setKeywords("Keywords");
        model.setLogo("Logo");
        model.setOrganisation(organisation2);
        model.setSource(source2);
        model.setSummary("Summary");
        model.setTags("Tags");
        model.setTitle("Dr");
        model.setTransferOptions(new ArrayList<>());
        model.setType(TypeModel.SERVICE);
        model.setUserId("42");
        Entry actualMapToEntryResult = entryMapper.mapToEntry(model);
        verify(dataSourceService, atLeast(1)).getOrCreateDataSource(Mockito.<DataSourceModel>any());
        verify(typeService).getOrCreateType(Mockito.<TypeModel>any());
        verify(accessPolicyService).getOrCreateAccessPolicy(Mockito.<AccessPolicyModel>any());
        verify(dashboardContentsService).getOrCreateDashboardContents(Mockito.<DashboardContentsModel>any());
        verify(definitionTypeService).getOrCreateDefinitionType(Mockito.<DefinitionTypeModel>any());
        verify(organisationService).getOrCreateOrganisation(Mockito.<OrganisationModel>any());
        verify(sourceService).getOrCreateSource(Mockito.<SourceModel>any());
        assertEquals("Coverage", actualMapToEntryResult.getCoverage());
        assertEquals("Dr", actualMapToEntryResult.getTitle());
        assertEquals("Keywords", actualMapToEntryResult.getKeywords());
        assertEquals("Logo", actualMapToEntryResult.getLogo());
        assertEquals("Summary", actualMapToEntryResult.getSummary());
        assertEquals("Tags", actualMapToEntryResult.getTags());
        assertEquals(0, actualMapToEntryResult.getDeleted().intValue());
        assertEquals(1.0d, actualMapToEntryResult.getScoreWeight().doubleValue());
        DataSource displayDataSource = actualMapToEntryResult.getDisplayDataSource();
        assertSame(dataSource, displayDataSource);
        assertSame(type, actualMapToEntryResult.getType());
        assertSame(accessPolicy, actualMapToEntryResult.getAccessPolicy());
        assertSame(dashboardContents, actualMapToEntryResult.getDashboardContents());
        assertSame(definitionType, actualMapToEntryResult.getDefinitionType());
        assertSame(organisation, actualMapToEntryResult.getOrganisation());
        assertSame(source, actualMapToEntryResult.getSource());
        assertSame(displayDataSource, actualMapToEntryResult.getDataSource());
    }

    /**
     * Method under test: {@link EntryMapper#mapToEntry(EntryModel, Entry)}
     */
    @Test
    void testMapToEntry2() {
        DataSource dataSource = new DataSource();
        dataSource.setCode("Code");
        dataSource.setId(1L);
        dataSource.setIsCustom(1);
        dataSource.setName("Name");
        when(dataSourceService.getOrCreateDataSource(Mockito.<DataSourceModel>any())).thenReturn(dataSource);

        Source source = new Source();
        source.setCode("Code");
        source.setId(1L);
        source.setIsCustom(1);
        source.setTerm("Term");
        when(sourceService.getOrCreateSource(Mockito.<SourceModel>any())).thenReturn(source);

        AccessPolicy accessPolicy = new AccessPolicy();
        accessPolicy.setCode("Code");
        accessPolicy.setId(1L);
        accessPolicy.setIsCustom(1);
        accessPolicy.setName("Name");
        when(accessPolicyService.getOrCreateAccessPolicy(Mockito.<AccessPolicyModel>any())).thenReturn(accessPolicy);

        Type type = new Type();
        type.setCode("Code");
        type.setId(1L);
        type.setIsCustom(1);
        type.setName("Name");
        when(typeService.getOrCreateType(Mockito.<TypeModel>any())).thenReturn(type);

        DefinitionType definitionType = new DefinitionType();
        definitionType.setCode(1);
        definitionType.setId(1L);
        definitionType.setName("Name");
        when(definitionTypeService.getOrCreateDefinitionType(Mockito.<DefinitionTypeModel>any()))
                .thenReturn(definitionType);

        Organisation organisation = new Organisation();
        organisation.setContact("Contact");
        organisation.setContactEmail("jane.doe@example.org");
        organisation.setEmail("jane.doe@example.org");
        organisation.setId(1L);
        organisation.setIsCustom(1);
        organisation.setTitle("Dr");
        when(organisationService.getOrCreateOrganisation(Mockito.<OrganisationModel>any())).thenReturn(organisation);

        DashboardContents dashboardContents = new DashboardContents();
        dashboardContents.setContent("Not all who wander are lost");
        dashboardContents.setId(1L);
        when(dashboardContentsService.getOrCreateDashboardContents(Mockito.<DashboardContentsModel>any()))
                .thenReturn(dashboardContents);

        AccessPolicyModel accessPolicy2 = new AccessPolicyModel();
        accessPolicy2.setCode("Code");
        accessPolicy2.setName("Name");

        DashboardContentsModel dashboardContents2 = new DashboardContentsModel();
        dashboardContents2.setContent("Not all who wander are lost");

        OrganisationModel organisation2 = new OrganisationModel();
        organisation2.setContact("Contact");
        organisation2.setContactEmail("jane.doe@example.org");
        organisation2.setEmail("jane.doe@example.org");
        organisation2.setTitle("Dr");

        SourceModel source2 = new SourceModel();
        source2.setCode("Code");
        source2.setTerm("Term");

        EntryModel model = new EntryModel();
        model.setAccessPolicy(accessPolicy2);
        model.setCode("Code");
        model.setCoverage("Coverage");
        model.setDashboardContents(dashboardContents2);
        model.setDataSource(DataSourceModel.DAB);
        model.setDefinitionType(DefinitionTypeModel.PREDEFINED);
        model.setDisplayDataSource(DataSourceModel.DAB);
        model.setKeywords("Keywords");
        model.setLogo("Logo");
        model.setOrganisation(organisation2);
        model.setSource(source2);
        model.setSummary("Summary");
        model.setTags("Tags");
        model.setTitle("Dr");
        model.setTransferOptions(new ArrayList<>());
        model.setType(TypeModel.SERVICE);
        model.setUserId("42");

        AccessPolicy accessPolicy3 = new AccessPolicy();
        accessPolicy3.setCode("Code");
        accessPolicy3.setId(1L);
        accessPolicy3.setIsCustom(1);
        accessPolicy3.setName("Name");

        DashboardContents dashboardContents3 = new DashboardContents();
        dashboardContents3.setContent("Not all who wander are lost");
        dashboardContents3.setId(1L);

        DataSource dataSource2 = new DataSource();
        dataSource2.setCode("Code");
        dataSource2.setId(1L);
        dataSource2.setIsCustom(1);
        dataSource2.setName("Name");

        DefinitionType definitionType2 = new DefinitionType();
        definitionType2.setCode(1);
        definitionType2.setId(1L);
        definitionType2.setName("Name");

        DataSource displayDataSource = new DataSource();
        displayDataSource.setCode("Code");
        displayDataSource.setId(1L);
        displayDataSource.setIsCustom(1);
        displayDataSource.setName("Name");

        Organisation organisation3 = new Organisation();
        organisation3.setContact("Contact");
        organisation3.setContactEmail("jane.doe@example.org");
        organisation3.setEmail("jane.doe@example.org");
        organisation3.setId(1L);
        organisation3.setIsCustom(1);
        organisation3.setTitle("Dr");

        Source source3 = new Source();
        source3.setCode("Code");
        source3.setId(1L);
        source3.setIsCustom(1);
        source3.setTerm("Term");

        Type type2 = new Type();
        type2.setCode("Code");
        type2.setId(1L);
        type2.setIsCustom(1);
        type2.setName("Name");

        Entry entry = new Entry();
        entry.setAccessPolicy(accessPolicy3);
        entry.setCode("Code");
        entry.setCoverage("Coverage");
        entry.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        entry.setDashboardContents(dashboardContents3);
        entry.setDataSource(dataSource2);
        entry.setDefinitionType(definitionType2);
        entry.setDeleted(1);
        entry.setDisplayDataSource(displayDataSource);
        entry.setId(1L);
        entry.setKeywords("Keywords");
        entry.setLogo("Logo");
        entry.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        entry.setOrganisation(organisation3);
        entry.setPublishDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        entry.setScoreWeight(10.0d);
        entry.setSource(source3);
        entry.setSummary("Summary");
        entry.setTags("Tags");
        entry.setTitle("Dr");
        entry.setType(type2);
        entry.setWorkflowInstanceId(1L);
        Entry actualMapToEntryResult = entryMapper.mapToEntry(model, entry);
        verify(dataSourceService, atLeast(1)).getOrCreateDataSource(Mockito.<DataSourceModel>any());
        verify(typeService).getOrCreateType(Mockito.<TypeModel>any());
        verify(accessPolicyService).getOrCreateAccessPolicy(Mockito.<AccessPolicyModel>any());
        verify(dashboardContentsService).getOrCreateDashboardContents(Mockito.<DashboardContentsModel>any());
        verify(definitionTypeService).getOrCreateDefinitionType(Mockito.<DefinitionTypeModel>any());
        verify(organisationService).getOrCreateOrganisation(Mockito.<OrganisationModel>any());
        verify(sourceService).getOrCreateSource(Mockito.<SourceModel>any());
        assertEquals("Coverage", actualMapToEntryResult.getCoverage());
        assertEquals("Dr", actualMapToEntryResult.getTitle());
        assertEquals("Keywords", actualMapToEntryResult.getKeywords());
        assertEquals("Logo", actualMapToEntryResult.getLogo());
        assertEquals("Summary", actualMapToEntryResult.getSummary());
        assertEquals("Tags", actualMapToEntryResult.getTags());
        DataSource displayDataSource2 = actualMapToEntryResult.getDisplayDataSource();
        assertSame(dataSource, displayDataSource2);
        assertSame(type, actualMapToEntryResult.getType());
        assertSame(accessPolicy, actualMapToEntryResult.getAccessPolicy());
        assertSame(dashboardContents, actualMapToEntryResult.getDashboardContents());
        assertSame(definitionType, actualMapToEntryResult.getDefinitionType());
        assertSame(entry, actualMapToEntryResult);
        assertSame(organisation, actualMapToEntryResult.getOrganisation());
        assertSame(source, actualMapToEntryResult.getSource());
        assertSame(displayDataSource2, actualMapToEntryResult.getDataSource());
    }
}
