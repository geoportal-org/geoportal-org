package com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.articles.reader.service;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.articles.reader.dto.CategoryMemberDto;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.articles.reader.dto.CategoryMembersResults;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Category members service.
 */
@Log4j2
@Service
public class CategoryMembersService {

    private final WebClient webClient;
    private final CategoryMembersQueryFactory queryFactory;

    /**
     * Instantiates a new Category members service.
     *
     * @param webClient the web client
     * @param queryFactory the query factory
     */
    @Autowired
    public CategoryMembersService(@Qualifier("wikiApiWebClient") WebClient webClient,
            CategoryMembersQueryFactory queryFactory) {
        this.webClient = webClient;
        this.queryFactory = queryFactory;
    }

    /**
     * Returns list of pageIds, which are members of provided category. The category must not contain any whitespaces
     * and should be provided in format used by Wikipedia itself.
     *
     * @param category example value - Category:Earth_in_culture
     * @return the list
     */
    public List<String> fetchCategoryMembersPages(String category) {
        boolean nextPageExists;
        List<CategoryMemberDto> categoryMembers = new ArrayList<>();
        String nextPageIdentifier = "";
        do {
            CategoryMembersResults results = webClient
                    .get()
                    .uri(queryFactory.createUriQuery(category, nextPageIdentifier))
                    .retrieve()
                    .bodyToMono(CategoryMembersResults.class)
                    .timeout(Duration.ofSeconds(30))
                    .retryWhen(Retry.backoff(3, Duration.ofSeconds(5)))
                    .block();
            nextPageExists = results.hasNextPage();
            nextPageIdentifier = results.getNextPageQueryIdentifier();
            categoryMembers.addAll(results.getCategoryMembers());
        } while (nextPageExists);

        return categoryMembers
                .stream()
                .map(CategoryMemberDto::getPageId)
                .collect(Collectors.toList());
    }

}
