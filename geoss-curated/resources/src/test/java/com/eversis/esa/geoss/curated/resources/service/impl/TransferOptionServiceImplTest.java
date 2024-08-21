package com.eversis.esa.geoss.curated.resources.service.impl;

import com.eversis.esa.geoss.curated.common.domain.DataSource;
import com.eversis.esa.geoss.curated.common.domain.Endpoint;
import com.eversis.esa.geoss.curated.common.domain.Protocol;
import com.eversis.esa.geoss.curated.common.domain.Type;
import com.eversis.esa.geoss.curated.common.model.EndpointModel;
import com.eversis.esa.geoss.curated.common.model.ProtocolModel;
import com.eversis.esa.geoss.curated.common.service.EndpointService;
import com.eversis.esa.geoss.curated.common.service.ProtocolService;
import com.eversis.esa.geoss.curated.resources.domain.AccessPolicy;
import com.eversis.esa.geoss.curated.resources.domain.DashboardContents;
import com.eversis.esa.geoss.curated.resources.domain.DefinitionType;
import com.eversis.esa.geoss.curated.resources.domain.Entry;
import com.eversis.esa.geoss.curated.resources.domain.Organisation;
import com.eversis.esa.geoss.curated.resources.domain.Source;
import com.eversis.esa.geoss.curated.resources.domain.TransferOption;
import com.eversis.esa.geoss.curated.resources.mapper.TransferOptionMapper;
import com.eversis.esa.geoss.curated.resources.model.TransferOptionModel;
import com.eversis.esa.geoss.curated.resources.repository.TransferOptionRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.AtLeast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * The type Transfer option service impl test.
 */
@ContextConfiguration(classes = {TransferOptionServiceImpl.class})
@ExtendWith(SpringExtension.class)
class TransferOptionServiceImplTest {

    @MockBean
    private TransferOptionMapper transferOptionMapper;

    @MockBean
    private TransferOptionRepository transferOptionRepository;

    @MockBean
    private EndpointService endpointService;

    @MockBean
    private ProtocolService protocolService;

    @Autowired
    private TransferOptionServiceImpl transferOptionServiceImpl;

    /**
     * Test find transfer options.
     */
    @Test
    void testFindTransferOptions() {
        PageImpl<TransferOption> pageImpl = new PageImpl<>(new ArrayList<>());
        when(transferOptionRepository.findAll(Mockito.<Pageable>any())).thenReturn(pageImpl);
        Page<TransferOption> actualFindTransferOptionsResult = transferOptionServiceImpl.findTransferOptions(null);
        verify(transferOptionRepository).findAll(Mockito.<Pageable>any());
        assertTrue(actualFindTransferOptionsResult.toList().isEmpty());
        assertSame(pageImpl, actualFindTransferOptionsResult);
    }

    /**
     * Test find transfer option.
     */
    @Test
    void testFindTransferOption() {
        Endpoint endpoint = new Endpoint();
        endpoint.setId(1L);
        endpoint.setIsCustom(1);
        endpoint.setUrl("https://example.org/example");
        endpoint.setUrlType("https://example.org/example");

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

        Protocol protocol = new Protocol();
        protocol.setId(1L);
        protocol.setIsCustom(1);
        protocol.setValue("42");

        TransferOption transferOption = new TransferOption();
        transferOption.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        transferOption.setDeleted(1);
        transferOption.setDescription("The characteristics of someone or something");
        transferOption.setEndpoint(endpoint);
        transferOption.setEntry(entry);
        transferOption.setId(1L);
        transferOption.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        transferOption.setName("Name");
        transferOption.setProtocol(protocol);
        transferOption.setTitle("Dr");
        Optional<TransferOption> ofResult = Optional.of(transferOption);
        when(transferOptionRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        TransferOption actualFindTransferOptionResult = transferOptionServiceImpl.findTransferOption(1L);
        verify(transferOptionRepository).findById(Mockito.<Long>any());
        assertSame(transferOption, actualFindTransferOptionResult);
    }

    /**
     * Test find transfer options by entry id.
     */
    @Test
    void testFindTransferOptionsByEntryId() {
        HashSet<TransferOption> transferOptionSet = new HashSet<>();
        when(transferOptionRepository.findByEntryId(anyLong())).thenReturn(transferOptionSet);
        Set<TransferOption> actualFindTransferOptionsByEntryIdResult = transferOptionServiceImpl
                .findTransferOptionsByEntryId(1L);
        verify(transferOptionRepository).findByEntryId(anyLong());
        assertTrue(actualFindTransferOptionsByEntryIdResult.isEmpty());
        assertSame(transferOptionSet, actualFindTransferOptionsByEntryIdResult);
    }

    /**
     * Test save transfer options.
     */
    @Test
    void testSaveTransferOptions() {
        ArrayList<TransferOption> transferOptionList = new ArrayList<>();
        when(transferOptionRepository.saveAll(Mockito.<Iterable<TransferOption>>any())).thenReturn(transferOptionList);
        when(transferOptionRepository.findByEntryId(anyLong())).thenReturn(new HashSet<>());
        ArrayList<TransferOptionModel> transferOptionDto = new ArrayList<>();

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

        Entry relatedEntry = new Entry();
        relatedEntry.setAccessPolicy(accessPolicy);
        relatedEntry.setCode("Code");
        relatedEntry.setCoverage("Coverage");
        relatedEntry.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        relatedEntry.setDashboardContents(dashboardContents);
        relatedEntry.setDataSource(dataSource);
        relatedEntry.setDefinitionType(definitionType);
        relatedEntry.setDeleted(1);
        relatedEntry.setDisplayDataSource(displayDataSource);
        relatedEntry.setId(1L);
        relatedEntry.setKeywords("Keywords");
        relatedEntry.setLogo("Logo");
        relatedEntry.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        relatedEntry.setOrganisation(organisation);
        relatedEntry.setPublishDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        relatedEntry.setScoreWeight(10.0d);
        relatedEntry.setSource(source);
        relatedEntry.setSummary("Summary");
        relatedEntry.setTags("Tags");
        relatedEntry.setTitle("Dr");
        relatedEntry.setType(type);
        relatedEntry.setWorkflowInstanceId(1L);
        List<TransferOption> actualSaveTransferOptionsResult = transferOptionServiceImpl
                .saveTransferOptions(transferOptionDto, relatedEntry);
        verify(transferOptionMapper, new AtLeast(0)).mapToTransferOption(Mockito.any(), anyLong());
        verify(transferOptionRepository).saveAll(Mockito.<Iterable<TransferOption>>any());
        assertTrue(actualSaveTransferOptionsResult.isEmpty());
        assertIterableEquals(transferOptionList, actualSaveTransferOptionsResult);
    }

    /**
     * Test save transfer options 2.
     */
    @Test
    void testSaveTransferOptions2() {
        Endpoint endpoint = new Endpoint();
        endpoint.setId(1L);
        endpoint.setIsCustom(1);
        endpoint.setUrl("https://example.org/example");
        endpoint.setUrlType("https://example.org/example");

        AccessPolicy accessPolicy = new AccessPolicy();
        accessPolicy.setCode("Saving transfer option - {}");
        accessPolicy.setId(1L);
        accessPolicy.setIsCustom(1);
        accessPolicy.setName("Saving transfer option - {}");

        DashboardContents dashboardContents = new DashboardContents();
        dashboardContents.setContent("Not all who wander are lost");
        dashboardContents.setId(1L);

        DataSource dataSource = new DataSource();
        dataSource.setCode("Saving transfer option - {}");
        dataSource.setId(1L);
        dataSource.setIsCustom(1);
        dataSource.setName("Saving transfer option - {}");

        DefinitionType definitionType = new DefinitionType();
        definitionType.setCode(1);
        definitionType.setId(1L);
        definitionType.setName("Saving transfer option - {}");

        DataSource displayDataSource = new DataSource();
        displayDataSource.setCode("Saving transfer option - {}");
        displayDataSource.setId(1L);
        displayDataSource.setIsCustom(1);
        displayDataSource.setName("Saving transfer option - {}");

        Organisation organisation = new Organisation();
        organisation.setContact("Saving transfer option - {}");
        organisation.setContactEmail("jane.doe@example.org");
        organisation.setEmail("jane.doe@example.org");
        organisation.setId(1L);
        organisation.setIsCustom(1);
        organisation.setTitle("Dr");

        Source source = new Source();
        source.setCode("Saving transfer option - {}");
        source.setId(1L);
        source.setIsCustom(1);
        source.setTerm("Saving transfer option - {}");

        Type type = new Type();
        type.setCode("Saving transfer option - {}");
        type.setId(1L);
        type.setIsCustom(1);
        type.setName("Saving transfer option - {}");

        Entry entry = new Entry();
        entry.setAccessPolicy(accessPolicy);
        entry.setCode("Saving transfer option - {}");
        entry.setCoverage("Saving transfer option - {}");
        entry.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        entry.setDashboardContents(dashboardContents);
        entry.setDataSource(dataSource);
        entry.setDefinitionType(definitionType);
        entry.setDeleted(1);
        entry.setDisplayDataSource(displayDataSource);
        entry.setId(1L);
        entry.setKeywords("Saving transfer option - {}");
        entry.setLogo("Saving transfer option - {}");
        entry.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        entry.setOrganisation(organisation);
        entry.setPublishDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        entry.setScoreWeight(10.0d);
        entry.setSource(source);
        entry.setSummary("Saving transfer option - {}");
        entry.setTags("Saving transfer option - {}");
        entry.setTitle("Dr");
        entry.setType(type);
        entry.setWorkflowInstanceId(1L);

        Protocol protocol = new Protocol();
        protocol.setId(1L);
        protocol.setIsCustom(1);
        protocol.setValue("42");

        TransferOption transferOption = new TransferOption();
        transferOption.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        transferOption.setDeleted(1);
        transferOption.setDescription("The characteristics of someone or something");
        transferOption.setEndpoint(endpoint);
        transferOption.setEntry(entry);
        transferOption.setId(1L);
        transferOption.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        transferOption.setName("Saving transfer option - {}");
        transferOption.setProtocol(protocol);
        transferOption.setTitle("Dr");

        HashSet<TransferOption> transferOptionSet = new HashSet<>();
        transferOptionSet.add(transferOption);
        ArrayList<TransferOption> transferOptionList = new ArrayList<>();
        when(transferOptionRepository.saveAll(Mockito.<Iterable<TransferOption>>any())).thenReturn(transferOptionList);
        when(transferOptionRepository.findByEntryId(anyLong())).thenReturn(transferOptionSet);
        ArrayList<TransferOptionModel> transferOptionDto = new ArrayList<>();

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

        Entry relatedEntry = new Entry();
        relatedEntry.setAccessPolicy(accessPolicy2);
        relatedEntry.setCode("Code");
        relatedEntry.setCoverage("Coverage");
        relatedEntry.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        relatedEntry.setDashboardContents(dashboardContents2);
        relatedEntry.setDataSource(dataSource2);
        relatedEntry.setDefinitionType(definitionType2);
        relatedEntry.setDeleted(1);
        relatedEntry.setDisplayDataSource(displayDataSource2);
        relatedEntry.setId(1L);
        relatedEntry.setKeywords("Keywords");
        relatedEntry.setLogo("Logo");
        relatedEntry.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        relatedEntry.setOrganisation(organisation2);
        relatedEntry.setPublishDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        relatedEntry.setScoreWeight(10.0d);
        relatedEntry.setSource(source2);
        relatedEntry.setSummary("Summary");
        relatedEntry.setTags("Tags");
        relatedEntry.setTitle("Dr");
        relatedEntry.setType(type2);
        relatedEntry.setWorkflowInstanceId(1L);
        List<TransferOption> actualSaveTransferOptionsResult = transferOptionServiceImpl
                .saveTransferOptions(transferOptionDto, relatedEntry);
        verify(transferOptionMapper, new AtLeast(0)).mapToTransferOption(Mockito.any(), anyLong());
        verify(transferOptionRepository).saveAll(Mockito.<Iterable<TransferOption>>any());
        assertTrue(actualSaveTransferOptionsResult.isEmpty());
        assertIterableEquals(transferOptionList, actualSaveTransferOptionsResult);
    }

    /**
     * Test save transfer options 3.
     */
    @Test
    void testSaveTransferOptions3() {
        Endpoint endpoint = new Endpoint();
        endpoint.setId(1L);
        endpoint.setIsCustom(1);
        endpoint.setUrl("https://example.org/example");
        endpoint.setUrlType("https://example.org/example");

        AccessPolicy accessPolicy = new AccessPolicy();
        accessPolicy.setCode("Saving transfer option - {}");
        accessPolicy.setId(1L);
        accessPolicy.setIsCustom(1);
        accessPolicy.setName("Saving transfer option - {}");

        DashboardContents dashboardContents = new DashboardContents();
        dashboardContents.setContent("Not all who wander are lost");
        dashboardContents.setId(1L);

        DataSource dataSource = new DataSource();
        dataSource.setCode("Saving transfer option - {}");
        dataSource.setId(1L);
        dataSource.setIsCustom(1);
        dataSource.setName("Saving transfer option - {}");

        DefinitionType definitionType = new DefinitionType();
        definitionType.setCode(1);
        definitionType.setId(1L);
        definitionType.setName("Saving transfer option - {}");

        DataSource displayDataSource = new DataSource();
        displayDataSource.setCode("Saving transfer option - {}");
        displayDataSource.setId(1L);
        displayDataSource.setIsCustom(1);
        displayDataSource.setName("Saving transfer option - {}");

        Organisation organisation = new Organisation();
        organisation.setContact("Saving transfer option - {}");
        organisation.setContactEmail("jane.doe@example.org");
        organisation.setEmail("jane.doe@example.org");
        organisation.setId(1L);
        organisation.setIsCustom(1);
        organisation.setTitle("Dr");

        Source source = new Source();
        source.setCode("Saving transfer option - {}");
        source.setId(1L);
        source.setIsCustom(1);
        source.setTerm("Saving transfer option - {}");

        Type type = new Type();
        type.setCode("Saving transfer option - {}");
        type.setId(1L);
        type.setIsCustom(1);
        type.setName("Saving transfer option - {}");

        Entry entry = new Entry();
        entry.setAccessPolicy(accessPolicy);
        entry.setCode("Saving transfer option - {}");
        entry.setCoverage("Saving transfer option - {}");
        entry.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        entry.setDashboardContents(dashboardContents);
        entry.setDataSource(dataSource);
        entry.setDefinitionType(definitionType);
        entry.setDeleted(1);
        entry.setDisplayDataSource(displayDataSource);
        entry.setId(1L);
        entry.setKeywords("Saving transfer option - {}");
        entry.setLogo("Saving transfer option - {}");
        entry.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        entry.setOrganisation(organisation);
        entry.setPublishDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        entry.setScoreWeight(10.0d);
        entry.setSource(source);
        entry.setSummary("Saving transfer option - {}");
        entry.setTags("Saving transfer option - {}");
        entry.setTitle("Dr");
        entry.setType(type);
        entry.setWorkflowInstanceId(1L);

        Protocol protocol = new Protocol();
        protocol.setId(1L);
        protocol.setIsCustom(1);
        protocol.setValue("42");

        TransferOption transferOption = new TransferOption();
        transferOption.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        transferOption.setDeleted(1);
        transferOption.setDescription("The characteristics of someone or something");
        transferOption.setEndpoint(endpoint);
        transferOption.setEntry(entry);
        transferOption.setId(1L);
        transferOption.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        transferOption.setName("Saving transfer option - {}");
        transferOption.setProtocol(protocol);
        transferOption.setTitle("Dr");

        Endpoint endpoint2 = new Endpoint();
        endpoint2.setId(2L);
        endpoint2.setIsCustom(0);
        endpoint2.setUrl("Saving transfer option - {}");
        endpoint2.setUrlType("Saving transfer option - {}");

        AccessPolicy accessPolicy2 = new AccessPolicy();
        accessPolicy2.setCode("Transfer options Saved");
        accessPolicy2.setId(2L);
        accessPolicy2.setIsCustom(0);
        accessPolicy2.setName("Transfer options Saved");

        DashboardContents dashboardContents2 = new DashboardContents();
        dashboardContents2.setContent("Saving transfer option - {}");
        dashboardContents2.setId(2L);

        DataSource dataSource2 = new DataSource();
        dataSource2.setCode("Transfer options Saved");
        dataSource2.setId(2L);
        dataSource2.setIsCustom(0);
        dataSource2.setName("Transfer options Saved");

        DefinitionType definitionType2 = new DefinitionType();
        definitionType2.setCode(0);
        definitionType2.setId(2L);
        definitionType2.setName("Transfer options Saved");

        DataSource displayDataSource2 = new DataSource();
        displayDataSource2.setCode("Transfer options Saved");
        displayDataSource2.setId(2L);
        displayDataSource2.setIsCustom(0);
        displayDataSource2.setName("Transfer options Saved");

        Organisation organisation2 = new Organisation();
        organisation2.setContact("Transfer options Saved");
        organisation2.setContactEmail("john.smith@example.org");
        organisation2.setEmail("john.smith@example.org");
        organisation2.setId(2L);
        organisation2.setIsCustom(0);
        organisation2.setTitle("Mr");

        Source source2 = new Source();
        source2.setCode("Transfer options Saved");
        source2.setId(2L);
        source2.setIsCustom(0);
        source2.setTerm("Transfer options Saved");

        Type type2 = new Type();
        type2.setCode("Transfer options Saved");
        type2.setId(2L);
        type2.setIsCustom(0);
        type2.setName("Transfer options Saved");

        Entry entry2 = new Entry();
        entry2.setAccessPolicy(accessPolicy2);
        entry2.setCode("Transfer options Saved");
        entry2.setCoverage("Transfer options Saved");
        entry2.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        entry2.setDashboardContents(dashboardContents2);
        entry2.setDataSource(dataSource2);
        entry2.setDefinitionType(definitionType2);
        entry2.setDeleted(0);
        entry2.setDisplayDataSource(displayDataSource2);
        entry2.setId(2L);
        entry2.setKeywords("Transfer options Saved");
        entry2.setLogo("Transfer options Saved");
        entry2.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        entry2.setOrganisation(organisation2);
        entry2.setPublishDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        entry2.setScoreWeight(2.0d);
        entry2.setSource(source2);
        entry2.setSummary("Transfer options Saved");
        entry2.setTags("Transfer options Saved");
        entry2.setTitle("Mr");
        entry2.setType(type2);
        entry2.setWorkflowInstanceId(2L);

        Protocol protocol2 = new Protocol();
        protocol2.setId(2L);
        protocol2.setIsCustom(0);
        protocol2.setValue("Saving transfer option - {}");

        TransferOption transferOption2 = new TransferOption();
        transferOption2.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        transferOption2.setDeleted(0);
        transferOption2.setDescription("Saving transfer option - {}");
        transferOption2.setEndpoint(endpoint2);
        transferOption2.setEntry(entry2);
        transferOption2.setId(2L);
        transferOption2.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        transferOption2.setName("Transfer options Saved");
        transferOption2.setProtocol(protocol2);
        transferOption2.setTitle("Mr");

        HashSet<TransferOption> transferOptionSet = new HashSet<>();
        transferOptionSet.add(transferOption2);
        transferOptionSet.add(transferOption);
        ArrayList<TransferOption> transferOptionList = new ArrayList<>();
        when(transferOptionRepository.saveAll(Mockito.<Iterable<TransferOption>>any())).thenReturn(transferOptionList);
        when(transferOptionRepository.findByEntryId(anyLong())).thenReturn(transferOptionSet);
        ArrayList<TransferOptionModel> transferOptionDto = new ArrayList<>();

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

        Entry relatedEntry = new Entry();
        relatedEntry.setAccessPolicy(accessPolicy3);
        relatedEntry.setCode("Code");
        relatedEntry.setCoverage("Coverage");
        relatedEntry.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        relatedEntry.setDashboardContents(dashboardContents3);
        relatedEntry.setDataSource(dataSource3);
        relatedEntry.setDefinitionType(definitionType3);
        relatedEntry.setDeleted(1);
        relatedEntry.setDisplayDataSource(displayDataSource3);
        relatedEntry.setId(1L);
        relatedEntry.setKeywords("Keywords");
        relatedEntry.setLogo("Logo");
        relatedEntry.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        relatedEntry.setOrganisation(organisation3);
        relatedEntry.setPublishDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        relatedEntry.setScoreWeight(10.0d);
        relatedEntry.setSource(source3);
        relatedEntry.setSummary("Summary");
        relatedEntry.setTags("Tags");
        relatedEntry.setTitle("Dr");
        relatedEntry.setType(type3);
        relatedEntry.setWorkflowInstanceId(1L);
        List<TransferOption> actualSaveTransferOptionsResult = transferOptionServiceImpl
                .saveTransferOptions(transferOptionDto, relatedEntry);
        verify(transferOptionMapper, new AtLeast(0)).mapToTransferOption(Mockito.any(), anyLong());
        verify(transferOptionRepository).saveAll(Mockito.<Iterable<TransferOption>>any());
        assertTrue(actualSaveTransferOptionsResult.isEmpty());
        assertIterableEquals(transferOptionList, actualSaveTransferOptionsResult);
    }

    /**
     * Test save transfer options 4.
     */
    @Test
    void testSaveTransferOptions4() {
        Endpoint endpoint = new Endpoint();
        endpoint.setId(1L);
        endpoint.setIsCustom(1);
        endpoint.setUrl("https://example.org/example");
        endpoint.setUrlType("https://example.org/example");

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

        Protocol protocol = new Protocol();
        protocol.setId(1L);
        protocol.setIsCustom(1);
        protocol.setValue("42");

        TransferOption transferOption = new TransferOption();
        transferOption.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        transferOption.setDeleted(1);
        transferOption.setDescription("The characteristics of someone or something");
        transferOption.setEndpoint(endpoint);
        transferOption.setEntry(entry);
        transferOption.setId(1L);
        transferOption.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        transferOption.setName("Name");
        transferOption.setProtocol(protocol);
        transferOption.setTitle("Dr");
        when(transferOptionMapper.mapToTransferOption(Mockito.<TransferOptionModel>any(), Mockito.<Entry>any()))
                .thenReturn(transferOption);

        ArrayList<TransferOption> transferOptionList = new ArrayList<>();
        transferOptionList.add(transferOption);
        when(transferOptionRepository.saveAll(Mockito.<Iterable<TransferOption>>any())).thenReturn(transferOptionList);
        when(transferOptionRepository.findByEntryId(anyLong())).thenReturn(new HashSet<>());

        ProtocolModel protocol2 = new ProtocolModel();
        protocol2.setValue("42");

        TransferOptionModel transferOptionModel = new TransferOptionModel();
        transferOptionModel.setDescription("The characteristics of someone or something");
        transferOptionModel.setEndpoint(
                new EndpointModel("https://example.org/example", "https://example.org/example"));
        transferOptionModel.setName("Saving transfer option - {}");
        transferOptionModel.setProtocol(protocol2);
        transferOptionModel.setTitle("Dr");

        ArrayList<TransferOptionModel> transferOptionDto = new ArrayList<>();
        transferOptionDto.add(transferOptionModel);

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

        Entry relatedEntry = new Entry();
        relatedEntry.setAccessPolicy(accessPolicy2);
        relatedEntry.setCode("Code");
        relatedEntry.setCoverage("Coverage");
        relatedEntry.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        relatedEntry.setDashboardContents(dashboardContents2);
        relatedEntry.setDataSource(dataSource2);
        relatedEntry.setDefinitionType(definitionType2);
        relatedEntry.setDeleted(1);
        relatedEntry.setDisplayDataSource(displayDataSource2);
        relatedEntry.setId(1L);
        relatedEntry.setKeywords("Keywords");
        relatedEntry.setLogo("Logo");
        relatedEntry.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        relatedEntry.setOrganisation(organisation2);
        relatedEntry.setPublishDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        relatedEntry.setScoreWeight(10.0d);
        relatedEntry.setSource(source2);
        relatedEntry.setSummary("Summary");
        relatedEntry.setTags("Tags");
        relatedEntry.setTitle("Dr");
        relatedEntry.setType(type2);
        relatedEntry.setWorkflowInstanceId(1L);
        List<TransferOption> actualSaveTransferOptionsResult = transferOptionServiceImpl
                .saveTransferOptions(transferOptionDto, relatedEntry);
        verify(transferOptionMapper).mapToTransferOption(Mockito.<TransferOptionModel>any(), Mockito.<Entry>any());
        verify(transferOptionRepository).saveAll(Mockito.<Iterable<TransferOption>>any());
        assertFalse(actualSaveTransferOptionsResult.isEmpty());
        assertIterableEquals(transferOptionList, actualSaveTransferOptionsResult);
    }

    /**
     * Test remove transfer option.
     */
    @Test
    void testRemoveTransferOption() {
        Endpoint endpoint = new Endpoint();
        endpoint.setId(1L);
        endpoint.setIsCustom(1);
        endpoint.setUrl("https://example.org/example");
        endpoint.setUrlType("https://example.org/example");

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

        Protocol protocol = new Protocol();
        protocol.setId(1L);
        protocol.setIsCustom(1);
        protocol.setValue("42");

        TransferOption transferOption = new TransferOption();
        transferOption.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        transferOption.setDeleted(1);
        transferOption.setDescription("The characteristics of someone or something");
        transferOption.setEndpoint(endpoint);
        transferOption.setEntry(entry);
        transferOption.setId(1L);
        transferOption.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        transferOption.setName("Name");
        transferOption.setProtocol(protocol);
        transferOption.setTitle("Dr");
        Optional<TransferOption> ofResult = Optional.of(transferOption);

        Endpoint endpoint2 = new Endpoint();
        endpoint2.setId(1L);
        endpoint2.setIsCustom(1);
        endpoint2.setUrl("https://example.org/example");
        endpoint2.setUrlType("https://example.org/example");

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

        Protocol protocol2 = new Protocol();
        protocol2.setId(1L);
        protocol2.setIsCustom(1);
        protocol2.setValue("42");

        TransferOption transferOption2 = new TransferOption();
        transferOption2.setCreateDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        transferOption2.setDeleted(1);
        transferOption2.setDescription("The characteristics of someone or something");
        transferOption2.setEndpoint(endpoint2);
        transferOption2.setEntry(entry2);
        transferOption2.setId(1L);
        transferOption2.setModifiedDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        transferOption2.setName("Name");
        transferOption2.setProtocol(protocol2);
        transferOption2.setTitle("Dr");
        when(transferOptionRepository.save(Mockito.<TransferOption>any())).thenReturn(transferOption2);
        when(transferOptionRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        transferOptionServiceImpl.removeTransferOption(1L);
        verify(transferOptionRepository).findById(Mockito.<Long>any());
        verify(transferOptionRepository).save(Mockito.<TransferOption>any());
    }

    /**
     * Test delete transfer option.
     */
    @Test
    void testDeleteTransferOption() {
        doNothing().when(transferOptionRepository).deleteById(Mockito.<Long>any());
        transferOptionServiceImpl.deleteTransferOption(1L);
        verify(transferOptionRepository).deleteById(Mockito.<Long>any());
    }
}
