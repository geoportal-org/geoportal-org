package com.eversis.esa.geoss.curated.extensions.mapper;

import com.eversis.esa.geoss.curated.common.service.EndpointService;
import com.eversis.esa.geoss.curated.common.service.ProtocolService;
import com.eversis.esa.geoss.curated.extensions.domain.EntryExtension;
import com.eversis.esa.geoss.curated.extensions.domain.TransferOptionExtension;
import com.eversis.esa.geoss.curated.extensions.model.TransferOptionExtensionModel;
import com.eversis.esa.geoss.curated.extensions.service.ExtensionService;

import org.springframework.stereotype.Component;

/**
 * The TransferOptionExtensionMapper class. This class is responsible for mapping between the
 * TransferOptionExtensionModel and the TransferOptionExtension entity. It uses the EndpointService and ProtocolService
 * to get or create Endpoint and Protocol entities, and the EntryExtensionService to find EntryExtension entities. The
 * class is annotated with @Component, meaning it is a Spring bean and can be autowired into other beans. It has methods
 * for mapping a TransferOptionExtensionModel to a TransferOptionExtension, either with an EntryExtension entity or with
 * an extension ID. These methods return a new TransferOptionExtension entity with the properties set from the model and
 * the services.
 */
@Component
public class TransferOptionExtensionMapper {

    private final EndpointService endpointService;
    private final ProtocolService protocolService;
    private final ExtensionService extensionService;

    /**
     * Instantiates a new Transfer option extension mapper.
     *
     * @param endpointService the endpoint service
     * @param protocolService the protocol service
     * @param extensionService the extension service
     */
    public TransferOptionExtensionMapper(EndpointService endpointService, ProtocolService protocolService,
            ExtensionService extensionService) {
        this.endpointService = endpointService;
        this.protocolService = protocolService;
        this.extensionService = extensionService;
    }

    /**
     * Map to transfer option extension transfer option extension.
     *
     * @param model the model
     * @param entryExtension the entry extension
     * @return the transfer option extension
     */
    public TransferOptionExtension mapToTransferOptionExtension(TransferOptionExtensionModel model,
            EntryExtension entryExtension) {
        return getTransferOptionExtension(model, entryExtension);
    }

    /**
     * Map to transfer option extension transfer option extension.
     *
     * @param transferOptionExtensionModel the transfer option extension model
     * @param extensionId the extension id
     * @return the transfer option extension
     */
    public TransferOptionExtension mapToTransferOptionExtension(
            TransferOptionExtensionModel transferOptionExtensionModel,
            long extensionId) {
        return getTransferOptionExtension(transferOptionExtensionModel, extensionId);
    }

    private TransferOptionExtension getTransferOptionExtension(TransferOptionExtensionModel model,
            EntryExtension entryExtension) {
        if (model == null) {
            return null;
        }
        TransferOptionExtension transferOptionExtension = new TransferOptionExtension();
        transferOptionExtension.setName(model.getName());
        transferOptionExtension.setDescription(model.getDescription());
        transferOptionExtension.setDisplayTitle(model.getDisplayTitle());
        transferOptionExtension.setEntryExtension(entryExtension);
        transferOptionExtension.setEndpoint(endpointService.getOrCreateEndpoint(model.getEndpoint()));
        transferOptionExtension.setProtocol(protocolService.getOrCreateProtocol(model.getProtocol()));
        transferOptionExtension.setDeleted(0);
        return transferOptionExtension;
    }

    private TransferOptionExtension getTransferOptionExtension(TransferOptionExtensionModel model,
            long extensionId) {
        if (model == null) {
            return null;
        }
        TransferOptionExtension transferOptionExtension = new TransferOptionExtension();
        transferOptionExtension.setName(model.getName());
        transferOptionExtension.setDescription(model.getDescription());
        transferOptionExtension.setDisplayTitle(model.getDisplayTitle());
        transferOptionExtension.setEntryExtension(extensionService.findExtension(extensionId));
        transferOptionExtension.setEndpoint(endpointService.getOrCreateEndpoint(model.getEndpoint()));
        transferOptionExtension.setProtocol(protocolService.getOrCreateProtocol(model.getProtocol()));
        transferOptionExtension.setDeleted(0);
        return transferOptionExtension;
    }

}
