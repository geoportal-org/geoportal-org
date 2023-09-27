package com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.articles.reader;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * The type Wiki category item reader configuration.
 */
@Configuration
class WikiCategoryItemReaderConfiguration {

    /**
     * Categories item reader jdbc paging item reader.
     *
     * @param dataSource the data source
     * @return the jdbc paging item reader
     * @throws Exception the exception
     */
    @StepScope
    @Bean(value = "wikiCategoriesItemReader", destroyMethod = "")
    JdbcPagingItemReader<String> categoriesItemReader(@Qualifier("categoriesDataSource") DataSource dataSource)
            throws Exception {
        final SqlPagingQueryProviderFactoryBean sqlPagingQueryProviderFactoryBean
                = new SqlPagingQueryProviderFactoryBean();
        sqlPagingQueryProviderFactoryBean.setDataSource(dataSource);
        sqlPagingQueryProviderFactoryBean.setSelectClause("select name");
        sqlPagingQueryProviderFactoryBean.setFromClause("from categories");
        sqlPagingQueryProviderFactoryBean.setSortKey("name");

        return new JdbcPagingItemReaderBuilder<String>()
                .queryProvider(sqlPagingQueryProviderFactoryBean.getObject())
                .dataSource(dataSource)
                .pageSize(100)
                .rowMapper((resultSet, rowNum) -> resultSet.getString(1))
                .saveState(false)
                .build();
    }

}
