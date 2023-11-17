package com.eversis.esa.geoss.curated.elasticsearch.event;

import com.eversis.esa.geoss.curated.elasticsearch.service.ElasticsearchService;
import com.eversis.esa.geoss.curated.resources.domain.Entry;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * The type Entry listener.
 */
@Log4j2
@RequiredArgsConstructor
@Component("elasticEntryListener")
public class EntryListener {

    private final ElasticsearchService elasticsearchService;

    /**
     * Handle entry.
     *
     * @param entry the entry
     */
    @EventListener(Entry.class)
    public void handleEntry(Entry entry) {
        log.info("entry:{}", entry);
        try {
            elasticsearchService.indexEntry(entry);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
