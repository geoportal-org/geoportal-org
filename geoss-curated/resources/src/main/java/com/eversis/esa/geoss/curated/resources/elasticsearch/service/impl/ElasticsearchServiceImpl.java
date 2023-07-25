package com.eversis.esa.geoss.curated.resources.elasticsearch.service.impl;

import com.eversis.esa.geoss.curated.resources.domain.Entry;
import com.eversis.esa.geoss.curated.resources.elasticsearch.mapper.ResourceEntryMapper;
import com.eversis.esa.geoss.curated.resources.elasticsearch.repository.ResourceRepository;
import com.eversis.esa.geoss.curated.resources.elasticsearch.service.ElasticsearchService;
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

    public ElasticsearchServiceImpl(ResourceRepository resourceRepository, ResourceEntryMapper resourceEntryMapper) {
        this.resourceRepository = resourceRepository;
        this.resourceEntryMapper = resourceEntryMapper;
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

}
