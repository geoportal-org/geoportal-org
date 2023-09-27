package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.service;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.model.Entry;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.model.TransferOption;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.repository.TransferOptionRepository;

import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The type Transfer option service.
 */
@Service
public class TransferOptionService {

    private final TransferOptionRepository transferOptionRepository;
    private final EndpointService endpointService;
    private final ProtocolService protocolService;

    /**
     * Instantiates a new Transfer option service.
     *
     * @param transferOptionRepository the transfer option repository
     * @param endpointService the endpoint service
     * @param protocolService the protocol service
     */
    public TransferOptionService(
            TransferOptionRepository transferOptionRepository,
            EndpointService endpointService,
            ProtocolService protocolService) {
        this.transferOptionRepository = transferOptionRepository;
        this.endpointService = endpointService;
        this.protocolService = protocolService;
    }

    /**
     * Save transfer options list.
     *
     * @param relatedEntry the related entry
     * @param domainTransferOptions the domain transfer options
     * @return the list
     */
    public List<TransferOption> saveTransferOptions(
            @NonNull Entry relatedEntry,
            @NonNull List<com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.TransferOption>
                    domainTransferOptions) {

        Set<TransferOption> existingTransferOptions = transferOptionRepository.findByEntryId(relatedEntry.getId());
        Set<TransferOption> newTransferOptions = domainTransferOptions.stream()
                .map(transferOption -> createTransferOption(relatedEntry, transferOption))
                .collect(Collectors.toSet());

        existingTransferOptions.addAll(newTransferOptions);
        existingTransferOptions.forEach(transferOption -> transferOption.setModifiedDate(LocalDateTime.now()));
        return transferOptionRepository.saveAll(existingTransferOptions);
    }

    private TransferOption createTransferOption(
            Entry relatedEntry,
            com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.TransferOption
                    domainTransferOption) {
        TransferOption dbTransferOption = new TransferOption();
        dbTransferOption.setName(domainTransferOption.getName());
        dbTransferOption.setTitle(domainTransferOption.getTitle());
        dbTransferOption.setDescription(domainTransferOption.getDescription());
        dbTransferOption.setProtocol(protocolService.getOrCreateProtocol(domainTransferOption.getProtocol()));
        dbTransferOption.setEndpoint(endpointService.getOrCreateEndpoint(domainTransferOption.getEndpoint()));
        dbTransferOption.setEntry(relatedEntry);
        return dbTransferOption;
    }

}
