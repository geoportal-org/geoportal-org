package com.eversis.esa.geoss.curated.resources.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
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
import com.eversis.esa.geoss.curated.resources.mapper.UserResourcesMapper;
import com.eversis.esa.geoss.curated.resources.model.AccessPolicyModel;
import com.eversis.esa.geoss.curated.resources.model.DashboardContentsModel;
import com.eversis.esa.geoss.curated.resources.model.DefinitionTypeModel;
import com.eversis.esa.geoss.curated.resources.model.EntryModel;
import com.eversis.esa.geoss.curated.resources.model.OrganisationModel;
import com.eversis.esa.geoss.curated.resources.model.SourceModel;
import com.eversis.esa.geoss.curated.resources.model.TransferOptionModel;
import com.eversis.esa.geoss.curated.resources.model.UserResourceModel;
import com.eversis.esa.geoss.curated.resources.repository.UserResourceRepository;
import com.eversis.esa.geoss.curated.resources.service.TransferOptionService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * The type User resource service impl test.
 */
@ContextConfiguration(classes = {UserResourceServiceImpl.class})
@ExtendWith(SpringExtension.class)
class UserResourceServiceImplTest {

    @MockBean
    private TransferOptionService transferOptionService;

    @MockBean
    private UserResourceRepository userResourceRepository;

    @Autowired
    private UserResourceServiceImpl userResourceServiceImpl;

    @MockBean
    private UserResourcesMapper userResourcesMapper;

    /**
     * Method under test: {@link UserResourceServiceImpl#findAllUserResources(String, Pageable)}
     */
    @Test
    void testFindAllUserResources() {
        PageImpl<UserResource> pageImpl = new PageImpl<>(new ArrayList<>());
        when(userResourceRepository.findByUserId(Mockito.<String>any(), Mockito.<Pageable>any())).thenReturn(pageImpl);
        Page<UserResource> actualFindAllUserResourcesResult = userResourceServiceImpl.findAllUserResources("42", null);
        verify(userResourceRepository).findByUserId(Mockito.<String>any(), Mockito.<Pageable>any());
        assertTrue(actualFindAllUserResourcesResult.toList().isEmpty());
        assertSame(pageImpl, actualFindAllUserResourcesResult);
    }

    /**
     * Method under test: {@link UserResourceServiceImpl#findAllUserResources(String, Pageable)}
     */
    @Test
    void testFindAllUserResources2() {
        when(userResourceRepository.findByUserId(Mockito.<String>any(), Mockito.<Pageable>any()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));
        assertThrows(ResourceNotFoundException.class, () -> userResourceServiceImpl.findAllUserResources("42", null));
        verify(userResourceRepository).findByUserId(Mockito.<String>any(), Mockito.<Pageable>any());
    }

    /**
     * Method under test: {@link UserResourceServiceImpl#findAllUserResources(Pageable)}
     */
    @Test
    void testFindAllUserResources3() {
        PageImpl<UserResource> pageImpl = new PageImpl<>(new ArrayList<>());
        when(userResourceRepository.findAll(Mockito.<Pageable>any())).thenReturn(pageImpl);
        Page<UserResource> actualFindAllUserResourcesResult = userResourceServiceImpl.findAllUserResources(null);
        verify(userResourceRepository).findAll(Mockito.<Pageable>any());
        assertTrue(actualFindAllUserResourcesResult.toList().isEmpty());
        assertSame(pageImpl, actualFindAllUserResourcesResult);
    }

    /**
     * Method under test: {@link UserResourceServiceImpl#findAllUserResources(Pageable)}
     */
    @Test
    void testFindAllUserResources4() {
        when(userResourceRepository.findAll(Mockito.<Pageable>any()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));
        assertThrows(ResourceNotFoundException.class, () -> userResourceServiceImpl.findAllUserResources(null));
        verify(userResourceRepository).findAll(Mockito.<Pageable>any());
    }

    /**
     * Method under test: {@link UserResourceServiceImpl#findUserResource(long)}
     */
    @Test
    void testFindUserResource() {
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

        UserResource userResource = new UserResource();
        userResource.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userResource.setEntry(entry);
        userResource.setEntryName("Entry Name");
        userResource.setId(1L);
        userResource.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userResource.setStatus(Status.DRAFT);
        userResource.setTaskType(TaskType.CREATE);
        userResource.setUserId("42");
        Optional<UserResource> ofResult = Optional.of(userResource);
        when(userResourceRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        UserResource actualFindUserResourceResult = userResourceServiceImpl.findUserResource(1L);
        verify(userResourceRepository).findById(Mockito.<Long>any());
        assertSame(userResource, actualFindUserResourceResult);
    }

    /**
     * Method under test: {@link UserResourceServiceImpl#createUserResource(UserResourceModel)}
     */
    @Test
    void testCreateUserResource() {
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

        UserResource userResource = new UserResource();
        userResource.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userResource.setEntry(entry);
        userResource.setEntryName("Entry Name");
        userResource.setId(1L);
        userResource.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userResource.setStatus(Status.DRAFT);
        userResource.setTaskType(TaskType.CREATE);
        userResource.setUserId("42");
        when(userResourceRepository.save(Mockito.<UserResource>any())).thenReturn(userResource);

        AccessPolicy accessPolicy2 = new AccessPolicy();
        accessPolicy2.setCode("Code");
        accessPolicy2.setId(1L);
        accessPolicy2.setIsCustom(1);
        accessPolicy2.setName("Name");

        DashboardContents dashboardContents2 = new DashboardContents();
        dashboardContents2.setContent("Not all who wander are lost");
        dashboardContents2.setId(1L);

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

        Organisation organisation2 = new Organisation();
        organisation2.setContact("Contact");
        organisation2.setContactEmail("jane.doe@example.org");
        organisation2.setEmail("jane.doe@example.org");
        organisation2.setId(1L);
        organisation2.setIsCustom(1);
        organisation2.setTitle("Dr");

        Source source2 = new Source();
        source2.setCode("Code");
        source2.setId(1L);
        source2.setIsCustom(1);
        source2.setTerm("Term");

        Type type2 = new Type();
        type2.setCode("Code");
        type2.setId(1L);
        type2.setIsCustom(1);
        type2.setName("Name");

        Entry entry2 = new Entry();
        entry2.setAccessPolicy(accessPolicy2);
        entry2.setCode("Code");
        entry2.setCoverage("Coverage");
        entry2.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        entry2.setDashboardContents(dashboardContents2);
        entry2.setDataSource(dataSource2);
        entry2.setDefinitionType(definitionType2);
        entry2.setDeleted(1);
        entry2.setDisplayDataSource(displayDataSource2);
        entry2.setId(1L);
        entry2.setKeywords("Keywords");
        entry2.setLogo("Logo");
        entry2.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        entry2.setOrganisation(organisation2);
        entry2.setPublishDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        entry2.setScoreWeight(10.0d);
        entry2.setSource(source2);
        entry2.setSummary("Summary");
        entry2.setTags("Tags");
        entry2.setTitle("Dr");
        entry2.setType(type2);
        entry2.setWorkflowInstanceId(1L);

        UserResource userResource2 = new UserResource();
        userResource2.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userResource2.setEntry(entry2);
        userResource2.setEntryName("Entry Name");
        userResource2.setId(1L);
        userResource2.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userResource2.setStatus(Status.DRAFT);
        userResource2.setTaskType(TaskType.CREATE);
        userResource2.setUserId("42");
        when(userResourcesMapper.mapToUserResource(Mockito.<UserResourceModel>any())).thenReturn(userResource2);
        when(transferOptionService.saveTransferOptions(Mockito.<List<TransferOptionModel>>any(), Mockito.<Entry>any()))
                .thenReturn(new ArrayList<>());

        AccessPolicyModel accessPolicy3 = new AccessPolicyModel();
        accessPolicy3.setCode("Code");
        accessPolicy3.setName("Name");

        DashboardContentsModel dashboardContents3 = new DashboardContentsModel();
        dashboardContents3.setContent("Not all who wander are lost");

        OrganisationModel organisation3 = new OrganisationModel();
        organisation3.setContact("Contact");
        organisation3.setContactEmail("jane.doe@example.org");
        organisation3.setEmail("jane.doe@example.org");
        organisation3.setTitle("Dr");

        SourceModel source3 = new SourceModel();
        source3.setCode("Code");
        source3.setTerm("Term");

        EntryModel entry3 = new EntryModel();
        entry3.setAccessPolicy(accessPolicy3);
        entry3.setCode("Code");
        entry3.setCoverage("Coverage");
        entry3.setDashboardContents(dashboardContents3);
        entry3.setDataSource(DataSourceModel.DAB);
        entry3.setDefinitionType(DefinitionTypeModel.PREDEFINED);
        entry3.setDisplayDataSource(DataSourceModel.DAB);
        entry3.setKeywords("Keywords");
        entry3.setLogo("Logo");
        entry3.setOrganisation(organisation3);
        entry3.setSource(source3);
        entry3.setSummary("Summary");
        entry3.setTags("Tags");
        entry3.setTitle("Dr");
        entry3.setTransferOptions(new ArrayList<>());
        entry3.setType(TypeModel.SERVICE);
        entry3.setUserId("42");

        UserResourceModel userResourceDto = new UserResourceModel();
        userResourceDto.setEntry(entry3);
        userResourceDto.setEntryName("Entry Name");
        userResourceDto.setTaskType(TaskType.CREATE);
        userResourceDto.setUserId("42");
        userResourceServiceImpl.createUserResource(userResourceDto);
        verify(userResourcesMapper).mapToUserResource(Mockito.<UserResourceModel>any());
        verify(transferOptionService).saveTransferOptions(Mockito.<List<TransferOptionModel>>any(),
                Mockito.<Entry>any());
        verify(userResourceRepository).save(Mockito.<UserResource>any());
        assertEquals("42", userResourceDto.getUserId());
        assertEquals("Entry Name", userResourceDto.getEntryName());
        assertEquals(TaskType.CREATE, userResourceDto.getTaskType());
        assertSame(entry3, userResourceDto.getEntry());
    }

    /**
     * Method under test: {@link UserResourceServiceImpl#createUserResource(UserResourceModel)}
     */
    @Test
    void testCreateUserResource2() {
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

        UserResource userResource = new UserResource();
        userResource.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userResource.setEntry(entry);
        userResource.setEntryName("Entry Name");
        userResource.setId(1L);
        userResource.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userResource.setStatus(Status.DRAFT);
        userResource.setTaskType(TaskType.CREATE);
        userResource.setUserId("42");
        when(userResourceRepository.save(Mockito.<UserResource>any())).thenReturn(userResource);

        AccessPolicy accessPolicy2 = new AccessPolicy();
        accessPolicy2.setCode("Code");
        accessPolicy2.setId(1L);
        accessPolicy2.setIsCustom(1);
        accessPolicy2.setName("Name");

        DashboardContents dashboardContents2 = new DashboardContents();
        dashboardContents2.setContent("Not all who wander are lost");
        dashboardContents2.setId(1L);

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

        Organisation organisation2 = new Organisation();
        organisation2.setContact("Contact");
        organisation2.setContactEmail("jane.doe@example.org");
        organisation2.setEmail("jane.doe@example.org");
        organisation2.setId(1L);
        organisation2.setIsCustom(1);
        organisation2.setTitle("Dr");

        Source source2 = new Source();
        source2.setCode("Code");
        source2.setId(1L);
        source2.setIsCustom(1);
        source2.setTerm("Term");

        Type type2 = new Type();
        type2.setCode("Code");
        type2.setId(1L);
        type2.setIsCustom(1);
        type2.setName("Name");

        Entry entry2 = new Entry();
        entry2.setAccessPolicy(accessPolicy2);
        entry2.setCode("Code");
        entry2.setCoverage("Coverage");
        entry2.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        entry2.setDashboardContents(dashboardContents2);
        entry2.setDataSource(dataSource2);
        entry2.setDefinitionType(definitionType2);
        entry2.setDeleted(1);
        entry2.setDisplayDataSource(displayDataSource2);
        entry2.setId(1L);
        entry2.setKeywords("Keywords");
        entry2.setLogo("Logo");
        entry2.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        entry2.setOrganisation(organisation2);
        entry2.setPublishDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        entry2.setScoreWeight(10.0d);
        entry2.setSource(source2);
        entry2.setSummary("Summary");
        entry2.setTags("Tags");
        entry2.setTitle("Dr");
        entry2.setType(type2);
        entry2.setWorkflowInstanceId(1L);

        UserResource userResource2 = new UserResource();
        userResource2.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userResource2.setEntry(entry2);
        userResource2.setEntryName("Entry Name");
        userResource2.setId(1L);
        userResource2.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userResource2.setStatus(Status.DRAFT);
        userResource2.setTaskType(TaskType.CREATE);
        userResource2.setUserId("42");
        when(userResourcesMapper.mapToUserResource(Mockito.<UserResourceModel>any())).thenReturn(userResource2);
        when(transferOptionService.saveTransferOptions(Mockito.<List<TransferOptionModel>>any(), Mockito.<Entry>any()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));

        AccessPolicyModel accessPolicy3 = new AccessPolicyModel();
        accessPolicy3.setCode("Code");
        accessPolicy3.setName("Name");

        DashboardContentsModel dashboardContents3 = new DashboardContentsModel();
        dashboardContents3.setContent("Not all who wander are lost");

        OrganisationModel organisation3 = new OrganisationModel();
        organisation3.setContact("Contact");
        organisation3.setContactEmail("jane.doe@example.org");
        organisation3.setEmail("jane.doe@example.org");
        organisation3.setTitle("Dr");

        SourceModel source3 = new SourceModel();
        source3.setCode("Code");
        source3.setTerm("Term");

        EntryModel entry3 = new EntryModel();
        entry3.setAccessPolicy(accessPolicy3);
        entry3.setCode("Code");
        entry3.setCoverage("Coverage");
        entry3.setDashboardContents(dashboardContents3);
        entry3.setDataSource(DataSourceModel.DAB);
        entry3.setDefinitionType(DefinitionTypeModel.PREDEFINED);
        entry3.setDisplayDataSource(DataSourceModel.DAB);
        entry3.setKeywords("Keywords");
        entry3.setLogo("Logo");
        entry3.setOrganisation(organisation3);
        entry3.setSource(source3);
        entry3.setSummary("Summary");
        entry3.setTags("Tags");
        entry3.setTitle("Dr");
        entry3.setTransferOptions(new ArrayList<>());
        entry3.setType(TypeModel.SERVICE);
        entry3.setUserId("42");

        UserResourceModel userResourceDto = new UserResourceModel();
        userResourceDto.setEntry(entry3);
        userResourceDto.setEntryName("Entry Name");
        userResourceDto.setTaskType(TaskType.CREATE);
        userResourceDto.setUserId("42");
        assertThrows(ResourceNotFoundException.class,
                () -> userResourceServiceImpl.createUserResource(userResourceDto));
        verify(userResourcesMapper).mapToUserResource(Mockito.<UserResourceModel>any());
        verify(transferOptionService).saveTransferOptions(Mockito.<List<TransferOptionModel>>any(),
                Mockito.<Entry>any());
        verify(userResourceRepository).save(Mockito.<UserResource>any());
    }

    /**
     * Method under test: {@link UserResourceServiceImpl#updateUserResource(long, UserResourceModel)}
     */
    @Test
    void testUpdateUserResource() {
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

        UserResource userResource = new UserResource();
        userResource.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userResource.setEntry(entry);
        userResource.setEntryName("Entry Name");
        userResource.setId(1L);
        userResource.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userResource.setStatus(Status.DRAFT);
        userResource.setTaskType(TaskType.CREATE);
        userResource.setUserId("42");
        Optional<UserResource> ofResult = Optional.of(userResource);
        when(userResourceRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(userResourcesMapper.mapToUserResource(Mockito.<UserResourceModel>any(), Mockito.<UserResource>any()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));

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

        UserResourceModel userResourceDto = new UserResourceModel();
        userResourceDto.setEntry(entry2);
        userResourceDto.setEntryName("Entry Name");
        userResourceDto.setTaskType(TaskType.CREATE);
        userResourceDto.setUserId("42");
        assertThrows(ResourceNotFoundException.class,
                () -> userResourceServiceImpl.updateUserResource(1L, userResourceDto));
        verify(userResourcesMapper).mapToUserResource(Mockito.<UserResourceModel>any(), Mockito.<UserResource>any());
        verify(userResourceRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link UserResourceServiceImpl#updateUserResource(long, UserResourceModel)}
     */
    @Test
    void testUpdateUserResource2() {
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

        UserResource userResource = new UserResource();
        userResource.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userResource.setEntry(entry);
        userResource.setEntryName("Entry Name");
        userResource.setId(1L);
        userResource.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userResource.setStatus(Status.DRAFT);
        userResource.setTaskType(TaskType.CREATE);
        userResource.setUserId("42");
        Optional<UserResource> ofResult = Optional.of(userResource);

        AccessPolicy accessPolicy2 = new AccessPolicy();
        accessPolicy2.setCode("Code");
        accessPolicy2.setId(1L);
        accessPolicy2.setIsCustom(1);
        accessPolicy2.setName("Name");

        DashboardContents dashboardContents2 = new DashboardContents();
        dashboardContents2.setContent("Not all who wander are lost");
        dashboardContents2.setId(1L);

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

        Organisation organisation2 = new Organisation();
        organisation2.setContact("Contact");
        organisation2.setContactEmail("jane.doe@example.org");
        organisation2.setEmail("jane.doe@example.org");
        organisation2.setId(1L);
        organisation2.setIsCustom(1);
        organisation2.setTitle("Dr");

        Source source2 = new Source();
        source2.setCode("Code");
        source2.setId(1L);
        source2.setIsCustom(1);
        source2.setTerm("Term");

        Type type2 = new Type();
        type2.setCode("Code");
        type2.setId(1L);
        type2.setIsCustom(1);
        type2.setName("Name");

        Entry entry2 = new Entry();
        entry2.setAccessPolicy(accessPolicy2);
        entry2.setCode("Code");
        entry2.setCoverage("Coverage");
        entry2.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        entry2.setDashboardContents(dashboardContents2);
        entry2.setDataSource(dataSource2);
        entry2.setDefinitionType(definitionType2);
        entry2.setDeleted(1);
        entry2.setDisplayDataSource(displayDataSource2);
        entry2.setId(1L);
        entry2.setKeywords("Keywords");
        entry2.setLogo("Logo");
        entry2.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        entry2.setOrganisation(organisation2);
        entry2.setPublishDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        entry2.setScoreWeight(10.0d);
        entry2.setSource(source2);
        entry2.setSummary("Summary");
        entry2.setTags("Tags");
        entry2.setTitle("Dr");
        entry2.setType(type2);
        entry2.setWorkflowInstanceId(1L);

        UserResource userResource2 = new UserResource();
        userResource2.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userResource2.setEntry(entry2);
        userResource2.setEntryName("Entry Name");
        userResource2.setId(1L);
        userResource2.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userResource2.setStatus(Status.DRAFT);
        userResource2.setTaskType(TaskType.CREATE);
        userResource2.setUserId("42");
        when(userResourceRepository.save(Mockito.<UserResource>any())).thenReturn(userResource2);
        when(userResourceRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        AccessPolicy accessPolicy3 = new AccessPolicy();
        accessPolicy3.setCode("Code");
        accessPolicy3.setId(1L);
        accessPolicy3.setIsCustom(1);
        accessPolicy3.setName("Name");

        DashboardContents dashboardContents3 = new DashboardContents();
        dashboardContents3.setContent("Not all who wander are lost");
        dashboardContents3.setId(1L);

        DataSource dataSource3 = new DataSource();
        dataSource3.setCode("Code");
        dataSource3.setId(1L);
        dataSource3.setIsCustom(1);
        dataSource3.setName("Name");

        DefinitionType definitionType3 = new DefinitionType();
        definitionType3.setCode(1);
        definitionType3.setId(1L);
        definitionType3.setName("Name");

        DataSource displayDataSource3 = new DataSource();
        displayDataSource3.setCode("Code");
        displayDataSource3.setId(1L);
        displayDataSource3.setIsCustom(1);
        displayDataSource3.setName("Name");

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

        Type type3 = new Type();
        type3.setCode("Code");
        type3.setId(1L);
        type3.setIsCustom(1);
        type3.setName("Name");

        Entry entry3 = new Entry();
        entry3.setAccessPolicy(accessPolicy3);
        entry3.setCode("Code");
        entry3.setCoverage("Coverage");
        entry3.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        entry3.setDashboardContents(dashboardContents3);
        entry3.setDataSource(dataSource3);
        entry3.setDefinitionType(definitionType3);
        entry3.setDeleted(1);
        entry3.setDisplayDataSource(displayDataSource3);
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
        entry3.setType(type3);
        entry3.setWorkflowInstanceId(1L);

        UserResource userResource3 = new UserResource();
        userResource3.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userResource3.setEntry(entry3);
        userResource3.setEntryName("Entry Name");
        userResource3.setId(1L);
        userResource3.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userResource3.setStatus(Status.DRAFT);
        userResource3.setTaskType(TaskType.CREATE);
        userResource3.setUserId("42");
        when(userResourcesMapper.mapToUserResource(Mockito.<UserResourceModel>any(), Mockito.<UserResource>any()))
                .thenReturn(userResource3);
        when(transferOptionService.saveTransferOptions(Mockito.<List<TransferOptionModel>>any(), Mockito.<Entry>any()))
                .thenReturn(new ArrayList<>());

        AccessPolicyModel accessPolicy4 = new AccessPolicyModel();
        accessPolicy4.setCode("Code");
        accessPolicy4.setName("Name");

        DashboardContentsModel dashboardContents4 = new DashboardContentsModel();
        dashboardContents4.setContent("Not all who wander are lost");

        OrganisationModel organisation4 = new OrganisationModel();
        organisation4.setContact("Contact");
        organisation4.setContactEmail("jane.doe@example.org");
        organisation4.setEmail("jane.doe@example.org");
        organisation4.setTitle("Dr");

        SourceModel source4 = new SourceModel();
        source4.setCode("Code");
        source4.setTerm("Term");

        EntryModel entry4 = new EntryModel();
        entry4.setAccessPolicy(accessPolicy4);
        entry4.setCode("Code");
        entry4.setCoverage("Coverage");
        entry4.setDashboardContents(dashboardContents4);
        entry4.setDataSource(DataSourceModel.DAB);
        entry4.setDefinitionType(DefinitionTypeModel.PREDEFINED);
        entry4.setDisplayDataSource(DataSourceModel.DAB);
        entry4.setKeywords("Keywords");
        entry4.setLogo("Logo");
        entry4.setOrganisation(organisation4);
        entry4.setSource(source4);
        entry4.setSummary("Summary");
        entry4.setTags("Tags");
        entry4.setTitle("Dr");
        entry4.setTransferOptions(new ArrayList<>());
        entry4.setType(TypeModel.SERVICE);
        entry4.setUserId("42");

        UserResourceModel userResourceDto = new UserResourceModel();
        userResourceDto.setEntry(entry4);
        userResourceDto.setEntryName("Entry Name");
        userResourceDto.setTaskType(TaskType.CREATE);
        userResourceDto.setUserId("42");
        userResourceServiceImpl.updateUserResource(1L, userResourceDto);
        verify(userResourcesMapper).mapToUserResource(Mockito.<UserResourceModel>any(), Mockito.<UserResource>any());
        verify(transferOptionService).saveTransferOptions(Mockito.<List<TransferOptionModel>>any(),
                Mockito.<Entry>any());
        verify(userResourceRepository).findById(Mockito.<Long>any());
        verify(userResourceRepository).save(Mockito.<UserResource>any());
        assertEquals("42", userResourceDto.getUserId());
        assertEquals("Entry Name", userResourceDto.getEntryName());
        assertEquals(TaskType.CREATE, userResourceDto.getTaskType());
        assertSame(entry4, userResourceDto.getEntry());
    }

    /**
     * Method under test: {@link UserResourceServiceImpl#removeUserResource(long)}
     */
    @Test
    void testRemoveUserResource() {
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

        UserResource userResource = new UserResource();
        userResource.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userResource.setEntry(entry);
        userResource.setEntryName("Entry Name");
        userResource.setId(1L);
        userResource.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userResource.setStatus(Status.DRAFT);
        userResource.setTaskType(TaskType.CREATE);
        userResource.setUserId("42");
        Optional<UserResource> ofResult = Optional.of(userResource);

        AccessPolicy accessPolicy2 = new AccessPolicy();
        accessPolicy2.setCode("Code");
        accessPolicy2.setId(1L);
        accessPolicy2.setIsCustom(1);
        accessPolicy2.setName("Name");

        DashboardContents dashboardContents2 = new DashboardContents();
        dashboardContents2.setContent("Not all who wander are lost");
        dashboardContents2.setId(1L);

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

        Organisation organisation2 = new Organisation();
        organisation2.setContact("Contact");
        organisation2.setContactEmail("jane.doe@example.org");
        organisation2.setEmail("jane.doe@example.org");
        organisation2.setId(1L);
        organisation2.setIsCustom(1);
        organisation2.setTitle("Dr");

        Source source2 = new Source();
        source2.setCode("Code");
        source2.setId(1L);
        source2.setIsCustom(1);
        source2.setTerm("Term");

        Type type2 = new Type();
        type2.setCode("Code");
        type2.setId(1L);
        type2.setIsCustom(1);
        type2.setName("Name");

        Entry entry2 = new Entry();
        entry2.setAccessPolicy(accessPolicy2);
        entry2.setCode("Code");
        entry2.setCoverage("Coverage");
        entry2.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        entry2.setDashboardContents(dashboardContents2);
        entry2.setDataSource(dataSource2);
        entry2.setDefinitionType(definitionType2);
        entry2.setDeleted(1);
        entry2.setDisplayDataSource(displayDataSource2);
        entry2.setId(1L);
        entry2.setKeywords("Keywords");
        entry2.setLogo("Logo");
        entry2.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        entry2.setOrganisation(organisation2);
        entry2.setPublishDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        entry2.setScoreWeight(10.0d);
        entry2.setSource(source2);
        entry2.setSummary("Summary");
        entry2.setTags("Tags");
        entry2.setTitle("Dr");
        entry2.setType(type2);
        entry2.setWorkflowInstanceId(1L);

        UserResource userResource2 = new UserResource();
        userResource2.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userResource2.setEntry(entry2);
        userResource2.setEntryName("Entry Name");
        userResource2.setId(1L);
        userResource2.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userResource2.setStatus(Status.DRAFT);
        userResource2.setTaskType(TaskType.CREATE);
        userResource2.setUserId("42");
        when(userResourceRepository.save(Mockito.<UserResource>any())).thenReturn(userResource2);
        when(userResourceRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        userResourceServiceImpl.removeUserResource(1L);
        verify(userResourceRepository).findById(Mockito.<Long>any());
        verify(userResourceRepository).save(Mockito.<UserResource>any());
    }

    /**
     * Method under test: {@link UserResourceServiceImpl#removeUserResource(long)}
     */
    @Test
    void testRemoveUserResource2() {
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

        UserResource userResource = new UserResource();
        userResource.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userResource.setEntry(entry);
        userResource.setEntryName("Entry Name");
        userResource.setId(1L);
        userResource.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userResource.setStatus(Status.DRAFT);
        userResource.setTaskType(TaskType.CREATE);
        userResource.setUserId("42");
        Optional<UserResource> ofResult = Optional.of(userResource);
        when(userResourceRepository.save(Mockito.<UserResource>any()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));
        when(userResourceRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(ResourceNotFoundException.class, () -> userResourceServiceImpl.removeUserResource(1L));
        verify(userResourceRepository).findById(Mockito.<Long>any());
        verify(userResourceRepository).save(Mockito.<UserResource>any());
    }

    /**
     * Method under test: {@link UserResourceServiceImpl#deleteUserResource(long)}
     */
    @Test
    void testDeleteUserResource() {
        doNothing().when(userResourceRepository).deleteById(Mockito.<Long>any());
        userResourceServiceImpl.deleteUserResource(1L);
        verify(userResourceRepository).deleteById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link UserResourceServiceImpl#deleteUserResource(long)}
     */
    @Test
    void testDeleteUserResource2() {
        doThrow(new ResourceNotFoundException("An error occurred")).when(userResourceRepository)
                .deleteById(Mockito.<Long>any());
        assertThrows(ResourceNotFoundException.class, () -> userResourceServiceImpl.deleteUserResource(1L));
        verify(userResourceRepository).deleteById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link UserResourceServiceImpl#restoreUserResource(long)}
     */
    @Test
    void testRestoreUserResource() {
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

        UserResource userResource = new UserResource();
        userResource.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userResource.setEntry(entry);
        userResource.setEntryName("Entry Name");
        userResource.setId(1L);
        userResource.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userResource.setStatus(Status.DRAFT);
        userResource.setTaskType(TaskType.CREATE);
        userResource.setUserId("42");
        Optional<UserResource> ofResult = Optional.of(userResource);

        AccessPolicy accessPolicy2 = new AccessPolicy();
        accessPolicy2.setCode("Code");
        accessPolicy2.setId(1L);
        accessPolicy2.setIsCustom(1);
        accessPolicy2.setName("Name");

        DashboardContents dashboardContents2 = new DashboardContents();
        dashboardContents2.setContent("Not all who wander are lost");
        dashboardContents2.setId(1L);

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

        Organisation organisation2 = new Organisation();
        organisation2.setContact("Contact");
        organisation2.setContactEmail("jane.doe@example.org");
        organisation2.setEmail("jane.doe@example.org");
        organisation2.setId(1L);
        organisation2.setIsCustom(1);
        organisation2.setTitle("Dr");

        Source source2 = new Source();
        source2.setCode("Code");
        source2.setId(1L);
        source2.setIsCustom(1);
        source2.setTerm("Term");

        Type type2 = new Type();
        type2.setCode("Code");
        type2.setId(1L);
        type2.setIsCustom(1);
        type2.setName("Name");

        Entry entry2 = new Entry();
        entry2.setAccessPolicy(accessPolicy2);
        entry2.setCode("Code");
        entry2.setCoverage("Coverage");
        entry2.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        entry2.setDashboardContents(dashboardContents2);
        entry2.setDataSource(dataSource2);
        entry2.setDefinitionType(definitionType2);
        entry2.setDeleted(1);
        entry2.setDisplayDataSource(displayDataSource2);
        entry2.setId(1L);
        entry2.setKeywords("Keywords");
        entry2.setLogo("Logo");
        entry2.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        entry2.setOrganisation(organisation2);
        entry2.setPublishDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        entry2.setScoreWeight(10.0d);
        entry2.setSource(source2);
        entry2.setSummary("Summary");
        entry2.setTags("Tags");
        entry2.setTitle("Dr");
        entry2.setType(type2);
        entry2.setWorkflowInstanceId(1L);

        UserResource userResource2 = new UserResource();
        userResource2.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userResource2.setEntry(entry2);
        userResource2.setEntryName("Entry Name");
        userResource2.setId(1L);
        userResource2.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userResource2.setStatus(Status.DRAFT);
        userResource2.setTaskType(TaskType.CREATE);
        userResource2.setUserId("42");
        when(userResourceRepository.save(Mockito.<UserResource>any())).thenReturn(userResource2);
        when(userResourceRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        userResourceServiceImpl.restoreUserResource(1L);
        verify(userResourceRepository).findById(Mockito.<Long>any());
        verify(userResourceRepository).save(Mockito.<UserResource>any());
    }

    /**
     * Method under test: {@link UserResourceServiceImpl#restoreUserResource(long)}
     */
    @Test
    void testRestoreUserResource2() {
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

        UserResource userResource = new UserResource();
        userResource.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userResource.setEntry(entry);
        userResource.setEntryName("Entry Name");
        userResource.setId(1L);
        userResource.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userResource.setStatus(Status.DRAFT);
        userResource.setTaskType(TaskType.CREATE);
        userResource.setUserId("42");
        Optional<UserResource> ofResult = Optional.of(userResource);
        when(userResourceRepository.save(Mockito.<UserResource>any()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));
        when(userResourceRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(ResourceNotFoundException.class, () -> userResourceServiceImpl.restoreUserResource(1L));
        verify(userResourceRepository).findById(Mockito.<Long>any());
        verify(userResourceRepository).save(Mockito.<UserResource>any());
    }

    /**
     * Method under test: {@link UserResourceServiceImpl#approveUserResource(long)}
     */
    @Test
    void testApproveUserResource() {
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

        UserResource userResource = new UserResource();
        userResource.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userResource.setEntry(entry);
        userResource.setEntryName("Entry Name");
        userResource.setId(1L);
        userResource.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userResource.setStatus(Status.DRAFT);
        userResource.setTaskType(TaskType.CREATE);
        userResource.setUserId("42");
        Optional<UserResource> ofResult = Optional.of(userResource);

        AccessPolicy accessPolicy2 = new AccessPolicy();
        accessPolicy2.setCode("Code");
        accessPolicy2.setId(1L);
        accessPolicy2.setIsCustom(1);
        accessPolicy2.setName("Name");

        DashboardContents dashboardContents2 = new DashboardContents();
        dashboardContents2.setContent("Not all who wander are lost");
        dashboardContents2.setId(1L);

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

        Organisation organisation2 = new Organisation();
        organisation2.setContact("Contact");
        organisation2.setContactEmail("jane.doe@example.org");
        organisation2.setEmail("jane.doe@example.org");
        organisation2.setId(1L);
        organisation2.setIsCustom(1);
        organisation2.setTitle("Dr");

        Source source2 = new Source();
        source2.setCode("Code");
        source2.setId(1L);
        source2.setIsCustom(1);
        source2.setTerm("Term");

        Type type2 = new Type();
        type2.setCode("Code");
        type2.setId(1L);
        type2.setIsCustom(1);
        type2.setName("Name");

        Entry entry2 = new Entry();
        entry2.setAccessPolicy(accessPolicy2);
        entry2.setCode("Code");
        entry2.setCoverage("Coverage");
        entry2.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        entry2.setDashboardContents(dashboardContents2);
        entry2.setDataSource(dataSource2);
        entry2.setDefinitionType(definitionType2);
        entry2.setDeleted(1);
        entry2.setDisplayDataSource(displayDataSource2);
        entry2.setId(1L);
        entry2.setKeywords("Keywords");
        entry2.setLogo("Logo");
        entry2.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        entry2.setOrganisation(organisation2);
        entry2.setPublishDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        entry2.setScoreWeight(10.0d);
        entry2.setSource(source2);
        entry2.setSummary("Summary");
        entry2.setTags("Tags");
        entry2.setTitle("Dr");
        entry2.setType(type2);
        entry2.setWorkflowInstanceId(1L);

        UserResource userResource2 = new UserResource();
        userResource2.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userResource2.setEntry(entry2);
        userResource2.setEntryName("Entry Name");
        userResource2.setId(1L);
        userResource2.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userResource2.setStatus(Status.DRAFT);
        userResource2.setTaskType(TaskType.CREATE);
        userResource2.setUserId("42");
        when(userResourceRepository.save(Mockito.<UserResource>any())).thenReturn(userResource2);
        when(userResourceRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        UserResource actualApproveUserResourceResult = userResourceServiceImpl.approveUserResource(1L);
        verify(userResourceRepository).findById(Mockito.<Long>any());
        verify(userResourceRepository).save(Mockito.<UserResource>any());
        assertEquals(Status.APPROVED, actualApproveUserResourceResult.getStatus());
        assertSame(userResource, actualApproveUserResourceResult);
    }

    /**
     * Method under test: {@link UserResourceServiceImpl#approveUserResource(long)}
     */
    @Test
    void testApproveUserResource2() {
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

        UserResource userResource = new UserResource();
        userResource.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userResource.setEntry(entry);
        userResource.setEntryName("Entry Name");
        userResource.setId(1L);
        userResource.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userResource.setStatus(Status.DRAFT);
        userResource.setTaskType(TaskType.CREATE);
        userResource.setUserId("42");
        Optional<UserResource> ofResult = Optional.of(userResource);
        when(userResourceRepository.save(Mockito.<UserResource>any()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));
        when(userResourceRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(ResourceNotFoundException.class, () -> userResourceServiceImpl.approveUserResource(1L));
        verify(userResourceRepository).findById(Mockito.<Long>any());
        verify(userResourceRepository).save(Mockito.<UserResource>any());
    }

    /**
     * Method under test: {@link UserResourceServiceImpl#denyUserResource(long)}
     */
    @Test
    void testDenyUserResource() {
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

        UserResource userResource = new UserResource();
        userResource.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userResource.setEntry(entry);
        userResource.setEntryName("Entry Name");
        userResource.setId(1L);
        userResource.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userResource.setStatus(Status.DRAFT);
        userResource.setTaskType(TaskType.CREATE);
        userResource.setUserId("42");
        Optional<UserResource> ofResult = Optional.of(userResource);

        AccessPolicy accessPolicy2 = new AccessPolicy();
        accessPolicy2.setCode("Code");
        accessPolicy2.setId(1L);
        accessPolicy2.setIsCustom(1);
        accessPolicy2.setName("Name");

        DashboardContents dashboardContents2 = new DashboardContents();
        dashboardContents2.setContent("Not all who wander are lost");
        dashboardContents2.setId(1L);

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

        Organisation organisation2 = new Organisation();
        organisation2.setContact("Contact");
        organisation2.setContactEmail("jane.doe@example.org");
        organisation2.setEmail("jane.doe@example.org");
        organisation2.setId(1L);
        organisation2.setIsCustom(1);
        organisation2.setTitle("Dr");

        Source source2 = new Source();
        source2.setCode("Code");
        source2.setId(1L);
        source2.setIsCustom(1);
        source2.setTerm("Term");

        Type type2 = new Type();
        type2.setCode("Code");
        type2.setId(1L);
        type2.setIsCustom(1);
        type2.setName("Name");

        Entry entry2 = new Entry();
        entry2.setAccessPolicy(accessPolicy2);
        entry2.setCode("Code");
        entry2.setCoverage("Coverage");
        entry2.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        entry2.setDashboardContents(dashboardContents2);
        entry2.setDataSource(dataSource2);
        entry2.setDefinitionType(definitionType2);
        entry2.setDeleted(1);
        entry2.setDisplayDataSource(displayDataSource2);
        entry2.setId(1L);
        entry2.setKeywords("Keywords");
        entry2.setLogo("Logo");
        entry2.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        entry2.setOrganisation(organisation2);
        entry2.setPublishDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        entry2.setScoreWeight(10.0d);
        entry2.setSource(source2);
        entry2.setSummary("Summary");
        entry2.setTags("Tags");
        entry2.setTitle("Dr");
        entry2.setType(type2);
        entry2.setWorkflowInstanceId(1L);

        UserResource userResource2 = new UserResource();
        userResource2.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userResource2.setEntry(entry2);
        userResource2.setEntryName("Entry Name");
        userResource2.setId(1L);
        userResource2.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userResource2.setStatus(Status.DRAFT);
        userResource2.setTaskType(TaskType.CREATE);
        userResource2.setUserId("42");
        when(userResourceRepository.save(Mockito.<UserResource>any())).thenReturn(userResource2);
        when(userResourceRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        userResourceServiceImpl.denyUserResource(1L);
        verify(userResourceRepository).findById(Mockito.<Long>any());
        verify(userResourceRepository).save(Mockito.<UserResource>any());
    }

    /**
     * Method under test: {@link UserResourceServiceImpl#denyUserResource(long)}
     */
    @Test
    void testDenyUserResource2() {
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

        UserResource userResource = new UserResource();
        userResource.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userResource.setEntry(entry);
        userResource.setEntryName("Entry Name");
        userResource.setId(1L);
        userResource.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userResource.setStatus(Status.DRAFT);
        userResource.setTaskType(TaskType.CREATE);
        userResource.setUserId("42");
        Optional<UserResource> ofResult = Optional.of(userResource);
        when(userResourceRepository.save(Mockito.<UserResource>any()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));
        when(userResourceRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(ResourceNotFoundException.class, () -> userResourceServiceImpl.denyUserResource(1L));
        verify(userResourceRepository).findById(Mockito.<Long>any());
        verify(userResourceRepository).save(Mockito.<UserResource>any());
    }

    /**
     * Method under test: {@link UserResourceServiceImpl#pendingUserResource(long)}
     */
    @Test
    void testPendingUserResource() {
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

        UserResource userResource = new UserResource();
        userResource.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userResource.setEntry(entry);
        userResource.setEntryName("Entry Name");
        userResource.setId(1L);
        userResource.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userResource.setStatus(Status.DRAFT);
        userResource.setTaskType(TaskType.CREATE);
        userResource.setUserId("42");
        Optional<UserResource> ofResult = Optional.of(userResource);

        AccessPolicy accessPolicy2 = new AccessPolicy();
        accessPolicy2.setCode("Code");
        accessPolicy2.setId(1L);
        accessPolicy2.setIsCustom(1);
        accessPolicy2.setName("Name");

        DashboardContents dashboardContents2 = new DashboardContents();
        dashboardContents2.setContent("Not all who wander are lost");
        dashboardContents2.setId(1L);

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

        Organisation organisation2 = new Organisation();
        organisation2.setContact("Contact");
        organisation2.setContactEmail("jane.doe@example.org");
        organisation2.setEmail("jane.doe@example.org");
        organisation2.setId(1L);
        organisation2.setIsCustom(1);
        organisation2.setTitle("Dr");

        Source source2 = new Source();
        source2.setCode("Code");
        source2.setId(1L);
        source2.setIsCustom(1);
        source2.setTerm("Term");

        Type type2 = new Type();
        type2.setCode("Code");
        type2.setId(1L);
        type2.setIsCustom(1);
        type2.setName("Name");

        Entry entry2 = new Entry();
        entry2.setAccessPolicy(accessPolicy2);
        entry2.setCode("Code");
        entry2.setCoverage("Coverage");
        entry2.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        entry2.setDashboardContents(dashboardContents2);
        entry2.setDataSource(dataSource2);
        entry2.setDefinitionType(definitionType2);
        entry2.setDeleted(1);
        entry2.setDisplayDataSource(displayDataSource2);
        entry2.setId(1L);
        entry2.setKeywords("Keywords");
        entry2.setLogo("Logo");
        entry2.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        entry2.setOrganisation(organisation2);
        entry2.setPublishDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        entry2.setScoreWeight(10.0d);
        entry2.setSource(source2);
        entry2.setSummary("Summary");
        entry2.setTags("Tags");
        entry2.setTitle("Dr");
        entry2.setType(type2);
        entry2.setWorkflowInstanceId(1L);

        UserResource userResource2 = new UserResource();
        userResource2.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userResource2.setEntry(entry2);
        userResource2.setEntryName("Entry Name");
        userResource2.setId(1L);
        userResource2.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userResource2.setStatus(Status.DRAFT);
        userResource2.setTaskType(TaskType.CREATE);
        userResource2.setUserId("42");
        when(userResourceRepository.save(Mockito.<UserResource>any())).thenReturn(userResource2);
        when(userResourceRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        userResourceServiceImpl.pendingUserResource(1L);
        verify(userResourceRepository).findById(Mockito.<Long>any());
        verify(userResourceRepository).save(Mockito.<UserResource>any());
    }

    /**
     * Method under test: {@link UserResourceServiceImpl#pendingUserResource(long)}
     */
    @Test
    void testPendingUserResource2() {
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

        UserResource userResource = new UserResource();
        userResource.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userResource.setEntry(entry);
        userResource.setEntryName("Entry Name");
        userResource.setId(1L);
        userResource.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        userResource.setStatus(Status.DRAFT);
        userResource.setTaskType(TaskType.CREATE);
        userResource.setUserId("42");
        Optional<UserResource> ofResult = Optional.of(userResource);
        when(userResourceRepository.save(Mockito.<UserResource>any()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));
        when(userResourceRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(ResourceNotFoundException.class, () -> userResourceServiceImpl.pendingUserResource(1L));
        verify(userResourceRepository).findById(Mockito.<Long>any());
        verify(userResourceRepository).save(Mockito.<UserResource>any());
    }
}
