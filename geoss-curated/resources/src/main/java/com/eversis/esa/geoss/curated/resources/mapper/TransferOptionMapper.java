package com.eversis.esa.geoss.curated.resources.mapper;

import com.eversis.esa.geoss.curated.common.service.EndpointService;
import com.eversis.esa.geoss.curated.common.service.ProtocolService;
import com.eversis.esa.geoss.curated.resources.domain.Entry;
import com.eversis.esa.geoss.curated.resources.domain.TransferOption;
import com.eversis.esa.geoss.curated.resources.model.TransferOptionModel;

import org.springframework.stereotype.Component;

/**
 * The type Transfer option mapper.
 */
@Component
public class TransferOptionMapper {

    private final EndpointService endpointService;
    private final ProtocolService protocolService;

    /**
     * Instantiates a new Transfer option mapper.
     *
     * @param endpointService the endpoint service
     * @param protocolService the protocol service
     */
    public TransferOptionMapper(EndpointService endpointService, ProtocolService protocolService) {
        this.endpointService = endpointService;
        this.protocolService = protocolService;
    }

    /**
     * Map to transfer option transfer option.
     *
     * @param model the model
     * @param relatedEntry the related entry
     * @return the transfer option
     */
    public TransferOption mapToTransferOption(TransferOptionModel model, Entry relatedEntry) {
        return getTransferOption(model, relatedEntry);
    }

    private TransferOption getTransferOption(TransferOptionModel model, Entry relatedEntry) {
        if (model == null) {
            return null;
        }
        TransferOption transferOption = new TransferOption();
        if (model.getId() != null || model.getId() != 0) {
            transferOption.setId(model.getId());
        }
        transferOption.setName(model.getName());
        transferOption.setDescription(model.getDescription());
        transferOption.setTitle(model.getTitle());
        transferOption.setEntry(relatedEntry);
        transferOption.setEndpoint(endpointService.getOrCreateEndpoint(model.getEndpoint()));
        transferOption.setProtocol(protocolService.getOrCreateProtocol(model.getProtocol()));
        transferOption.setDeleted(0);
        return transferOption;
    }

}
