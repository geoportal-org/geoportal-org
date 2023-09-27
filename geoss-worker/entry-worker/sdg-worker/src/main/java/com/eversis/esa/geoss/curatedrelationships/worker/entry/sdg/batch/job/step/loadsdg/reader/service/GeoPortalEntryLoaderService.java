package com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg.batch.job.step.loadsdg.reader.service;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Entry;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.EntryRelation;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Type;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg.batch.job.step.loadsdg.reader.dto.geoportal.GeossSDGNode;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg.batch.job.step.loadsdg.reader.mapper.GeoPortalEntryMapper;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Geo portal entry loader service.
 */
@Log4j2
@Service
public class GeoPortalEntryLoaderService {

    private final WebClient webClient;
    private final GeoPortalQueryFactory queryFactory;
    private final GeoPortalEntryMapper entryMapper;

    /**
     * Instantiates a new Geo portal entry loader service.
     *
     * @param webClient the web client
     * @param queryFactory the query factory
     * @param entryMapper the entry mapper
     */
    public GeoPortalEntryLoaderService(@Qualifier("geoPortalWebClient") WebClient webClient,
            GeoPortalQueryFactory queryFactory,
            GeoPortalEntryMapper entryMapper) {
        this.webClient = webClient;
        this.queryFactory = queryFactory;
        this.entryMapper = entryMapper;
    }

    /**
     * Fetch entries list.
     *
     * @return the list
     */
    public List<Entry> fetchEntries() {
        List<Entry> indicators = fetchIndicators()
                .stream()
                .map(this::parseSdgIndicatorEntry)
                .collect(Collectors.toList());
        log.info("Fetched {} entries directly from GeoPortal", indicators.size());
        return indicators;
    }

    private List<GeossSDGNode> fetchIndicators() {
        return webClient
                .get()
                .uri(queryFactory.createGeoPortalSdgEntriesUriQuery())
                .retrieve()
                .bodyToFlux(GeossSDGNode.class)
                .filter(geossSDGNode -> isIndicator(geossSDGNode.getNumber()))
                .timeout(Duration.ofSeconds(30))
                .collectList()
                .block();
    }

    private boolean isIndicator(String sdgNumber) {
        return sdgNumber.chars().filter(value -> value == '.').count() == 2;
    }

    private Entry parseSdgIndicatorEntry(GeossSDGNode indicator) {
        Entry entry = entryMapper.createGeossSdgIndicatorEntry(indicator);
        String parentNumber = indicator.getNumber().substring(0, indicator.getNumber().lastIndexOf('.'));
        EntryRelation entryRelation = entryMapper.createSdgEntryRelation(parentNumber, Type.INFORMATION,
                indicator.getNumber(), entry.getType());
        entry.addRelation(entryRelation);
        return entry;
    }

}
