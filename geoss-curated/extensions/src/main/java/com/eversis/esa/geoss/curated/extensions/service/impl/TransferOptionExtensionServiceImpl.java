package com.eversis.esa.geoss.curated.extensions.service.impl;

import com.eversis.esa.geoss.curated.common.service.EndpointService;
import com.eversis.esa.geoss.curated.common.service.ProtocolService;
import com.eversis.esa.geoss.curated.extensions.domain.EntryExtension;
import com.eversis.esa.geoss.curated.extensions.domain.TransferOptionExtension;
import com.eversis.esa.geoss.curated.extensions.mapper.TransferOptionExtensionMapper;
import com.eversis.esa.geoss.curated.extensions.model.TransferOptionExtensionModel;
import com.eversis.esa.geoss.curated.extensions.repository.TransferOptionExtensionRepository;
import com.eversis.esa.geoss.curated.extensions.service.TransferOptionExtensionService;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The type Transfer option extension service.
 */
@Log4j2
@Service
@Transactional(readOnly = true)
@Validated
public class TransferOptionExtensionServiceImpl implements TransferOptionExtensionService {

    private final TransferOptionExtensionRepository transferOptionExtensionRepository;

    private final TransferOptionExtensionMapper transferOptionExtensionMapper;

    private final EndpointService endpointService;

    private final ProtocolService protocolService;

    /**
     * Instantiates a new Transfer option extension service.
     *
     * @param transferOptionExtensionRepository the transfer option extension repository
     * @param transferOptionExtensionMapper the transfer option extension mapper
     * @param endpointService the endpoint service
     * @param protocolService the protocol service
     */
    public TransferOptionExtensionServiceImpl(TransferOptionExtensionRepository transferOptionExtensionRepository,
            TransferOptionExtensionMapper transferOptionExtensionMapper, EndpointService endpointService,
            ProtocolService protocolService) {
        this.transferOptionExtensionRepository = transferOptionExtensionRepository;
        this.transferOptionExtensionMapper = transferOptionExtensionMapper;
        this.endpointService = endpointService;
        this.protocolService = protocolService;
    }

    @Override
    public Page<TransferOptionExtension> findTransferOptionExtensions(@NotNull Pageable pageable) {
        log.info("Finding transfer option extemsions");
        return transferOptionExtensionRepository.findAll(pageable);
    }

    @Override
    public TransferOptionExtension findTransferOptionExtension(long transferOptionExtensionId) {
        log.info("Finding transfer option extension with id {}", transferOptionExtensionId);
        return transferOptionExtensionRepository.findById(transferOptionExtensionId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "TransferOptionExtension entity with id: " + transferOptionExtensionId + " does not exist"));
    }

    @Override
    public Set<TransferOptionExtension> findTransferOptionExtensionsByExtensionId(long extensionId) {
        log.info("Finding transfer option extensions by extension id");
        return transferOptionExtensionRepository.findByEntryExtensionId(extensionId);
    }

    @Transactional
    @Override
    public List<TransferOptionExtension> saveTransferOptionExtensions(
            @NonNull List<TransferOptionExtensionModel> transferOptionExtensionDto,
            @NonNull EntryExtension entryExtension) {
        log.info("Saving transfer option extensions - {}", transferOptionExtensionDto);
        List<TransferOptionExtension> transferOptionExtensions = transferOptionExtensionDto.stream()
                .map(option -> transferOptionExtensionMapper.mapToTransferOptionExtension(option, entryExtension))
                .collect(Collectors.toList());
        transferOptionExtensionRepository.saveAll(transferOptionExtensions);
        log.info("Transfer option extensions saved");
        return transferOptionExtensions;
    }

    @Transactional
    @Override
    public void saveTransferOptionExtension(TransferOptionExtension transferOptionExtension) {
        log.info("Saving transfer option extension - {}", transferOptionExtension);
        transferOptionExtensionRepository.save(transferOptionExtension);
        log.info("Transfer option extension saved");
    }

    @Transactional
    @Override
    public void removeTransferOptionExtension(long transferOptionExtensionId) {
        log.info("Removing transfer option extension with id {}", transferOptionExtensionId);
        final TransferOptionExtension transferOptionExtension =
                transferOptionExtensionRepository.findById(transferOptionExtensionId).orElseThrow(
                        () -> new ResourceNotFoundException(
                                "TransferOptionExtension entity with id: " + transferOptionExtensionId
                                        + " does not exist"));
        transferOptionExtension.setDeleted(1);
        transferOptionExtensionRepository.save(transferOptionExtension);
        log.info("Removed transfer option extension with id: {}", transferOptionExtension.getId());
    }

    @Transactional
    @Override
    public void deleteTransferOptionExtension(long transferOptionExtensionId) {
        log.info("Deleting transfer option extension with id: {}", transferOptionExtensionId);
        transferOptionExtensionRepository.deleteById(transferOptionExtensionId);
        log.info("Deleted transfer option extension with id: {}", transferOptionExtensionId);
    }

    @Transactional
    @Override
    public void updateTransferOptionExtensionsByExtensionId(long extensionId,
            Set<TransferOptionExtensionModel> transferOptionExtensions) {
        log.info("Updating transfer option extensions by entry extension id: {}", extensionId);

        Set<TransferOptionExtension> currentTransferOptionExtensions =
                transferOptionExtensionRepository.findByEntryExtensionId(extensionId);
        Set<Long> newIds = new HashSet<>();

        transferOptionExtensions.forEach(transferOptionExtensionModel -> {
            if (transferOptionExtensionModel.getId() == null) {
                final TransferOptionExtension transferOptionExtension =
                        transferOptionExtensionRepository.findById(transferOptionExtensionModel.getId()).orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "TransferOptionExtension entity with id: "
                                                + transferOptionExtensionModel.getId()
                                                + " does not exist"));
                transferOptionExtension.setName(transferOptionExtensionModel.getName());
                transferOptionExtension.setDescription(transferOptionExtensionModel.getDescription());
                transferOptionExtension.setDisplayTitle(transferOptionExtensionModel.getDisplayTitle());
                transferOptionExtension.setEndpoint(
                        endpointService.getOrCreateEndpoint(transferOptionExtensionModel.getEndpoint()));
                transferOptionExtension.setProtocol(
                        protocolService.getOrCreateProtocol(transferOptionExtensionModel.getProtocol()));
                saveTransferOptionExtension(transferOptionExtension);
                newIds.add(transferOptionExtension.getId());
            } else {
                TransferOptionExtension option =
                        createTransferOptionExtension(
                                transferOptionExtensionMapper.mapToTransferOptionExtension(transferOptionExtensionModel,
                                        extensionId));
                newIds.add(option.getId());
            }
        });

        List<TransferOptionExtension> optionsToRemove = currentTransferOptionExtensions.stream()
                .filter(option -> !newIds.contains(option.getId()))
                .collect(Collectors.toList());

        optionsToRemove.forEach(transferOption -> {
            deleteTransferOptionExtension(transferOption.getId());
        });
    }

    @Transactional
    @Override
    public TransferOptionExtension createTransferOptionExtension(TransferOptionExtension transferOptionExtension) {
        log.info("Creating transfer option extension - {}", transferOptionExtension);
        return transferOptionExtensionRepository.save(transferOptionExtension);
    }

}
