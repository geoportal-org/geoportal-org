package com.eversis.esa.geoss.curated.extensions.mapper;

import com.eversis.esa.geoss.curated.common.service.EndpointService;
import com.eversis.esa.geoss.curated.common.service.ProtocolService;
import com.eversis.esa.geoss.curated.extensions.domain.EntryExtension;
import com.eversis.esa.geoss.curated.extensions.domain.TransferOptionExtension;
import com.eversis.esa.geoss.curated.extensions.model.TransferOptionExtensionModel;

import org.springframework.stereotype.Component;

/**
 * The type Transfer option extension mapper.
 */
@Component
public class TransferOptionExtensionMapper {

    private final EndpointService endpointService;
    private final ProtocolService protocolService;

    /**
     * Instantiates a new Transfer option extension mapper.
     *
     * @param endpointService the endpoint service
     * @param protocolService the protocol service
     */
    public TransferOptionExtensionMapper(EndpointService endpointService, ProtocolService protocolService) {
        this.endpointService = endpointService;
        this.protocolService = protocolService;
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

}
