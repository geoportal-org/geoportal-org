package com.eversis.esa.geoss.curated.resources.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import jakarta.validation.constraints.NotNull;

import com.eversis.esa.geoss.curated.common.service.EndpointService;
import com.eversis.esa.geoss.curated.common.service.ProtocolService;
import com.eversis.esa.geoss.curated.resources.domain.Entry;
import com.eversis.esa.geoss.curated.resources.domain.TransferOption;
import com.eversis.esa.geoss.curated.resources.mapper.TransferOptionMapper;
import com.eversis.esa.geoss.curated.resources.model.TransferOptionModel;
import com.eversis.esa.geoss.curated.resources.repository.TransferOptionRepository;
import com.eversis.esa.geoss.curated.resources.service.TransferOptionService;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 * The type Transfer option service.
 */
@Log4j2
@Service
@Transactional(readOnly = true)
@Validated
public class TransferOptionServiceImpl implements TransferOptionService {

    private final TransferOptionRepository transferOptionRepository;

    private final TransferOptionMapper transferOptionMapper;

    private final EndpointService endpointService;

    private final ProtocolService protocolService;

    /**
     * Instantiates a new Transfer option service.
     *
     * @param transferOptionRepository the transfer option repository
     * @param transferOptionMapper the transfer option mapper
     * @param endpointService the endpoint service
     * @param protocolService the protocol service
     */
    public TransferOptionServiceImpl(TransferOptionRepository transferOptionRepository,
            TransferOptionMapper transferOptionMapper, EndpointService endpointService,
            ProtocolService protocolService) {
        this.transferOptionRepository = transferOptionRepository;
        this.transferOptionMapper = transferOptionMapper;
        this.endpointService = endpointService;
        this.protocolService = protocolService;
    }

    @Override
    public Page<TransferOption> findTransferOptions(@NotNull Pageable pageable) {
        log.info("Finding transfer options");
        return transferOptionRepository.findAll(pageable);
    }

    @Override
    public TransferOption findTransferOption(long transferOptionId) {
        log.info("Finding transfer option with id {}", transferOptionId);
        return transferOptionRepository.findById(transferOptionId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "TransferOption entity with id: " + transferOptionId + " does not exist"));
    }

    @Override
    public Set<TransferOption> findTransferOptionsByEntryId(long entryId) {
        log.info("Finding transfer options by entry id");
        return transferOptionRepository.findByEntryId(entryId);
    }

    @Transactional
    @Override
    public List<TransferOption> saveTransferOptions(
            @NonNull List<TransferOptionModel> transferOptionDto, @NonNull Entry relatedEntry) {
        log.info("Saving transfer option - {}", transferOptionDto);
        List<TransferOption> transferOptions = transferOptionDto.stream()
                .map(option -> transferOptionMapper.mapToTransferOption(option, relatedEntry))
                .collect(Collectors.toList());
        transferOptionRepository.saveAll(transferOptions);
        log.info("Transfer options Saved");
        return transferOptions;
    }

    @Transactional
    @Override
    public void saveTransferOption(TransferOption transferOption) {
        log.info("Saving transfer option - {}", transferOption);
        transferOptionRepository.save(transferOption);
        log.info("Transfer option Saved");
    }

    @Transactional
    @Override
    public void removeTransferOption(long transferOptionId) {
        log.info("Removing transfer option with id {}", transferOptionId);
        final TransferOption transferOption = transferOptionRepository.findById(transferOptionId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "TransferOption entity with id: " + transferOptionId + " does not exist"));
        transferOption.setDeleted(1);
        transferOptionRepository.save(transferOption);
        log.info("Removed transfer option with id: {}", transferOption.getId());
    }

    @Transactional
    @Override
    public void deleteTransferOption(long transferOptionId) {
        log.info("Deleting transfer option with id: {}", transferOptionId);
        transferOptionRepository.deleteById(transferOptionId);
        log.info("Deleted transfer option with id: {}", transferOptionId);
    }

    @Transactional
    @Override
    public void updateTransferOptionsByEntryId(long entryId, Set<TransferOptionModel> transferOptions) {
        log.info("Updating transfer options by entry id: {}", entryId);

        Set<TransferOption> currentTransferOptions = findTransferOptionsByEntryId(entryId);
        Set<Long> newIds = new HashSet<>();

        transferOptions.forEach(transferOptionModel -> {
            if (transferOptionModel.getId() != null) {
                final TransferOption transferOption =
                        transferOptionRepository.findById(transferOptionModel.getId()).orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "TransferOption entity with id: " + transferOptionModel.getId()
                                                + " does not exist"));
                transferOption.setName(transferOptionModel.getName());
                transferOption.setDescription(transferOptionModel.getDescription());
                transferOption.setTitle(transferOptionModel.getTitle());
                transferOption.setEndpoint(endpointService.getOrCreateEndpoint(transferOptionModel.getEndpoint()));
                transferOption.setProtocol(protocolService.getOrCreateProtocol(transferOptionModel.getProtocol()));
                saveTransferOption(transferOption);
                newIds.add(transferOption.getId());
            } else {
                TransferOption option =
                        createTransferOption(transferOptionMapper.mapToTransferOption(transferOptionModel, entryId));
                newIds.add(option.getId());
            }
        });

        List<TransferOption> optionsToRemove = currentTransferOptions.stream()
                .filter(option -> !newIds.contains(option.getId()))
                .collect(Collectors.toList());

        optionsToRemove.forEach(transferOption -> {
            deleteTransferOption(transferOption.getId());
        });

    }

    @Transactional
    @Override
    public TransferOption createTransferOption(TransferOption transferOption) {
        log.info("Creating transfer option - {}", transferOption);
        return transferOptionRepository.save(transferOption);
    }

}
