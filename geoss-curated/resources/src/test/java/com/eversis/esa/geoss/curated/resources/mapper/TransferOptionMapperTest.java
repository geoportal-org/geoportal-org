package com.eversis.esa.geoss.curated.resources.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.eversis.esa.geoss.curated.resources.model.TransferOptionModel;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * The type Transfer option mapper test.
 */
@ContextConfiguration(classes = {TransferOptionMapper.class})
@ExtendWith(SpringExtension.class)
class TransferOptionMapperTest {

    @MockBean
    private EndpointService endpointService;

    @MockBean
    private ProtocolService protocolService;

    @Autowired
    private TransferOptionMapper transferOptionMapper;

    /**
     * Method under test: {@link TransferOptionMapper#mapToTransferOption(TransferOptionModel, Entry)}
     */
    @Test
    void testMapToTransferOption() {
        Endpoint endpoint = new Endpoint();
        endpoint.setId(1L);
        endpoint.setIsCustom(1);
        endpoint.setUrl("https://example.org/example");
        endpoint.setUrlType("https://example.org/example");
        when(endpointService.getOrCreateEndpoint(Mockito.<EndpointModel>any())).thenReturn(endpoint);

        Protocol protocol = new Protocol();
        protocol.setId(1L);
        protocol.setIsCustom(1);
        protocol.setValue("42");
        when(protocolService.getOrCreateProtocol(Mockito.<ProtocolModel>any())).thenReturn(protocol);

        ProtocolModel protocol2 = new ProtocolModel();
        protocol2.setValue("42");

        TransferOptionModel model = new TransferOptionModel();
        model.setDescription("The characteristics of someone or something");
        model.setEndpoint(new EndpointModel("https://example.org/example", "https://example.org/example"));
        model.setName("Name");
        model.setProtocol(protocol2);
        model.setTitle("Dr");

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
        TransferOption actualMapToTransferOptionResult = transferOptionMapper.mapToTransferOption(model, relatedEntry);
        verify(endpointService).getOrCreateEndpoint(Mockito.<EndpointModel>any());
        verify(protocolService).getOrCreateProtocol(Mockito.<ProtocolModel>any());
        assertEquals("Dr", actualMapToTransferOptionResult.getTitle());
        assertEquals("Name", actualMapToTransferOptionResult.getName());
        assertEquals("The characteristics of someone or something", actualMapToTransferOptionResult.getDescription());
        assertEquals(0, actualMapToTransferOptionResult.getDeleted().intValue());
        assertSame(endpoint, actualMapToTransferOptionResult.getEndpoint());
        assertSame(protocol, actualMapToTransferOptionResult.getProtocol());
        assertSame(relatedEntry, actualMapToTransferOptionResult.getEntry());
    }
}
