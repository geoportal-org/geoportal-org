package com.eversis.esa.geoss.proxy.service.impl;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import com.eversis.esa.geoss.proxy.domain.ViewCounter;
import com.eversis.esa.geoss.proxy.domain.ViewCounterModel;
import com.eversis.esa.geoss.proxy.exception.ViewCounterException;
import com.eversis.esa.geoss.proxy.mapper.impl.ViewCounterMapper;
import com.eversis.esa.geoss.proxy.repository.ViewCounterRepository;
import com.eversis.esa.geoss.proxy.service.ViewCounterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ViewCounterServiceImpl implements ViewCounterService {

    private static final String INDEX_NAME = "geoss_index";
    private static final String FIELD_UI_ACTION = "ui_action";
    private static final String FIELD_DS_REQID = "ds_reqID";
    private static final String FIELD_UI_ENTRY_ID = "ui_entry_id";
    private final ViewCounterRepository viewCounterRepository;
    private final ViewCounterMapper viewCounterMapper;
    private final ElasticsearchClient elasticsearchClient;

    /**
     * Instantiates a new Score service.
     *
     * @param viewCounterRepository the score repository
     * @param viewCounterMapper the score mapper
     */
    public ViewCounterServiceImpl(ViewCounterRepository viewCounterRepository, ViewCounterMapper viewCounterMapper,
            ElasticsearchClient elasticsearchClient) {
        this.viewCounterRepository = viewCounterRepository;
        this.viewCounterMapper = viewCounterMapper;
        this.elasticsearchClient = elasticsearchClient;
    }

    @Override
    public void increaseCounter(ViewCounterModel viewCounterModel) {
        viewCounterRepository.save(viewCounterMapper.mapToDocument(viewCounterModel));
    }

    @Override
    public ViewCounter getCounter(String entryId) {
        ViewCounter viewCounter = new ViewCounter();
        viewCounter.setEntryId(entryId);
        long counter;

        Query query = Query.of(q -> q
                .bool(b -> b
                        .must(m -> m.exists(e -> e.field(FIELD_UI_ACTION)))
                        .mustNot(mn -> mn.exists(e -> e.field(FIELD_DS_REQID)))
                        .must(m -> m.term(t -> t.field(FIELD_UI_ENTRY_ID).value(entryId)))
                )
        );

        var request = new SearchRequest.Builder()
                .index(INDEX_NAME)
                .query(query)
                .size(0)
                .build();

        try {
            var response = elasticsearchClient.search(request, Void.class);
            counter = response.hits().total().value();
        } catch (IOException e) {
            log.warn("Could not get counter for entryId: {}. Error: {}", entryId, e.getMessage(), e);
            throw new ViewCounterException("Could not get counter for entryId: " + entryId, e);
        }
        log.info("Counter for entryId: {} is: {}", entryId, counter);
        viewCounter.setCounter(counter);
        return viewCounter;
    }

    @Override
    public List<ViewCounter> getCounter(List<String> entryIds) {

        Query query = Query.of(q -> q
                .bool(b -> b
                        .must(m -> m.exists(e -> e.field(FIELD_UI_ACTION)))
                        .mustNot(mn -> mn.exists(e -> e.field(FIELD_DS_REQID)))
                        .filter(f -> f.terms(t -> t.field(FIELD_UI_ENTRY_ID)
                                .terms(ts -> ts.value(entryIds.stream().map(FieldValue::of).toList()))))
                )
        );

        var request = new SearchRequest.Builder()
                .index(INDEX_NAME)
                .query(query)
                .aggregations("entryId_counts", a -> a
                        .terms(t -> t.field(FIELD_UI_ENTRY_ID))
                )
                .size(0)
                .build();

        try {
            var response = elasticsearchClient.search(request, Void.class);

            var agg = response.aggregations().get("entryId_counts");
            log.info("Agg: {}", agg);
            if (agg != null) {
                return agg.sterms().buckets().array().stream().map(bucket -> {
                    ViewCounter viewCounter = new ViewCounter();
                    viewCounter.setEntryId(bucket.key().stringValue());
                    viewCounter.setCounter(bucket.docCount());
                    return viewCounter;
                }).collect(Collectors.toList());
            }
        } catch (IOException e) {
            log.warn("Could not get counters for entryIds: {}. Error: {}", entryIds, e.getMessage(), e);
            throw new ViewCounterException("Could not get counters for entryIds", e);
        }
        return Collections.emptyList();
    }

}
