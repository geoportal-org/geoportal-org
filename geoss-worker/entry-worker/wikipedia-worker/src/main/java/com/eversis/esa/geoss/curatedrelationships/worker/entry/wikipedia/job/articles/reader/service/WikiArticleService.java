package com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.articles.reader.service;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.articles.model.WikiArticle;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.articles.reader.dto.ArticleDto;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.articles.reader.dto.ArticlesResults;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The type Wiki article service.
 */
@Log4j2
@Service
public class WikiArticleService {

    private final WebClient webClient;
    private final WikiArticleQueryFactory queryFactory;
    @Value("${wikidata.api.pages-chunk-size}")
    private int pagesChunkSize;

    /**
     * Instantiates a new Wiki article service.
     *
     * @param webClient the web client
     * @param queryFactory the query factory
     */
    @Autowired
    public WikiArticleService(@Qualifier("wikiApiWebClient") WebClient webClient,
            WikiArticleQueryFactory queryFactory) {
        this.webClient = webClient;
        this.queryFactory = queryFactory;
    }

    /**
     * Returns articles basic content for provided list of pageId values.
     *
     * @param pageIds list of pageIds
     * @return the list
     */
    public List<WikiArticle> fetchWikiArticles(List<String> pageIds) {
        List<Mono<Collection<ArticleDto>>> wikiArticles = new ArrayList<>();
        for (int i = 0; i < pageIds.size(); i += pagesChunkSize) {
            int end = Math.min(pageIds.size(), i + pagesChunkSize);
            Mono<Collection<ArticleDto>> results = webClient
                    .get()
                    .uri(queryFactory.createUriQuery(pageIds.subList(i, end)))
                    .retrieve()
                    .bodyToMono(ArticlesResults.class)
                    .timeout(Duration.ofSeconds(30))
                    .retryWhen(Retry.backoff(3, Duration.ofSeconds(5)))
                    .map(articlesResults -> articlesResults.getArticles().values());
            wikiArticles.add(results);
        }
        return Flux
                .merge(wikiArticles)
                .flatMapIterable(articleDtos -> articleDtos)
                .map(ArticleDto::toWikiArticle)
                .collectList()
                .block();
    }

}
