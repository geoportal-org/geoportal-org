package com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.esathesaurus.service;

import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.esathesaurus.service.model.BaseConceptDto;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.esathesaurus.service.model.ConceptDto;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.esathesaurus.service.model.EntryDto;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.esathesaurus.service.model.EntryType;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.esathesaurus.service.model.ThesaurusDataResponseDto;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.esathesaurus.service.query.EsaThesaurusQueryFactory;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The type Esa thesaurus service.
 */
@Log4j2
@Service
public class EsaThesaurusService {

    private final WebClient webClient;
    private final EsaThesaurusQueryFactory queryFactory;

    /**
     * Instantiates a new Esa thesaurus service.
     *
     * @param webClient the web client
     * @param queryFactory the query factory
     */
    @Autowired
    public EsaThesaurusService(
            @Qualifier("esaThesaurusWebClient") WebClient webClient,
            EsaThesaurusQueryFactory queryFactory) {
        this.webClient = webClient;
        this.queryFactory = queryFactory;
    }

    /**
     * Downloads and parses concept from external datasource.
     *
     * @param uri direct uri which identifies concept
     * @return the mono
     */
    public Mono<ConceptDto> fetchConcept(String uri) {
        log.debug("Fetching concept from uri: {}", uri);
        return webClient.get()
                .uri(queryFactory.createUriQuery(uri))
                .retrieve()
                .bodyToMono(ThesaurusDataResponseDto.class)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(3)))
                .map(ThesaurusDataResponseDto::getDataEntries)
                .map(this::filterByConceptType)
                .map(entries -> createConcept(uri, entries))
                .filter(Optional::isPresent)
                .map(Optional::get);
    }

    private List<EntryDto> filterByConceptType(List<EntryDto> entries) {
        if (entries == null) {
            return Collections.emptyList();
        }

        return entries.stream()
                .filter(entryDto -> entryDto.getType().contains(EntryType.CONCEPT.getName()))
                .collect(Collectors.toList());
    }

    private Optional<ConceptDto> createConcept(String uri, List<EntryDto> entries) {
        return entries.stream()
                .filter(entryDto -> entryDto.getUri().equals(uri))
                .findFirst()
                .map(entryConceptDto -> {
                    ConceptDto concept = new ConceptDto(entryConceptDto.getLabelValue(), entryConceptDto.getUri());
                    concept.setDefinition(entryConceptDto.getDefinitionValue());

                    List<BaseConceptDto> relatedConcepts = entries.stream()
                            .filter(entry -> entryConceptDto.getRelatedUris().contains(entry.getUri()))
                            .map(entryDto -> new BaseConceptDto(entryDto.getLabelValue(), entryDto.getUri()))
                            .collect(Collectors.toList());
                    concept.setRelatedConcepts(relatedConcepts);

                    List<BaseConceptDto> broaderConcepts = entries.stream()
                            .filter(entry -> entryConceptDto.getBroaderUris().contains(entry.getUri()))
                            .map(entryDto -> new BaseConceptDto(entryDto.getLabelValue(), entryDto.getUri()))
                            .collect(Collectors.toList());
                    concept.setBroaderConcepts(broaderConcepts);

                    List<BaseConceptDto> narrowerConcepts = entries.stream()
                            .filter(entry -> entryConceptDto.getNarrowerUris().contains(entry.getUri()))
                            .map(entryDto -> new BaseConceptDto(entryDto.getLabelValue(), entryDto.getUri()))
                            .collect(Collectors.toList());
                    concept.setNarrowerConcepts(narrowerConcepts);
                    return concept;
                });
    }
}
