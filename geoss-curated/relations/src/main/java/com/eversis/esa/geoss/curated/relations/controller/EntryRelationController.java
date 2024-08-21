package com.eversis.esa.geoss.curated.relations.controller;

import com.eversis.esa.geoss.curated.relations.domain.EntryRelation;
import com.eversis.esa.geoss.curated.relations.model.EntryRelationIdModel;
import com.eversis.esa.geoss.curated.relations.model.EntryRelationModel;
import com.eversis.esa.geoss.curated.relations.service.EntryRelationService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.validation.Valid;

/**
 * The type Entry relation controller.
 */
@Log4j2
@BasePathAwareController("/relations")
@ResponseBody
@Tag(name = "relations")
public class EntryRelationController {

    private final EntryRelationService entryRelationService;

    /**
     * Instantiates a new Entry relation controller.
     *
     * @param entryRelationService the entry relation service
     */
    public EntryRelationController(EntryRelationService entryRelationService) {
        this.entryRelationService = entryRelationService;
    }

    /**
     * Find entry relations page.
     *
     * @param page the page
     * @param size the size
     * @return the page
     */
    @PreAuthorize("hasAnyRole('RELATION_READER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Page<EntryRelation> findEntryRelations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Find entry relations page: {}, size: {}", page, size);
        return entryRelationService.findEntryRelations(PageRequest.of(page, size));
    }

    /**
     * Find entry relation entry relation.
     *
     * @param srcId the src id
     * @param srcDataSourceId the src data source id
     * @param destId the dest id
     * @param destDataSourceId the dest data source id
     * @param relationTypeId the relation type id
     * @return the entry relation
     */
    @PreAuthorize("hasAnyRole('RELATION_READER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/findEntryRelation")
    public EntryRelation findEntryRelation(
            @RequestParam String srcId,
            @RequestParam int srcDataSourceId,
            @RequestParam String destId,
            @RequestParam int destDataSourceId,
            @RequestParam int relationTypeId) {
        log.info("Find entry relation");
        return entryRelationService.findEntryRelation(
                new EntryRelationIdModel(srcId, srcDataSourceId, destId, destDataSourceId, relationTypeId));
    }

    /**
     * Create entry relation.
     *
     * @param entryRelationDto the entry relation dto
     */
    @PreAuthorize("hasAnyRole('RELATION_WRITER', 'ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createEntryRelation(@RequestBody @Valid EntryRelationModel entryRelationDto) {
        log.info("Create entry relation");
        entryRelationService.createEntryRelation(entryRelationDto);
    }

    /**
     * Update entry relation.
     *
     * @param srcId the src id
     * @param srcDataSourceId the src data source id
     * @param destId the dest id
     * @param destDataSourceId the dest data source id
     * @param relationTypeId the relation type id
     * @param entryRelationDto the entry relation dto
     */
    @PreAuthorize("hasAnyRole('RELATION_WRITER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    public void updateEntryRelation(
            @RequestParam String srcId,
            @RequestParam int srcDataSourceId,
            @RequestParam String destId,
            @RequestParam int destDataSourceId,
            @RequestParam int relationTypeId,
            @RequestBody @Valid EntryRelationModel entryRelationDto) {
        log.info("Update entry relation");
        entryRelationService.updateEntryRelation(
                new EntryRelationIdModel(srcId, srcDataSourceId, destId, destDataSourceId, relationTypeId),
                entryRelationDto);
    }

    /**
     * Remove entry relation.
     *
     * @param srcId the src id
     * @param srcDataSourceId the src data source id
     * @param destId the dest id
     * @param destDataSourceId the dest data source id
     * @param relationTypeId the relation type id
     */
    @PreAuthorize("hasAnyRole('RELATION_REMOVER', 'ADMIN')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping
    public void removeEntryRelation(
            @RequestParam String srcId,
            @RequestParam int srcDataSourceId,
            @RequestParam String destId,
            @RequestParam int destDataSourceId,
            @RequestParam int relationTypeId) {
        log.info("Remove entry relation");
        entryRelationService.removeEntryRelation(
                new EntryRelationIdModel(srcId, srcDataSourceId, destId, destDataSourceId, relationTypeId));
    }

    /**
     * Delete entry relation.
     *
     * @param srcId the src id
     * @param srcDataSourceId the src data source id
     * @param destId the dest id
     * @param destDataSourceId the dest data source id
     * @param relationTypeId the relation type id
     */
    @PreAuthorize("hasAnyRole('RELATION_DELETER', 'ADMIN')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping("/delete")
    public void deleteEntryRelation(
            @RequestParam String srcId,
            @RequestParam int srcDataSourceId,
            @RequestParam String destId,
            @RequestParam int destDataSourceId,
            @RequestParam int relationTypeId) {
        log.info("Delete entry relation");
        entryRelationService.deleteEntryRelation(
                new EntryRelationIdModel(srcId, srcDataSourceId, destId, destDataSourceId, relationTypeId));
    }

    /**
     * Restore entry relation.
     *
     * @param srcId the src id
     * @param srcDataSourceId the src data source id
     * @param destId the dest id
     * @param destDataSourceId the dest data source id
     * @param relationTypeId the relation type id
     */
    @PreAuthorize("hasAnyRole('RELATION_REMOVER', 'ADMIN')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping("/restore")
    public void restoreEntryRelation(
            @RequestParam String srcId,
            @RequestParam int srcDataSourceId,
            @RequestParam String destId,
            @RequestParam int destDataSourceId,
            @RequestParam int relationTypeId) {
        log.info("Restore entry relation");
        entryRelationService.restoreEntryRelation(
                new EntryRelationIdModel(srcId, srcDataSourceId, destId, destDataSourceId, relationTypeId));
    }

}
