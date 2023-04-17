package com.eversis.esa.geoss.personaldata.searches.service.internal;

import com.eversis.esa.geoss.personaldata.searches.domain.HighlightedSearches;
import com.eversis.esa.geoss.personaldata.searches.repository.HighlightedSearchesRepository;
import com.eversis.esa.geoss.personaldata.searches.repository.SavedSearchesRepository;
import com.eversis.esa.geoss.personaldata.searches.service.SearchesService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The type Searches service.
 */
@Log4j2
@RequiredArgsConstructor
@Service
@Transactional
public class SearchesServiceImpl implements SearchesService {

    private final HighlightedSearchesRepository highlightedSearchesRepository;

    private final SavedSearchesRepository savedSearchesRepository;

    @Override
    public Long createHighlightedSearchesFromSavedSearches(Long savedSearchesId, String name) {
        log.debug("savedSearchesId:{}", savedSearchesId);
        log.debug("name:{}", name);
        return savedSearchesRepository.findById(savedSearchesId)
                .map(savedSearches -> {
                    log.debug("savedSearches:{}", savedSearches);
                    HighlightedSearches highlightedSearches = new HighlightedSearches();
                    highlightedSearches.setName(name);
                    highlightedSearches.setPhrase(savedSearches.getPhrase());
                    highlightedSearches.setUrl(savedSearches.getUrl());
                    highlightedSearches.setEnabled(false);
                    highlightedSearches.setDefaultSearch(false);
                    highlightedSearches = highlightedSearchesRepository.save(highlightedSearches);
                    log.debug("highlightedSearches:{}", highlightedSearches);
                    return highlightedSearches.getId();
                })
                .orElseThrow(() -> new ResourceNotFoundException("Saved searches not found"));
    }
}
