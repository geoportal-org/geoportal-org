package com.eversis.esa.geoss.curated.extensions.controller;

import jakarta.validation.Valid;

import com.eversis.esa.geoss.curated.extensions.domain.EntryExtension;
import com.eversis.esa.geoss.curated.extensions.model.EntryExtensionModel;
import com.eversis.esa.geoss.curated.extensions.service.EntryExtensionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The type Entry extension controller.
 */
@Log4j2
@RequiredArgsConstructor
@BasePathAwareController("/extensions")
@ResponseBody
@Tag(name = "extensions")
public class EntryExtensionController {

    private final EntryExtensionService entryExtensionService;

    /**
     * Find entry extensions page.
     *
     * @param page the page
     * @param size the size
     * @return the page
     */
    @PreAuthorize("hasAnyRole('EXTENSION_READER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Page<EntryExtension> findEntryExtensions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Find entry extensions page: {}, size: {}", page, size);
        return entryExtensionService.findEntryExtensions(PageRequest.of(page, size));
    }

    /**
     * Find entry extension entry extension.
     *
     * @param entryExtensionId the entry extension id
     * @return the entry extension
     */
    @PreAuthorize("hasAnyRole('EXTENSION_READER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{entryExtensionId}")
    public EntryExtension findEntryExtension(@PathVariable long entryExtensionId) {
        log.info("Find entry extension");
        return entryExtensionService.findEntryExtension(entryExtensionId);
    }

    /**
     * Create entry extension.
     *
     * @param entryExtensionDto the entry extension dto
     */
    @PreAuthorize("hasAnyRole('EXTENSION_WRITER', 'ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createEntryExtension(@RequestBody @Valid EntryExtensionModel entryExtensionDto) {
        log.info("Create entry extension");
        entryExtensionService.createEntryExtension(entryExtensionDto);
    }

    /**
     * Update entry extension.
     *
     * @param entryExtensionId the entry extension id
     * @param entryExtensionDto the entry extension dto
     */
    @PreAuthorize("hasAnyRole('EXTENSION_WRITER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{entryExtensionId}")
    public void updateEntryExtension(
            @PathVariable long entryExtensionId,
            @RequestBody @Valid EntryExtensionModel entryExtensionDto) {
        log.info("Update entry extension");
        entryExtensionService.updateEntryExtension(entryExtensionId, entryExtensionDto);
    }

    /**
     * Remove entry extension.
     *
     * @param entryExtensionId the entry extension id
     */
    @PreAuthorize("hasAnyRole('EXTENSION_REMOVER', 'ADMIN')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping("/{entryExtensionId}")
    public void removeEntryExtension(@PathVariable long entryExtensionId) {
        log.info("Remove entry extension");
        entryExtensionService.removeEntryExtension(entryExtensionId);
    }

    /**
     * Delete entry extension.
     *
     * @param entryExtensionId the entry extension id
     */
    @PreAuthorize("hasAnyRole('EXTENSION_DELETER', 'ADMIN')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping("/delete/{entryExtensionId}")
    public void deleteEntryExtension(@PathVariable long entryExtensionId) {
        log.info("Delete entry extension");
        entryExtensionService.deleteEntryExtension(entryExtensionId);
    }

    /**
     * Restore entry extension.
     *
     * @param entryExtensionId the entry extension id
     */
    @PreAuthorize("hasAnyRole('EXTENSION_REMOVER', 'ADMIN')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping("/restore/{entryExtensionId}")
    public void restoreEntryExtension(@PathVariable long entryExtensionId) {
        log.info("Restore entry extension");
        entryExtensionService.restoreEntryExtension(entryExtensionId);
    }

}
