package com.eversis.esa.geoss.personaldata.survey.service.internal;

import com.eversis.esa.geoss.personaldata.survey.domain.Survey;
import com.eversis.esa.geoss.personaldata.survey.jpa.SurveySpecificationBuilder;
import com.eversis.esa.geoss.personaldata.survey.repository.SurveyRepository;
import com.eversis.esa.geoss.personaldata.survey.search.SearchQuery;
import com.eversis.esa.geoss.personaldata.survey.service.SurveyService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * The type Survey service.
 */
@Log4j2
@RequiredArgsConstructor
@Service
@Transactional
public class SurveyServiceImpl implements SurveyService {

    private final SurveyRepository surveyRepository;

    @Override
    public String explain(List<SearchQuery> searchQueries) {
        log.debug("searchQueries:{}", searchQueries);

        SurveySpecificationBuilder surveySpecificationBuilder = new SurveySpecificationBuilder();
        String specification = surveySpecificationBuilder.with(searchQueries).toString();
        log.debug("specification:{}", specification);

        return specification;
    }

    @Override
    public Page<Survey> search(List<SearchQuery> searchQueries, Pageable pageable) {
        log.debug("searchQueries:{},pageable:{}", searchQueries, pageable);

        SurveySpecificationBuilder surveySpecificationBuilder = new SurveySpecificationBuilder();
        Specification<Survey> specification = surveySpecificationBuilder.with(searchQueries).build();
        log.debug("specification:{}", surveySpecificationBuilder.toString());

        Page<Survey> surveys = surveyRepository.findAll(specification, pageable);
        log.debug("surveys:{}", surveys);
        return surveys;
    }
}
