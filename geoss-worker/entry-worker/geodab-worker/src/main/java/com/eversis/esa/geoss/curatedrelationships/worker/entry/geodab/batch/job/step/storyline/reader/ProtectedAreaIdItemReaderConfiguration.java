package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.storyline.reader;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.config.geodab.DabProperties;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.base.constants.EntryIdentifiers;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;

/**
 * The type Protected area id item reader configuration.
 */
@Configuration
class ProtectedAreaIdItemReaderConfiguration {

    private static final String RESULT_COLUMN_NAME = "ecoid";
    private static final String SELECT_CLAUSE = "select replace(code, :entryCodePrefix, '') as " + RESULT_COLUMN_NAME;
    private static final String FROM_CLAUSE = "from entry";
    private static final String WHERE_CLAUSE = "where entry.code like :entryCodePrefixLike and entry.tags like"
                                               + " :protectedAreaIdentifier";

    /**
     * Protected area id item reader jdbc paging item reader.
     *
     * @param dataSource the data source
     * @param dabProperties the dab configuration
     * @return the jdbc paging item reader
     * @throws Exception the exception
     */
    @StepScope
    @Bean(value = "protectedAreaIdItemReader", destroyMethod = "")
    JdbcPagingItemReader<String> protectedAreaIdItemReader(
            @Qualifier("geossCuratedDataSource") DataSource dataSource,
            DabProperties dabProperties) throws Exception {
        final SqlPagingQueryProviderFactoryBean sqlPagingQueryProviderFactoryBean
                = new SqlPagingQueryProviderFactoryBean();
        sqlPagingQueryProviderFactoryBean.setDataSource(dataSource);
        sqlPagingQueryProviderFactoryBean.setSelectClause(SELECT_CLAUSE);
        sqlPagingQueryProviderFactoryBean.setFromClause(FROM_CLAUSE);
        sqlPagingQueryProviderFactoryBean.setWhereClause(WHERE_CLAUSE);
        sqlPagingQueryProviderFactoryBean.setSortKey(RESULT_COLUMN_NAME);

        Map<String, Object> parameterValues = new HashMap<>();
        parameterValues.put("entryCodePrefix", dabProperties.getEntryCodePrefix());
        parameterValues.put("entryCodePrefixLike", dabProperties.getEntryCodePrefix() + "%");
        parameterValues.put("protectedAreaIdentifier", EntryIdentifiers.PROTECTED_AREA);

        return new JdbcPagingItemReaderBuilder<String>()
                .queryProvider(sqlPagingQueryProviderFactoryBean.getObject())
                .parameterValues(parameterValues)
                .dataSource(dataSource)
                .pageSize(100)
                .rowMapper((resultSet, rowNum) -> resultSet.getString(1))
                .saveState(false)
                .build();
    }

}
