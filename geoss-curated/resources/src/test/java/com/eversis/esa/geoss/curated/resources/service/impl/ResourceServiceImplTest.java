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
import com.eversis.esa.geoss.curated.resources.service.TransferOptionService;

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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * The type Resource service impl test.
 */
@ContextConfiguration(classes = {ResourceServiceImpl.class})
@ExtendWith(SpringExtension.class)
class ResourceServiceImplTest {

    @MockBean
    private EntryMapper entryMapper;

    @MockBean
    private EntryRepository entryRepository;

    @MockBean
    private TransferOptionService transferOptionService;

    @Autowired
    private ResourceServiceImpl resourceServiceImpl;

    /**
     * Test find entries.
     */
    @Test
    void testFindEntries() {
        PageImpl<Entry> pageImpl = new PageImpl<>(new ArrayList<>());
        when(entryRepository.findAll(Mockito.<Pageable>any())).thenReturn(pageImpl);
        Page<Entry> actualFindEntriesResult = resourceServiceImpl.findEntries(null);
        verify(entryRepository).findAll(Mockito.<Pageable>any());
        assertTrue(actualFindEntriesResult.toList().isEmpty());
        assertSame(pageImpl, actualFindEntriesResult);
    }

    /**
     * Test find entries 2.
     */
    @Test
    void testFindEntries2() {
        when(entryRepository.findAll(Mockito.<Pageable>any()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));
        assertThrows(ResourceNotFoundException.class, () -> resourceServiceImpl.findEntries(null));
        verify(entryRepository).findAll(Mockito.<Pageable>any());
    }

    /**
     * Test find entry.
     */
    @Test
    void testFindEntry() {
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
        Optional<Entry> ofResult = Optional.of(entry);
        when(entryRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        Entry actualFindEntryResult = resourceServiceImpl.findEntry(1L);
        verify(entryRepository).findById(Mockito.<Long>any());
        assertSame(entry, actualFindEntryResult);
    }

    /**
     * Test create entry.
     */
    @Test
    void testCreateEntry() {
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
        when(entryRepository.save(Mockito.<Entry>any())).thenReturn(entry);

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
        when(entryMapper.mapToEntry(Mockito.<EntryModel>any())).thenReturn(entry2);

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

        EntryModel entryDto = new EntryModel();
        entryDto.setAccessPolicy(accessPolicy3);
        entryDto.setCode("Code");
        entryDto.setCoverage("Coverage");
        entryDto.setDashboardContents(dashboardContents3);
        entryDto.setDataSource(DataSourceModel.DAB);
        entryDto.setDefinitionType(DefinitionTypeModel.PREDEFINED);
        entryDto.setDisplayDataSource(DataSourceModel.DAB);
        entryDto.setKeywords("Keywords");
        entryDto.setLogo("Logo");
        entryDto.setOrganisation(organisation3);
        entryDto.setSource(source3);
        entryDto.setSummary("Summary");
        entryDto.setTags("Tags");
        entryDto.setTitle("Dr");
        entryDto.setTransferOptions(new ArrayList<>());
        entryDto.setType(TypeModel.SERVICE);
        entryDto.setUserId("42");
        resourceServiceImpl.createEntry(entryDto);
        verify(entryMapper).mapToEntry(Mockito.<EntryModel>any());
        verify(entryRepository).save(Mockito.<Entry>any());
        assertEquals("42", entryDto.getUserId());
        assertEquals("Code", entryDto.getCode());
        assertEquals("Coverage", entryDto.getCoverage());
        assertEquals("Dr", entryDto.getTitle());
        assertEquals("Keywords", entryDto.getKeywords());
        assertEquals("Logo", entryDto.getLogo());
        assertEquals("Summary", entryDto.getSummary());
        assertEquals("Tags", entryDto.getTags());
        assertEquals(DataSourceModel.DAB, entryDto.getDataSource());
        assertEquals(DataSourceModel.DAB, entryDto.getDisplayDataSource());
        assertEquals(TypeModel.SERVICE, entryDto.getType());
        assertEquals(DefinitionTypeModel.PREDEFINED, entryDto.getDefinitionType());
        assertTrue(entryDto.getTransferOptions().isEmpty());
        assertSame(accessPolicy3, entryDto.getAccessPolicy());
        assertSame(dashboardContents3, entryDto.getDashboardContents());
        assertSame(organisation3, entryDto.getOrganisation());
        assertSame(source3, entryDto.getSource());
    }

    /**
     * Test create entry 2.
     */
    @Test
    void testCreateEntry2() {
        when(entryMapper.mapToEntry(Mockito.<EntryModel>any()))
                .thenThrow(new ResourceNotFoundException("An error occurred"));

        AccessPolicyModel accessPolicy = new AccessPolicyModel();
        accessPolicy.setCode("Code");
        accessPolicy.setName("Name");

        DashboardContentsModel dashboardContents = new DashboardContentsModel();
        dashboardContents.setContent("Not all who wander are lost");

        OrganisationModel organisation = new OrganisationModel();
        organisation.setContact("Contact");
        organisation.setContactEmail("jane.doe@example.org");
        organisation.setEmail("jane.doe@example.org");
        organisation.setTitle("Dr");

        SourceModel source = new SourceModel();
        source.setCode("Code");
        source.setTerm("Term");

        EntryModel entryDto = new EntryModel();
        entryDto.setAccessPolicy(accessPolicy);
        entryDto.setCode("Code");
        entryDto.setCoverage("Coverage");
        entryDto.setDashboardContents(dashboardContents);
        entryDto.setDataSource(DataSourceModel.DAB);
        entryDto.setDefinitionType(DefinitionTypeModel.PREDEFINED);
        entryDto.setDisplayDataSource(DataSourceModel.DAB);
        entryDto.setKeywords("Keywords");
        entryDto.setLogo("Logo");
        entryDto.setOrganisation(organisation);
        entryDto.setSource(source);
        entryDto.setSummary("Summary");
        entryDto.setTags("Tags");
        entryDto.setTitle("Dr");
        entryDto.setTransferOptions(new ArrayList<>());
        entryDto.setType(TypeModel.SERVICE);
        entryDto.setUserId("42");
        assertThrows(ResourceNotFoundException.class, () -> resourceServiceImpl.createEntry(entryDto));
        verify(entryMapper).mapToEntry(Mockito.<EntryModel>any());
    }

    /**
     * Test update entry.
     */
    @Test
    void testUpdateEntry() {
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
        Optional<Entry> ofResult = Optional.of(entry);

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
        when(entryRepository.save(Mockito.<Entry>any())).thenReturn(entry2);
        when(entryRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

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
        when(entryMapper.mapToEntry(Mockito.<EntryModel>any(), Mockito.<Entry>any())).thenReturn(entry3);

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

        EntryModel entryDto = new EntryModel();
        entryDto.setAccessPolicy(accessPolicy4);
        entryDto.setCode("Code");
        entryDto.setCoverage("Coverage");
        entryDto.setDashboardContents(dashboardContents4);
        entryDto.setDataSource(DataSourceModel.DAB);
        entryDto.setDefinitionType(DefinitionTypeModel.PREDEFINED);
        entryDto.setDisplayDataSource(DataSourceModel.DAB);
        entryDto.setKeywords("Keywords");
        entryDto.setLogo("Logo");
        entryDto.setOrganisation(organisation4);
        entryDto.setSource(source4);
        entryDto.setSummary("Summary");
        entryDto.setTags("Tags");
        entryDto.setTitle("Dr");
        entryDto.setTransferOptions(new ArrayList<>());
        entryDto.setType(TypeModel.SERVICE);
        entryDto.setUserId("42");
        resourceServiceImpl.updateEntry(1L, entryDto);
        verify(entryMapper).mapToEntry(Mockito.<EntryModel>any(), Mockito.<Entry>any());
        verify(entryRepository).findById(Mockito.<Long>any());
        verify(entryRepository).save(Mockito.<Entry>any());
        assertEquals("42", entryDto.getUserId());
        assertEquals("Code", entryDto.getCode());
        assertEquals("Coverage", entryDto.getCoverage());
        assertEquals("Dr", entryDto.getTitle());
        assertEquals("Keywords", entryDto.getKeywords());
        assertEquals("Logo", entryDto.getLogo());
        assertEquals("Summary", entryDto.getSummary());
        assertEquals("Tags", entryDto.getTags());
        assertEquals(DataSourceModel.DAB, entryDto.getDataSource());
        assertEquals(DataSourceModel.DAB, entryDto.getDisplayDataSource());
        assertEquals(TypeModel.SERVICE, entryDto.getType());
        assertEquals(DefinitionTypeModel.PREDEFINED, entryDto.getDefinitionType());
        assertTrue(entryDto.getTransferOptions().isEmpty());
        assertSame(accessPolicy4, entryDto.getAccessPolicy());
        assertSame(dashboardContents4, entryDto.getDashboardContents());
        assertSame(organisation4, entryDto.getOrganisation());
        assertSame(source4, entryDto.getSource());
    }

    /**
     * Test update entry 2.
     */
    @Test
    void testUpdateEntry2() {
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
        Optional<Entry> ofResult = Optional.of(entry);
        when(entryRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(entryMapper.mapToEntry(Mockito.<EntryModel>any(), Mockito.<Entry>any()))
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

        EntryModel entryDto = new EntryModel();
        entryDto.setAccessPolicy(accessPolicy2);
        entryDto.setCode("Code");
        entryDto.setCoverage("Coverage");
        entryDto.setDashboardContents(dashboardContents2);
        entryDto.setDataSource(DataSourceModel.DAB);
        entryDto.setDefinitionType(DefinitionTypeModel.PREDEFINED);
        entryDto.setDisplayDataSource(DataSourceModel.DAB);
        entryDto.setKeywords("Keywords");
        entryDto.setLogo("Logo");
        entryDto.setOrganisation(organisation2);
        entryDto.setSource(source2);
        entryDto.setSummary("Summary");
        entryDto.setTags("Tags");
        entryDto.setTitle("Dr");
        entryDto.setTransferOptions(new ArrayList<>());
        entryDto.setType(TypeModel.SERVICE);
        entryDto.setUserId("42");
        assertThrows(ResourceNotFoundException.class, () -> resourceServiceImpl.updateEntry(1L, entryDto));
        verify(entryMapper).mapToEntry(Mockito.<EntryModel>any(), Mockito.<Entry>any());
        verify(entryRepository).findById(Mockito.<Long>any());
    }

    /**
     * Test remove entry.
     */
    @Test
    void testRemoveEntry() {
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
        Optional<Entry> ofResult = Optional.of(entry);

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
        when(entryRepository.save(Mockito.<Entry>any())).thenReturn(entry2);
        when(entryRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        resourceServiceImpl.removeEntry(1L);
        verify(entryRepository).findById(Mockito.<Long>any());
        verify(entryRepository).save(Mockito.<Entry>any());
    }

    /**
     * Test remove entry 2.
     */
    @Test
    void testRemoveEntry2() {
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
        Optional<Entry> ofResult = Optional.of(entry);
        when(entryRepository.save(Mockito.<Entry>any())).thenThrow(new ResourceNotFoundException("An error occurred"));
        when(entryRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(ResourceNotFoundException.class, () -> resourceServiceImpl.removeEntry(1L));
        verify(entryRepository).findById(Mockito.<Long>any());
        verify(entryRepository).save(Mockito.<Entry>any());
    }

    /**
     * Test delete entry.
     */
    @Test
    void testDeleteEntry() {
        doNothing().when(entryRepository).deleteById(Mockito.<Long>any());
        resourceServiceImpl.deleteEntry(1L);
        verify(entryRepository).deleteById(Mockito.<Long>any());
    }

    /**
     * Test delete entry 2.
     */
    @Test
    void testDeleteEntry2() {
        doThrow(new ResourceNotFoundException("An error occurred")).when(entryRepository)
                .deleteById(Mockito.<Long>any());
        assertThrows(ResourceNotFoundException.class, () -> resourceServiceImpl.deleteEntry(1L));
        verify(entryRepository).deleteById(Mockito.<Long>any());
    }

    /**
     * Test restore entry.
     */
    @Test
    void testRestoreEntry() {
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
        Optional<Entry> ofResult = Optional.of(entry);

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
        when(entryRepository.save(Mockito.<Entry>any())).thenReturn(entry2);
        when(entryRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        resourceServiceImpl.restoreEntry(1L);
        verify(entryRepository).findById(Mockito.<Long>any());
        verify(entryRepository).save(Mockito.<Entry>any());
    }

    /**
     * Test restore entry 2.
     */
    @Test
    void testRestoreEntry2() {
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
        Optional<Entry> ofResult = Optional.of(entry);
        when(entryRepository.save(Mockito.<Entry>any())).thenThrow(new ResourceNotFoundException("An error occurred"));
        when(entryRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(ResourceNotFoundException.class, () -> resourceServiceImpl.restoreEntry(1L));
        verify(entryRepository).findById(Mockito.<Long>any());
        verify(entryRepository).save(Mockito.<Entry>any());
    }
}
