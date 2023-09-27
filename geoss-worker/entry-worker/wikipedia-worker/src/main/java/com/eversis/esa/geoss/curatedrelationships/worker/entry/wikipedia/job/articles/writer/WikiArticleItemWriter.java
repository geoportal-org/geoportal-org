package com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.articles.writer;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.articles.model.WikiArticleEntry;

import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;

import java.util.List;
import javax.sql.DataSource;

/**
 * The type Wiki article item writer.
 */
@Log4j2
@StepScope
@Component("wikiArticleItemWriter")
public class WikiArticleItemWriter implements ItemWriter<WikiArticleEntry> {

    private final DataSource dataSource;
    private final WikiArticleSqlParameterSourceProvider sqlParameterSourceProvider;
    @Value("${geoss.curated.wikipedia.add-procedure-name}")
    private String storedProcedureName;

    /**
     * Instantiates a new Wiki article item writer.
     *
     * @param dataSource the data source
     */
    @Autowired
    public WikiArticleItemWriter(@Qualifier("geossCuratedDataSource") DataSource dataSource) {
        this.dataSource = dataSource;
        this.sqlParameterSourceProvider = new WikiArticleSqlParameterSourceProvider();
    }

    /**
     * Write.
     *
     * @param chunk the chunk
     * @throws Exception the exception
     */
    @Override
    public void write(Chunk<? extends WikiArticleEntry> chunk) throws Exception {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource).withProcedureName(storedProcedureName);
        List<? extends WikiArticleEntry> items = chunk.getItems();
        if (log.isDebugEnabled()) {
            log.debug("Executing batch with " + items.size() + " items.");
        }
        for (WikiArticleEntry entry : items) {
            jdbcCall.execute(sqlParameterSourceProvider.createSqlParameterSource(entry));
        }
    }
}
