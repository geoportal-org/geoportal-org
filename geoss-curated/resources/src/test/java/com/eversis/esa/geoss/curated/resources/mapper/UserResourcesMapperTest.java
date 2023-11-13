package com.eversis.esa.geoss.curated.resources.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eversis.esa.geoss.curated.common.domain.DataSource;
import com.eversis.esa.geoss.curated.common.domain.Status;
import com.eversis.esa.geoss.curated.common.domain.TaskType;
import com.eversis.esa.geoss.curated.common.domain.Type;
import com.eversis.esa.geoss.curated.common.model.DataSourceModel;
import com.eversis.esa.geoss.curated.common.model.TypeModel;
import com.eversis.esa.geoss.curated.resources.domain.AccessPolicy;
import com.eversis.esa.geoss.curated.resources.domain.DashboardContents;
import com.eversis.esa.geoss.curated.resources.domain.DefinitionType;
import com.eversis.esa.geoss.curated.resources.domain.Entry;
import com.eversis.esa.geoss.curated.resources.domain.Organisation;
import com.eversis.esa.geoss.curated.resources.domain.Source;
import com.eversis.esa.geoss.curated.resources.domain.UserResource;
import com.eversis.esa.geoss.curated.resources.model.AccessPolicyModel;
import com.eversis.esa.geoss.curated.resources.model.DashboardContentsModel;
import com.eversis.esa.geoss.curated.resources.model.DefinitionTypeModel;
import com.eversis.esa.geoss.curated.resources.model.EntryModel;
import com.eversis.esa.geoss.curated.resources.model.OrganisationModel;
import com.eversis.esa.geoss.curated.resources.model.SourceModel;
import com.eversis.esa.geoss.curated.resources.model.UserResourceModel;
import com.eversis.esa.geoss.curated.resources.service.EntryService;

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
 * The type User resources mapper test.
 */
@ContextConfiguration(classes = {UserResourcesMapper.class})
@ExtendWith(SpringExtension.class)
class UserResourcesMapperTest {

    @MockBean
    private EntryService entryService;

    @Autowired
    private UserResourcesMapper userResourcesMapper;

    /**
     * Method under test: {@link UserResourcesMapper#mapToUserResource(UserResourceModel)}
     */
    @Test
    void testMapToUserResource() {
        AccessPolicy accessPolicy = new AccessPolicy();
        accessPolicy.setCode("Code");
        accessPolicy.setId(1L);
        accessPolicy.setIsCustom(1);
        accessPolicy.setName("Name");

        DashboardContents dashboardContents = new DashboardContents();
        dashboardContents.setContent("Not all who wander are lost");
        dashboardContents.setId(1L);

        DataSource dataSource = new DataSource();
        dataSource.setCode("Code");
        dataSource.setId(1L);
        dataSource.setIsCustom(1);
        dataSource.setName("Name");

        DefinitionType definitionType = new DefinitionType();
        definitionType.setCode(1);
        definitionType.setId(1L);
        definitionType.setName("Name");

        DataSource displayDataSource = new DataSource();
        displayDataSource.setCode("Code");
        displayDataSource.setId(1L);
        displayDataSource.setIsCustom(1);
        displayDataSource.setName("Name");

        Organisation organisation = new Organisation();
        organisation.setContact("Contact");
        organisation.setContactEmail("jane.doe@example.org");
        organisation.setEmail("jane.doe@example.org");
        organisation.setId(1L);
        organisation.setIsCustom(1);
        organisation.setTitle("Dr");

        Source source = new Source();
        source.setCode("Code");
        source.setId(1L);
        source.setIsCustom(1);
        source.setTerm("Term");

        Type type = new Type();
        type.setCode("Code");
        type.setId(1L);
        type.setIsCustom(1);
        type.setName("Name");

        Entry entry = new Entry();
        entry.setAccessPolicy(accessPolicy);
        entry.setCode("Code");
        entry.setCoverage("Coverage");
        entry.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        entry.setDashboardContents(dashboardContents);
        entry.setDataSource(dataSource);
        entry.setDefinitionType(definitionType);
        entry.setDeleted(1);
        entry.setDisplayDataSource(displayDataSource);
        entry.setId(1L);
        entry.setKeywords("Keywords");
        entry.setLogo("Logo");
        entry.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        entry.setOrganisation(organisation);
        entry.setPublishDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        entry.setScoreWeight(10.0d);
        entry.setSource(source);
        entry.setSummary("Summary");
        entry.setTags("Tags");
        entry.setTitle("Dr");
        entry.setType(type);
        entry.setWorkflowInstanceId(1L);
        when(entryService.getOrCreateEntry(Mockito.<EntryModel>any())).thenReturn(entry);

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

        EntryModel entry2 = new EntryModel();
        entry2.setAccessPolicy(accessPolicy2);
        entry2.setCode("Code");
        entry2.setCoverage("Coverage");
        entry2.setDashboardContents(dashboardContents2);
        entry2.setDataSource(DataSourceModel.DAB);
        entry2.setDefinitionType(DefinitionTypeModel.PREDEFINED);
        entry2.setDisplayDataSource(DataSourceModel.DAB);
        entry2.setKeywords("Keywords");
        entry2.setLogo("Logo");
        entry2.setOrganisation(organisation2);
        entry2.setSource(source2);
        entry2.setSummary("Summary");
        entry2.setTags("Tags");
        entry2.setTitle("Dr");
        entry2.setTransferOptions(new ArrayList<>());
        entry2.setType(TypeModel.SERVICE);
        entry2.setUserId("42");

        UserResourceModel model = new UserResourceModel();
        model.setEntry(entry2);
        model.setEntryName("Entry Name");
        model.setTaskType(TaskType.CREATE);
        model.setUserId("42");
        UserResource actualMapToUserResourceResult = userResourcesMapper.mapToUserResource(model);
        verify(entryService).getOrCreateEntry(Mockito.<EntryModel>any());
        assertEquals("42", actualMapToUserResourceResult.getUserId());
        assertEquals("Entry Name", actualMapToUserResourceResult.getEntryName());
        assertEquals(Status.DRAFT, actualMapToUserResourceResult.getStatus());
        assertEquals(TaskType.CREATE, actualMapToUserResourceResult.getTaskType());
        assertSame(entry, actualMapToUserResourceResult.getEntry());
    }

    /**
     * Method under test: {@link UserResourcesMapper#mapToUserResource(UserResourceModel, UserResource)}
     */
    @Test
    void testMapToUserResource2() {
        AccessPolicy accessPolicy = new AccessPolicy();
        accessPolicy.setCode("Code");
        accessPolicy.setId(1L);
        accessPolicy.setIsCustom(1);
        accessPolicy.setName("Name");

        DashboardContents dashboardContents = new DashboardContents();
        dashboardContents.setContent("Not all who wander are lost");
        dashboardContents.setId(1L);

        DataSource dataSource = new DataSource();
        dataSource.setCode("Code");
        dataSource.setId(1L);
        dataSource.setIsCustom(1);
        dataSource.setName("Name");

        DefinitionType definitionType = new DefinitionType();
        definitionType.setCode(1);
        definitionType.setId(1L);
        definitionType.setName("Name");

        DataSource displayDataSource = new DataSource();
        displayDataSource.setCode("Code");
        displayDataSource.setId(1L);
        displayDataSource.setIsCustom(1);
        displayDataSource.setName("Name");

        Organisation organisation = new Organisation();
        organisation.setContact("Contact");
        organisation.setContactEmail("jane.doe@example.org");
        organisation.setEmail("jane.doe@example.org");
        organisation.setId(1L);
        organisation.setIsCustom(1);
        organisation.setTitle("Dr");

        Source source = new Source();
        source.setCode("Code");
        source.setId(1L);
        source.setIsCustom(1);
        source.setTerm("Term");

        Type type = new Type();
        type.setCode("Code");
        type.setId(1L);
        type.setIsCustom(1);
        type.setName("Name");

        Entry entry = new Entry();
        entry.setAccessPolicy(accessPolicy);
        entry.setCode("Code");
        entry.setCoverage("Coverage");
        entry.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        entry.setDashboardContents(dashboardContents);
        entry.setDataSource(dataSource);
        entry.setDefinitionType(definitionType);
        entry.setDeleted(1);
        entry.setDisplayDataSource(displayDataSource);
        entry.setId(1L);
        entry.setKeywords("Keywords");
        entry.setLogo("Logo");
        entry.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        entry.setOrganisation(organisation);
        entry.setPublishDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        entry.setScoreWeight(10.0d);
        entry.setSource(source);
        entry.setSummary("Summary");
        entry.setTags("Tags");
        entry.setTitle("Dr");
        entry.setType(type);
        entry.setWorkflowInstanceId(1L);
        when(entryService.getOrCreateEntry(Mockito.<EntryModel>any())).thenReturn(entry);

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

        EntryModel entry2 = new EntryModel();
        entry2.setAccessPolicy(accessPolicy2);
        entry2.setCode("Code");
        entry2.setCoverage("Coverage");
        entry2.setDashboardContents(dashboardContents2);
        entry2.setDataSource(DataSourceModel.DAB);
        entry2.setDefinitionType(DefinitionTypeModel.PREDEFINED);
        entry2.setDisplayDataSource(DataSourceModel.DAB);
        entry2.setKeywords("Keywords");
        entry2.setLogo("Logo");
        entry2.setOrganisation(organisation2);
        entry2.setSource(source2);
        entry2.setSummary("Summary");
        entry2.setTags("Tags");
        entry2.setTitle("Dr");
        entry2.setTransferOptions(new ArrayList<>());
        entry2.setType(TypeModel.SERVICE);
        entry2.setUserId("42");

        UserResourceModel model = new UserResourceModel();
        model.setEntry(entry2);
        model.setEntryName("Entry Name");
        model.setTaskType(TaskType.CREATE);
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

        DataSource displayDataSource2 = new DataSource();
        displayDataSource2.setCode("Code");
        displayDataSource2.setId(1L);
        displayDataSource2.setIsCustom(1);
        displayDataSource2.setName("Name");

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

        Entry entry3 = new Entry();
        entry3.setAccessPolicy(accessPolicy3);
        entry3.setCode("Code");
        entry3.setCoverage("Coverage");
        entry3.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        entry3.setDashboardContents(dashboardContents3);
        entry3.setDataSource(dataSource2);
        entry3.setDefinitionType(definitionType2);
        entry3.setDeleted(1);
        entry3.setDisplayDataSource(displayDataSource2);
        entry3.setId(1L);
        entry3.setKeywords("Keywords");
        entry3.setLogo("Logo");
        entry3.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        entry3.setOrganisation(organisation3);
        entry3.setPublishDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        entry3.setScoreWeight(10.0d);
        entry3.setSource(source3);
        entry3.setSummary("Summary");
        entry3.setTags("Tags");
        entry3.setTitle("Dr");
        entry3.setType(type2);
        entry3.setWorkflowInstanceId(1L);

        UserResource userResource = new UserResource();
        userResource.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userResource.setEntry(entry3);
        userResource.setEntryName("Entry Name");
        userResource.setId(1L);
        userResource.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userResource.setStatus(Status.DRAFT);
        userResource.setTaskType(TaskType.CREATE);
        userResource.setUserId("42");
        UserResource actualMapToUserResourceResult = userResourcesMapper.mapToUserResource(model, userResource);
        verify(entryService).getOrCreateEntry(Mockito.<EntryModel>any());
        assertEquals("42", actualMapToUserResourceResult.getUserId());
        assertEquals("Entry Name", actualMapToUserResourceResult.getEntryName());
        assertEquals(TaskType.CREATE, actualMapToUserResourceResult.getTaskType());
        assertSame(entry, actualMapToUserResourceResult.getEntry());
        assertSame(userResource, actualMapToUserResourceResult);
    }
}
