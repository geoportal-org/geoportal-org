package com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.articles.service.database;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

/**
 * The type Wikipedia database service.
 */
@Log4j2
@Service
public class WikipediaDatabaseService {

    private final DataSourceService dataSourceService;
    private final SourceService sourceService;
    private final TypeService typeService;
    private final DefinitionTypeService definitionTypeService;
    private final AccessPolicyService accessPolicyService;
    private final ProtocolService protocolService;

    /**
     * Instantiates a new Wikipedia database service.
     *
     * @param dataSourceService the data source service
     * @param sourceService the source service
     * @param typeService the type service
     * @param definitionTypeService the definition type service
     * @param accessPolicyService the access policy service
     * @param protocolService the protocol service
     */
    public WikipediaDatabaseService(
            DataSourceService dataSourceService,
            SourceService sourceService,
            TypeService typeService,
            DefinitionTypeService definitionTypeService,
            AccessPolicyService accessPolicyService,
            ProtocolService protocolService) {
        this.dataSourceService = dataSourceService;
        this.sourceService = sourceService;
        this.typeService = typeService;
        this.definitionTypeService = definitionTypeService;
        this.accessPolicyService = accessPolicyService;
        this.protocolService = protocolService;
    }

    /**
     * Returns value of typeId column, which should be used as foreign key in main entry table for Wikipedia article's
     * data.
     *
     * @return the article type id
     */
    public Integer getArticleTypeId() {
        return typeService.getArticeTypeId();
    }

    /**
     * Returns value of typeId column, which should be used as foreign key in main entry table for Wikipedia article's
     * data.
     *
     * @return the article definition type id
     */
    public Integer getArticleDefinitionTypeId() {
        return definitionTypeService.getArticleDefinitionTypeId();
    }

    /**
     * Returns value of accessPolicyId column, which should be used as foreign key in main entry table for Wikipedia
     * article's data.
     *
     * @return the article access policy id
     */
    public Integer getArticleAccessPolicyId() {
        return accessPolicyService.getArticleAccessPolicyId();
    }

    /**
     * Returns value of sourceId column, which should be used as foreign key in main entry table for Wikipedia article's
     * data.
     *
     * @return the wikipedia source id
     */
    public Integer getWikipediaSourceId() {
        return sourceService.getWikipediaSourceId();
    }

    /**
     * Returns value of dataSourceId column, which should be used as foreign key in main entry table for Wikipedia
     * article's data.
     *
     * @return the wikipedia data source id
     */
    public Integer getWikipediaDataSourceId() {
        return dataSourceService.getWikipediaDataSourceId();
    }

    /**
     * Returns value of protocolId column, which should be used as foreign key in transfer options table for Wikipedia
     * article's data.
     *
     * @return the wikipedia protocol id
     */
    public Integer getWikipediaProtocolId() {
        return protocolService.getWikipediaProtocolId();
    }

}
