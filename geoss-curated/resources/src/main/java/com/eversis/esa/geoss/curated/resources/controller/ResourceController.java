package com.eversis.esa.geoss.curated.resources.controller;

import jakarta.validation.Valid;

import com.eversis.esa.geoss.curated.resources.domain.Entry;
import com.eversis.esa.geoss.curated.resources.model.EntryModel;
import com.eversis.esa.geoss.curated.resources.service.ResourceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The type Resource controller.
 */
@Log4j2
@RequiredArgsConstructor
@BasePathAwareController("/resources")
@ResponseBody
@Tag(name = "resources")
public class ResourceController {

    private final ResourceService resourceService;

    /**
     * Find entries page.
     *
     * @param page the page
     * @param size the size
     * @return the page
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Page<Entry> findEntries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Find entries page: {}, size: {}", page, size);
        return resourceService.findEntries(PageRequest.of(page, size));
    }

    /**
     * Find entry entry.
     *
     * @param entryId the entry id
     * @return the entry
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{entryId}")
    public Entry findEntry(@PathVariable long entryId) {
        log.info("Find entry");
        return resourceService.findEntry(entryId);
    }

    /**
     * Create entry.
     *
     * @param entryDto the entry dto
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createEntry(@RequestBody @Valid EntryModel entryDto) {
        log.info("Create entry");
        resourceService.createEntry(entryDto);
    }

    /**
     * Update entry.
     *
     * @param entryId the entry id
     * @param entryDto the entry dto
     */
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{entryId}")
    public void updateEntry(
            @PathVariable long entryId,
            @RequestBody @Valid EntryModel entryDto) {
        log.info("Update entry");
        resourceService.updateEntry(entryId, entryDto);
    }

    /**
     * Remove entry.
     *
     * @param entryId the entry id
     */
    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping("/{entryId}")
    public void removeEntry(@PathVariable long entryId) {
        log.info("Remove entry");
        resourceService.removeEntry(entryId);
    }

    /**
     * Delete entry.
     *
     * @param entryId the entry id
     */
    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping("/delete/{entryId}")
    public void deleteEntry(@PathVariable long entryId) {
        log.info("Delete entry");
        resourceService.deleteEntry(entryId);
    }

    /**
     * Restore entry.
     *
     * @param entryId the entry id
     */
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping("/restore/{entryId}")
    public void restoreEntry(@PathVariable long entryId) {
        log.info("Restore entry");
        resourceService.restoreEntry(entryId);
    }

}
