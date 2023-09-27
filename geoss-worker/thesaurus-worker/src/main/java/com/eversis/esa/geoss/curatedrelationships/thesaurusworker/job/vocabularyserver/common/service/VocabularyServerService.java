package com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.vocabularyserver.common.service;

import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.vocabularyserver.common.service.model.ConceptDto;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.vocabularyserver.common.service.model.TermDto;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.vocabularyserver.common.service.model.TermResponseDto;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.vocabularyserver.common.service.query.VocabularyServerQueryFactory;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.model.ThesaurusType;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.util.StringUtils;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import jakarta.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

/**
 * The type Vocabulary server service.
 */
@Log4j2
public abstract class VocabularyServerService {

    private static final int MAX_RETRY_COUNT = 4;
    private static final int RETRY_SECONDS_INTERVAL = 3;

    private final WebClient webClient;
    private final VocabularyServerQueryFactory queryFactory;

    /**
     * Instantiates a new Vocabulary server service.
     *
     * @param webClient the web client
     * @param queryFactory the query factory
     */
    public VocabularyServerService(
            WebClient webClient,
            VocabularyServerQueryFactory queryFactory) {
        this.webClient = webClient;
        this.queryFactory = queryFactory;
    }

    /**
     * Downloads and parses concept from external datasource basing on provided common URI and term's id.
     */
    protected Mono<ConceptDto> fetchConcept(ThesaurusType source, String baseUri, String termId) {
        log.debug("Fetching concept from thesaurus: {} with base-uri: {} related to termId: {}", source.name(), baseUri,
                termId);

        Mono<TermDto> term = fetchTerm(baseUri, termId);
        Mono<List<TermDto>> relatedTerms = fetchRelatedTerms(baseUri, termId);
        Mono<List<TermDto>> narrowerTerms = fetchNarrowerTerms(baseUri, termId);
        Mono<List<TermDto>> broaderTerms = fetchBroaderTerms(baseUri, termId);

        return Mono.zip(term, relatedTerms, narrowerTerms, broaderTerms)
                .map(objects -> new ConceptDto(
                        source,
                        objects.getT1().getName(),
                        termId,
                        objects.getT1().isMetaTerm(),
                        objects.getT2(),
                        objects.getT3(),
                        objects.getT4()));
    }

    /**
     * Fetches top terms from vocabulary using provided common URI.
     */
    protected Flux<TermDto> fetchTopTerms(String baseUri) {
        if (log.isTraceEnabled()) {
            log.trace("Fetching top terms from uri: {}", baseUri);
        }
        return webClient.get()
                .uri(queryFactory.createFetchTopTermsQuery(baseUri))
                .accept(MediaType.TEXT_XML)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(TermResponseDto.class)
                .map(TermResponseDto::getTerms)
                .flatMapMany(Flux::fromIterable);
    }

    private Mono<TermDto> fetchTerm(String baseUri, String termId) {
        if (log.isTraceEnabled()) {
            log.trace("Fetching term's data from uri: {} related to termId: {}", baseUri, termId);
        }
        return webClient.get()
                .uri(queryFactory.createFetchTermQuery(baseUri, termId))
                .accept(MediaType.TEXT_XML)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(TermResponseDto.class)
                .retryWhen(Retry.backoff(MAX_RETRY_COUNT, Duration.ofSeconds(RETRY_SECONDS_INTERVAL)))
                .map(TermResponseDto::getSingleTerm)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(this::processTermDto);
    }

    private Mono<List<TermDto>> fetchRelatedTerms(String baseUri, String termId) {
        if (log.isTraceEnabled()) {
            log.trace("Fetching related terms from uri: {} related to termId: {}", baseUri, termId);
        }
        return webClient.get()
                .uri(queryFactory.createFetchRelatedTermsQuery(baseUri, termId))
                .accept(MediaType.TEXT_XML)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(TermResponseDto.class)
                .retryWhen(Retry.backoff(MAX_RETRY_COUNT, Duration.ofSeconds(RETRY_SECONDS_INTERVAL)))
                .map(TermResponseDto::getTerms)
                .flatMapMany(Flux::fromIterable)
                .map(this::processTermDto)
                .filter(termDto -> !termId.equals(termDto.getTermId()))
                .collectList();
    }

    private Mono<List<TermDto>> fetchNarrowerTerms(String baseUri, String termId) {
        if (log.isTraceEnabled()) {
            log.trace("Fetching narrower terms from uri: {} related to termId: {}", baseUri, termId);
        }
        return webClient.get()
                .uri(queryFactory.createFetchNarrowerTermsQuery(baseUri, termId))
                .accept(MediaType.TEXT_XML)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(TermResponseDto.class)
                .retryWhen(Retry.backoff(MAX_RETRY_COUNT, Duration.ofSeconds(RETRY_SECONDS_INTERVAL)))
                .map(TermResponseDto::getTerms)
                .flatMapMany(Flux::fromIterable)
                .map(this::processTermDto)
                .filter(termDto -> !termId.equals(termDto.getTermId()))
                .collectList();
    }

    private Mono<List<TermDto>> fetchBroaderTerms(String baseUri, String termId) {
        if (log.isTraceEnabled()) {
            log.trace("Fetching broader terms from uri: {} related to termId: {}", baseUri, termId);
        }
        return webClient.get()
                .uri(queryFactory.createFetchBroaderTermsQuery(baseUri, termId))
                .accept(MediaType.TEXT_XML)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(TermResponseDto.class)
                .retryWhen(Retry.backoff(MAX_RETRY_COUNT, Duration.ofSeconds(RETRY_SECONDS_INTERVAL)))
                .map(TermResponseDto::getTerms)
                .flatMapMany(Flux::fromIterable)
                .map(this::processTermDto)
                .filter(termDto -> !termId.equals(termDto.getTermId()))
                .collectList();
    }

    private TermDto processTermDto(@NotNull TermDto termDto) {
        termDto.setTermId(termDto.getTermId().strip());
        termDto.setName(StringUtils.removeSurroundingStrings(termDto.getName().trim(), "[", "]").trim());
        return termDto;
    }

}
