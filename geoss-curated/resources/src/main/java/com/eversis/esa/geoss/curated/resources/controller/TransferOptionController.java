package com.eversis.esa.geoss.curated.resources.controller;

import com.eversis.esa.geoss.curated.resources.domain.TransferOption;
import com.eversis.esa.geoss.curated.resources.model.TransferOptionModel;
import com.eversis.esa.geoss.curated.resources.service.TransferOptionService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Set;
import jakarta.validation.Valid;

/**
 * The type Transfer option controller.
 */
@Log4j2
@BasePathAwareController("/transferOption")
@ResponseBody
@Tag(name = "transferOption")
public class TransferOptionController {

    private final TransferOptionService transferOptionService;

    /**
     * Instantiates a new Transfer option controller.
     *
     * @param transferOptionService the transfer option service
     */
    public TransferOptionController(TransferOptionService transferOptionService) {
        this.transferOptionService = transferOptionService;
    }

    /**
     * Find transfer options page.
     *
     * @param page the page
     * @param size the size
     * @return the page
     */
    @PreAuthorize("hasAnyRole('RESOURCE_READER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Page<TransferOption> findTransferOptions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Find transfer options page: {}, size: {}", page, size);
        return transferOptionService.findTransferOptions(PageRequest.of(page, size));
    }

    /**
     * Find transfer option transfer option.
     *
     * @param transferOptionId the transfer option id
     * @return the transfer option
     */
    @PreAuthorize("hasAnyRole('RESOURCE_READER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{transferOptionId}")
    public TransferOption findTransferOption(@PathVariable long transferOptionId) {
        log.info("Find transfer option");
        return transferOptionService.findTransferOption(transferOptionId);
    }

    /**
     * Find transfer options by entry id set.
     *
     * @param entryId the entry id
     * @return the set
     */
    @PreAuthorize("hasAnyRole('RESOURCE_READER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/entry/{entryId}")
    public Set<TransferOption> findTransferOptionsByEntryId(@PathVariable long entryId) {
        log.info("Find transfer options by entry id");
        return transferOptionService.findTransferOptionsByEntryId(entryId);
    }

    /**
     * Update transfer options by entry id set.
     *
     * @param entryId the entry id
     * @return the set
     */
    @PreAuthorize("hasAnyRole('RESOURCE_WRITER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/entry/{entryId}")
    public void updateTransferOptionsByEntryId(@PathVariable long entryId,
            @RequestBody @Valid Set<TransferOptionModel> transferOptions) {
        log.info("Update transfer options by entry id");
        transferOptionService.updateTransferOptionsByEntryId(entryId, transferOptions);
    }

    /**
     * Remove transfer option.
     *
     * @param transferOptionId the transfer option id
     */
    @PreAuthorize("hasAnyRole('RESOURCE_REMOVER', 'ADMIN')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping("/{transferOptionId}")
    public void removeTransferOption(@PathVariable long transferOptionId) {
        log.info("remove transfer option");
        transferOptionService.removeTransferOption(transferOptionId);
    }

    /**
     * Delete transfer option.
     *
     * @param transferOptionId the transfer option id
     */
    @PreAuthorize("hasAnyRole('RESOURCE_DELETER', 'ADMIN')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping("/delete/{transferOptionId}")
    public void deleteTransferOption(@PathVariable long transferOptionId) {
        log.info("Delete transfer option");
        transferOptionService.deleteTransferOption(transferOptionId);
    }

}
