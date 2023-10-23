package com.eversis.esa.geoss.curated.resources.service.impl;

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

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    /**
     * Instantiates a new Transfer option service.
     *
     * @param transferOptionRepository the transfer option repository
     * @param transferOptionMapper the transfer option mapper
     */
    public TransferOptionServiceImpl(TransferOptionRepository transferOptionRepository,
            TransferOptionMapper transferOptionMapper) {
        this.transferOptionRepository = transferOptionRepository;
        this.transferOptionMapper = transferOptionMapper;
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
        Set<TransferOption> existingTransferOptions = transferOptionRepository.findByEntryId(relatedEntry.getId());
        Set<TransferOption> newTransferOptions = transferOptionDto.stream()
                .map(transferOption -> transferOptionMapper.mapToTransferOption(transferOption, relatedEntry))
                .collect(Collectors.toSet());
        existingTransferOptions.addAll(newTransferOptions);
        existingTransferOptions.forEach(transferOption -> transferOption.setModifiedDate(LocalDateTime.now()));
        log.info("Transfer options Saved");
        return transferOptionRepository.saveAll(existingTransferOptions);
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

}
