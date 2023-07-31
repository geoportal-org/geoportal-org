package com.eversis.esa.geoss.curatedrelationships.search.service.search.impl;

import com.eversis.esa.geoss.curatedrelationships.search.model.SearchQuery;
import com.eversis.esa.geoss.curatedrelationships.search.model.SearchQueryType;
import com.eversis.esa.geoss.curatedrelationships.search.service.search.SearchQueryService;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;
import javax.validation.constraints.NotNull;

@Service
@Validated
class SearchQueryServiceImpl implements SearchQueryService {

    private static final String SPACE_SEPARATOR = " ";
    private static final String PHRASE_SEPARATOR = " OR ";

    @Override
    public SearchQuery processSearchPhrase(@NotNull SearchQuery searchQuery) {
        Optional<String> optionalPhrase = searchQuery.getOptionalPhrase();
        if (optionalPhrase.isPresent()) {
            String phrase = optionalPhrase.get();
            SearchQueryType queryType = getSearchQueryType(phrase);
            searchQuery.setQueryType(queryType);
            if (queryType == SearchQueryType.FULL_TEXT) {
                searchQuery.setOptionalPhrase(phrase.replaceAll(PHRASE_SEPARATOR, SPACE_SEPARATOR));
            }

        }
        return searchQuery;
    }

    @Override
    public SearchQueryType getSearchQueryType(@NotNull String phrase) {
        if (phrase.contains(PHRASE_SEPARATOR) || !phrase.contains(SPACE_SEPARATOR)) {
            return SearchQueryType.FULL_TEXT;
        } else {
            return SearchQueryType.KEYWORD;
        }
    }

}
