package com.eversis.esa.geoss.proxy.confiiguration;

import com.eversis.esa.geoss.proxy.document.ElementClickDoc;
import com.eversis.esa.geoss.proxy.document.ResourceErrorDoc;
import com.eversis.esa.geoss.proxy.document.SearchResultDoc;
import com.eversis.esa.geoss.proxy.document.SignInDoc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

/**
 * The type Init elasticsearch config.
 */
@Slf4j
@Configuration
public class InitElasticsearchConfig {

    @Autowired
    private ElasticsearchOperations operations;

    /**
     * Init index.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void initIndex() {

        operations.indexOps(ElementClickDoc.class)
                .putMapping(operations.indexOps(ElementClickDoc.class).createMapping());

        operations.indexOps(ResourceErrorDoc.class)
                .putMapping(operations.indexOps(ResourceErrorDoc.class).createMapping());

        operations.indexOps(SearchResultDoc.class)
                .putMapping(operations.indexOps(SearchResultDoc.class).createMapping());

        operations.indexOps(SignInDoc.class)
                .putMapping(operations.indexOps(SignInDoc.class).createMapping());

    }

}
