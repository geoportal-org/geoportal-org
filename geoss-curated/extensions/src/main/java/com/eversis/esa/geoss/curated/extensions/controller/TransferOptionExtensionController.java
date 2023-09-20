package com.eversis.esa.geoss.curated.extensions.controller;

import java.util.Set;

import com.eversis.esa.geoss.curated.extensions.domain.TransferOptionExtension;
import com.eversis.esa.geoss.curated.extensions.service.TransferOptionExtensionService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The type Transfer option extension controller.
 */
@Log4j2
@BasePathAwareController("/transferOptionExtension")
@ResponseBody
@Tag(name = "transferOptionExtension")
public class TransferOptionExtensionController {

    private final TransferOptionExtensionService transferOptionExtensionService;

    /**
     * Instantiates a new Transfer option extension controller.
     *
     * @param transferOptionExtensionService the transfer option extension service
     */
    public TransferOptionExtensionController(TransferOptionExtensionService transferOptionExtensionService) {
        this.transferOptionExtensionService = transferOptionExtensionService;
    }

    /**
     * Find transfer option extensions page.
     *
     * @param page the page
     * @param size the size
     * @return the page
     */
    @PreAuthorize("hasAnyRole('EXTENSION_READER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Page<TransferOptionExtension> findTransferOptionExtensions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Find transfer option extensions page: {}, size: {}", page, size);
        return transferOptionExtensionService.findTransferOptionExtensions(PageRequest.of(page, size));
    }

    /**
     * Find transfer option extension transfer option extension.
     *
     * @param transferOptionExtensionId the transfer option extension id
     * @return the transfer option extension
     */
    @PreAuthorize("hasAnyRole('EXTENSION_READER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{transferOptionExtensionId}")
    public TransferOptionExtension findTransferOptionExtension(@PathVariable long transferOptionExtensionId) {
        log.info("Find transfer option extension");
        return transferOptionExtensionService.findTransferOptionExtension(transferOptionExtensionId);
    }

    /**
     * Find transfer option extensions by extension id set.
     *
     * @param extensionId the extension id
     * @return the set
     */
    @PreAuthorize("hasAnyRole('EXTENSION_READER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/extension/{extensionId}")
    public Set<TransferOptionExtension> findTransferOptionExtensionsByExtensionId(
            @PathVariable long extensionId) {
        log.info("Find transfer option extensions by extension id");
        return transferOptionExtensionService.findTransferOptionExtensionsByExtensionId(extensionId);
    }

    /**
     * Remove transfer option extension.
     *
     * @param transferOptionExtensionId the transfer option extension id
     */
    @PreAuthorize("hasAnyRole('EXTENSION_REMOVER', 'ADMIN')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping("/{transferOptionExtensionId}")
    public void removeTransferOptionExtension(@PathVariable long transferOptionExtensionId) {
        log.info("remove transfer option extension");
        transferOptionExtensionService.removeTransferOptionExtension(transferOptionExtensionId);
    }

    /**
     * Delete transfer option extension.
     *
     * @param transferOptionExtensionId the transfer option extension id
     */
    @PreAuthorize("hasAnyRole('EXTENSION_DELETER', 'ADMIN')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping("/delete/{transferOptionExtensionId}")
    public void deleteTransferOptionExtension(@PathVariable long transferOptionExtensionId) {
        log.info("Delete transfer option extension");
        transferOptionExtensionService.deleteTransferOptionExtension(transferOptionExtensionId);
    }

}
