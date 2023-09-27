package com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.categories.writer;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * The type Wiki category item writer configuration.
 */
@Configuration
class WikiCategoryItemWriterConfiguration {

    /**
     * Item writer item writer.
     *
     * @param dataSource the data source
     * @return the item writer
     */
    @StepScope
    @Bean("wikiCategoryItemWriter")
    ItemWriter<String> itemWriter(@Qualifier("categoriesDataSource") DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<String>()
                .dataSource(dataSource)
                .beanMapped()
                .sql("INSERT INTO CATEGORIES( name ) values ( ? )")
                .itemPreparedStatementSetter((category, preparedStatement) -> preparedStatement.setString(1, category))
                .build();
    }

}
