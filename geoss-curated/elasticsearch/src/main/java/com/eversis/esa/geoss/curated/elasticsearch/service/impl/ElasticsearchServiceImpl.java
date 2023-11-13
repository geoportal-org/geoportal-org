package com.eversis.esa.geoss.curated.elasticsearch.service.impl;

import java.util.Set;

import com.eversis.esa.geoss.curated.common.repository.DataSourceRepository;
import com.eversis.esa.geoss.curated.common.repository.TypeRepository;
import com.eversis.esa.geoss.curated.elasticsearch.mapper.ExtensionMapper;
import com.eversis.esa.geoss.curated.elasticsearch.mapper.ResourceEntryMapper;
import com.eversis.esa.geoss.curated.elasticsearch.repository.ExtensionRepository;
import com.eversis.esa.geoss.curated.elasticsearch.repository.ResourceRepository;
import com.eversis.esa.geoss.curated.elasticsearch.service.ElasticsearchService;
import com.eversis.esa.geoss.curated.extensions.domain.EntryExtension;
import com.eversis.esa.geoss.curated.relations.domain.EntryRelation;
import com.eversis.esa.geoss.curated.resources.domain.Entry;

import com.eversis.esa.geoss.curated.resources.repository.EntryRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 * The type Elasticsearch service.
 */
@Log4j2
@Service
@Transactional(readOnly = true)
@Validated
public class ElasticsearchServiceImpl implements ElasticsearchService {

    private final ResourceRepository resourceRepository;

    private final ResourceEntryMapper resourceEntryMapper;

    private final EntryRepository entryRepository;

    private final ExtensionRepository extensionRepository;

    private final TypeRepository typeRepository;

    private final DataSourceRepository dataSourceRepository;

    private final ExtensionMapper extensionMapper;

    public ElasticsearchServiceImpl(ResourceRepository resourceRepository, ResourceEntryMapper resourceEntryMapper,
            EntryRepository entryRepository, ExtensionRepository extensionRepository, TypeRepository typeRepository,
            DataSourceRepository dataSourceRepository, ExtensionMapper extensionMapper) {
        this.resourceRepository = resourceRepository;
        this.resourceEntryMapper = resourceEntryMapper;
        this.entryRepository = entryRepository;
        this.extensionRepository = extensionRepository;
        this.typeRepository = typeRepository;
        this.dataSourceRepository = dataSourceRepository;
        this.extensionMapper = extensionMapper;
    }

    @Override
    public void indexEntry(Entry entry) {
        log.info("Indexing entry {}", entry);
        resourceRepository.save(resourceEntryMapper.mapToDocument(entry));
        log.info("Indexing entry finished.");
    }

    @Override
    public void removeEntryFromIndex(Entry entry) {
        log.info("Removing entry {} from index", entry);
        resourceRepository.deleteById(entry.getCode());
        log.info("Entry removed from index.");
    }

    @Override
    public void indexEntryRelation(EntryRelation entryRelation) {
        log.info("Indexing entry relation {}", entryRelation);

        resourceRepository.findByCode(entryRelation.getId().getDestId()).ifPresent(resourceEntryELK -> {
            log.info("Child entry: {}", resourceEntryELK);
            Set<String> parentId = resourceEntryELK.getParentId();
            parentId.add(entryRelation.getId().getSrcId());
            resourceEntryELK.setParentId(parentId);
            resourceRepository.save(resourceEntryELK);
        });

        resourceRepository.findByCode(entryRelation.getId().getSrcId()).ifPresent(resourceEntryELK -> {
            log.info("Parent entry: {}", resourceEntryELK);
            resourceEntryELK.setHasChildren(true);
            Set<String> childrenTypes = resourceEntryELK.getChildrenTypes();
            childrenTypes.add(entryRelation.getDestType().getCode());
            resourceEntryELK.setChildrenTypes(childrenTypes);
            resourceRepository.save(resourceEntryELK);
        });

        log.info("Indexing entry relation finished.");
    }

    @Override
    public void removeEntryRelationFromIndex(EntryRelation entryRelation) {
        log.info("Removing entry relation {} from index", entryRelation);

        resourceRepository.findByCode(entryRelation.getId().getDestId()).ifPresent(resourceEntryELK -> {
            Set<String> parentId = resourceEntryELK.getParentId();
            parentId.remove(entryRelation.getSrcDataSource().getCode());
            resourceEntryELK.setParentId(parentId);
            resourceRepository.save(resourceEntryELK);
        });

        resourceRepository.findByCode(entryRelation.getId().getSrcId()).ifPresent(resourceEntryELK -> {
            resourceEntryELK.setHasChildren(false);
            Set<String> childrenTypes = resourceEntryELK.getChildrenTypes();
            childrenTypes.remove(entryRelation.getDestType().getCode());
            resourceEntryELK.setChildrenTypes(childrenTypes);
            resourceRepository.save(resourceEntryELK);
        });

        log.info("Entry relation removed from index.");
    }

    @Override
    public void indexEntryExtension(EntryExtension entryExtension) {
        log.info("Indexing entry extension {}", entryExtension);
        extensionRepository.save(extensionMapper.mapToDocument(entryExtension));
        log.info("Indexing entry extension finished.");
    }

    @Override
    public void removeEntryExtensionFromIndex(EntryExtension entryExtension) {
        log.info("Removing entry extension {} from index", entryExtension);
        resourceRepository.deleteById(String.valueOf(entryExtension.getId()));
        log.info("Entry extension removed from index.");
    }

    @Override
    public void indexDashboard(Entry entry) {
        log.info("Indexing dashboard {}", entry);
        resourceRepository.save(resourceEntryMapper.mapToDocument(entry));
        log.info("Indexing dashboard finished.");
    }

    @Override
    public void removeDashboardFromIndex(Entry entry) {
        log.info("Removing dashboard {} from index", entry);
        resourceRepository.deleteById(entry.getCode());
        log.info("Dashboard removed from index.");
    }

    @Override
    public void reindexEntries() {
        log.info("Reindexing all entries.");
        resourceRepository.deleteAll();
        entryRepository.findAll().forEach(this::indexEntry);
        log.info("Reindex finished.");
    }

    @Override
    public void reindexEntriesByType(String type) {
        log.info("Reindexing entries by type: {}.", type);
        typeRepository.findByCode(type).ifPresent(type1 -> {
            entryRepository.findAllByType(type1).forEach(this::indexEntry);
        });
        log.info("Reindex finished.");
    }

    @Override
    public void reindexEntriesByDataSource(String dataSource) {
        log.info("Reindexing entries by dataSource: {}.", dataSource);
        dataSourceRepository.findByCode(dataSource).ifPresent(dataSource1 -> {
            entryRepository.findAllByDataSource(dataSource1).forEach(this::indexEntry);
        });
        log.info("Reindex finished.");
    }

    @Override
    public void clearEntries() {
        log.info("Clearing all entries.");
        resourceRepository.deleteAll();
        log.info("Clear finished.");
    }

}
