package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.service;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.model.Entry;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.repository.EntryRepository;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * The type Entry service.
 */
@Log4j2
@Service
public class EntryService {

    private EntryRepository entryRepository;
    private AccessPolicyService accessPolicyService;
    private DataSourceService dataSourceService;
    private OrganisationService organisationService;
    private TypeService typeService;
    private DefinitionTypeService definitionTypeService;
    private SourceService sourceService;
    private TransferOptionService transferOptionService;
    private EntryRelationService entryRelationService;

    /**
     * Instantiates a new Entry service.
     *
     * @param entryRepository the entry repository
     * @param accessPolicyService the access policy service
     * @param dataSourceService the data source service
     * @param organisationService the organisation service
     * @param typeService the type service
     * @param definitionTypeService the definition type service
     * @param sourceService the source service
     * @param transferOptionService the transfer option service
     * @param entryRelationService the entry relation service
     */
    public EntryService(EntryRepository entryRepository,
            AccessPolicyService accessPolicyService,
            DataSourceService dataSourceService,
            OrganisationService organisationService,
            TypeService typeService,
            DefinitionTypeService definitionTypeService,
            SourceService sourceService,
            TransferOptionService transferOptionService,
            EntryRelationService entryRelationService) {
        this.entryRepository = entryRepository;
        this.accessPolicyService = accessPolicyService;
        this.dataSourceService = dataSourceService;
        this.organisationService = organisationService;
        this.typeService = typeService;
        this.definitionTypeService = definitionTypeService;
        this.sourceService = sourceService;
        this.transferOptionService = transferOptionService;
        this.entryRelationService = entryRelationService;
    }

    /**
     * Saves all provided entries and their related data like entry-relations and transfer-options in database.
     *
     * @param domainEntries list of domain entries
     */
    public void saveEntries(
            @NonNull List<? extends com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Entry>
                    domainEntries) {
        if (log.isDebugEnabled()) {
            log.debug("Saving {} entries", domainEntries.size());
        }

        domainEntries.forEach(this::saveEntryData);
    }

    /**
     * Saves entry and its related data like entry-relations and transfer-options in database.
     *
     * @param domainEntry domain entry, which should contain all data, which should be saved
     * @return database entry
     */
    public Entry saveEntryData(
            @NonNull com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Entry domainEntry) {
        if (log.isTraceEnabled()) {
            log.trace("Saving entry: {}", domainEntry);
        }

        Entry entry = saveEntry(domainEntry);
        entryRelationService.saveEntryRelations(domainEntry.getRelations());
        transferOptionService.saveTransferOptions(entry, domainEntry.getTransferOptions());
        return entry;
    }

    private Entry saveEntry(
            @NonNull com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Entry domainEntry) {
        Entry databaseEntry = getOrCreateEntry(domainEntry.getCode());
        databaseEntry.setTitle(domainEntry.getTitle());
        databaseEntry.setSummary(domainEntry.getSummary());
        databaseEntry.setLogo(domainEntry.getLogo());
        databaseEntry.setCoverage(domainEntry.getCoverage());
        databaseEntry.setScoreWeight(
                BigDecimal.valueOf(domainEntry.getScoreWeight() != null ? domainEntry.getScoreWeight() : 1.0));
        databaseEntry.setKeywords(String.join(",", domainEntry.getKeywords()));
        databaseEntry.setTags(String.join(",", domainEntry.getTags()));

        databaseEntry.setAccessPolicy(accessPolicyService.getOrCreateAccessPolicy(domainEntry.getAccessPolicy()));
        databaseEntry.setDataSource(dataSourceService.getOrCreateDataSource(domainEntry.getDataSource()));
        databaseEntry.setDisplayDataSource(dataSourceService.getOrCreateDataSource(domainEntry.getDisplayDataSource()));
        databaseEntry.setSource(sourceService.getOrCreateSource(domainEntry.getSource()));
        databaseEntry.setType(typeService.getOrCreateType(domainEntry.getType()));
        databaseEntry.setOrganisation(organisationService.getOrCreateOrganisation(domainEntry.getOrganisation()));
        databaseEntry.setDefinitionType(
                definitionTypeService.getOrCreateDefinitionType(domainEntry.getDefinitionType()));
        databaseEntry.setModifiedDate(LocalDateTime.now());
        return databaseEntry.getId() != null ? databaseEntry : entryRepository.save(databaseEntry);
    }

    private Entry getOrCreateEntry(@NonNull String code) {
        return entryRepository.findByCode(code).orElseGet(() -> new Entry(code));
    }

}
