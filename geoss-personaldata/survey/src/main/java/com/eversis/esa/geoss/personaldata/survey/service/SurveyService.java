package com.eversis.esa.geoss.personaldata.survey.service;

import com.eversis.esa.geoss.personaldata.survey.domain.Survey;
import com.eversis.esa.geoss.personaldata.survey.search.SearchQuery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * The interface Survey service.
 */
public interface SurveyService {

    /**
     * Explain string.
     *
     * @param searchQueries the search queries
     * @return the string
     */
    String explain(List<SearchQuery> searchQueries);

    /**
     * Search page.
     *
     * @param searchQueries the search queries
     * @param pageable the pageable
     * @return the page
     */
    Page<Survey> search(List<SearchQuery> searchQueries, Pageable pageable);

    /**
     * Delete all surveys.
     */
    void deleteAllSurveys();

}
