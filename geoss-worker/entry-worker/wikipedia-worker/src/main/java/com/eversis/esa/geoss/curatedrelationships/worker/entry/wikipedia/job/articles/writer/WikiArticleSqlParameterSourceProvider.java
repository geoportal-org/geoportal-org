package com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.articles.writer;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.articles.model.WikiArticleEntry;

import org.springframework.batch.item.database.ItemSqlParameterSourceProvider;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

/**
 * The type Wiki article sql parameter source provider.
 */
class WikiArticleSqlParameterSourceProvider implements ItemSqlParameterSourceProvider<WikiArticleEntry> {

    /**
     * Create sql parameter source sql parameter source.
     *
     * @param articleEntry the article entry
     * @return the sql parameter source
     */
    @Override
    public SqlParameterSource createSqlParameterSource(WikiArticleEntry articleEntry) {
        return new MapSqlParameterSource()
                .addValue("_title", articleEntry.getTitle())
                .addValue("_summary", articleEntry.getSummary())
                .addValue("_logo", articleEntry.getLogo())
                .addValue("_keywords", articleEntry.getKeywords())
                .addValue("_tags", articleEntry.getTags())
                .addValue("_code", articleEntry.getCode())
                .addValue("_typeId", articleEntry.getTypeId())
                .addValue("_definitionTypeId", articleEntry.getDefinitionTypeId())
                .addValue("_accessPolicyId", articleEntry.getAccessPolicyId())
                .addValue("_sourceId", articleEntry.getSourceId())
                .addValue("_dataSourceId", articleEntry.getDataSourceId())
                .addValue("_protocolId", articleEntry.getTransferOption().getProtocolId())
                .addValue("_endpointUrl", articleEntry.getTransferOption().getUrl());
    }
}
