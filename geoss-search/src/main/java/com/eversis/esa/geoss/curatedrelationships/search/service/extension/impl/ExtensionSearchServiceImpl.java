package com.eversis.esa.geoss.curatedrelationships.search.service.extension.impl;

import com.eversis.esa.geoss.curatedrelationships.search.model.DataSource;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.impl.PageRequest;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.Extension;
import com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.ExtensionElasticsearchRepository;
import com.eversis.esa.geoss.curatedrelationships.search.service.extension.ExtensionSearchService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Set;
import javax.validation.constraints.NotNull;

/**
 * The type Extension search service.
 */
@Slf4j
@Service
@Validated
class ExtensionSearchServiceImpl implements ExtensionSearchService {

    private final ExtensionElasticsearchRepository extensionElasticsearchRepository;

    /**
     * Instantiates a new Extension search service.
     *
     * @param extensionElasticsearchRepository the extension elasticsearch repository
     */
    public ExtensionSearchServiceImpl(ExtensionElasticsearchRepository extensionElasticsearchRepository) {
        this.extensionElasticsearchRepository = extensionElasticsearchRepository;
    }

    @Override
    public List<Extension> findExtensions(
            @NotNull Set<String> ids,
            @NotNull DataSource dataSource) {
        if (log.isDebugEnabled()) {
            log.debug("Searching for CR resources entries, using ids: {}, dataSourceType: {}", ids, dataSource);
        }
        return extensionElasticsearchRepository
                .findExtensions(
                        ids,
                        dataSource,
                        new PageRequest(0, ids.size()))
                .getContent();
    }
}
