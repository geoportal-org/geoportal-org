package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.service;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.model.DataSource;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.model.EntryRelation;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.model.EntryRelationId;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.model.RelationType;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.repository.EntryRelationRepository;

import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Entry relation service.
 */
@Service
public class EntryRelationService {

    private final EntryRelationRepository entryRelationRepository;
    private final DataSourceService dataSourceService;
    private final RelationTypeService relationTypeService;
    private final TypeService typeService;

    /**
     * Instantiates a new Entry relation service.
     *
     * @param entryRelationRepository the entry relation repository
     * @param dataSourceService the data source service
     * @param relationTypeService the relation type service
     * @param typeService the type service
     */
    public EntryRelationService(
            EntryRelationRepository entryRelationRepository,
            DataSourceService dataSourceService,
            RelationTypeService relationTypeService, TypeService typeService) {
        this.entryRelationRepository = entryRelationRepository;
        this.dataSourceService = dataSourceService;
        this.relationTypeService = relationTypeService;
        this.typeService = typeService;
    }

    /**
     * Save entry relations list.
     *
     * @param domainEntryRelations the domain entry relations
     * @return the list
     */
    public List<EntryRelation> saveEntryRelations(
            @NonNull List<com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.EntryRelation>
                    domainEntryRelations) {
        List<EntryRelation> dbEntryRelations = domainEntryRelations.stream()
                .map(this::createEntryRelation)
                .collect(Collectors.toList());
        return entryRelationRepository.saveAll(dbEntryRelations);
    }

    /**
     * Save entry relation entry relation.
     *
     * @param domainEntryRelation the domain entry relation
     * @return the entry relation
     */
    public EntryRelation saveEntryRelation(
            @NonNull com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.EntryRelation
                    domainEntryRelation) {
        EntryRelation dbEntryRelation = createEntryRelation(domainEntryRelation);
        return entryRelationRepository.save(dbEntryRelation);
    }

    private EntryRelation createEntryRelation(
            @NonNull com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.EntryRelation
                    domainEntryRelation) {
        DataSource soruceDataSource = dataSourceService.getOrCreateDataSource(
                domainEntryRelation.getSourceDataSource());
        DataSource destinationDataSource = dataSourceService.getOrCreateDataSource(
                domainEntryRelation.getDestinationDataSource());
        RelationType relationType = relationTypeService.getOrCreateRelationType(
                domainEntryRelation.getRelationType());

        EntryRelation dbEntryRelation = new EntryRelation();
        dbEntryRelation.setId(new EntryRelationId(domainEntryRelation.getSourceEntryCode(),
                soruceDataSource.getId(), domainEntryRelation.getDestinationEntryCode(), destinationDataSource.getId(),
                relationType.getId()));
        dbEntryRelation.setSrcType(typeService.getOrCreateType(domainEntryRelation.getSourceType()));
        dbEntryRelation.setDestType(typeService.getOrCreateType(domainEntryRelation.getDestinationType()));
        dbEntryRelation.setModifiedDate(LocalDateTime.now());
        dbEntryRelation.setSrcDataSource(soruceDataSource);
        dbEntryRelation.setDestDataSource(destinationDataSource);
        dbEntryRelation.setRelationType(relationType);
        return dbEntryRelation;
    }

}
