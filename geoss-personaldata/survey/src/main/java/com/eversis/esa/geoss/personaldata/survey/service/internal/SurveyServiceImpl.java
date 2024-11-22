package com.eversis.esa.geoss.personaldata.survey.service.internal;

import com.eversis.esa.geoss.personaldata.survey.domain.Survey;
import com.eversis.esa.geoss.personaldata.survey.jpa.SurveySpecificationBuilder;
import com.eversis.esa.geoss.personaldata.survey.model.SurveyModel;
import com.eversis.esa.geoss.personaldata.survey.repository.SurveyRepository;
import com.eversis.esa.geoss.personaldata.survey.search.SearchQuery;
import com.eversis.esa.geoss.personaldata.survey.service.SurveyService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.mapping.MappingException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * The type Survey service.
 */
@Log4j2
@RequiredArgsConstructor
@Service
@Transactional
public class SurveyServiceImpl implements SurveyService {

    private final SurveyRepository surveyRepository;

    private ConversionService conversionService;

    /**
     * Sets conversion service.
     *
     * @param conversionService the conversion service
     */
    @Autowired
    public void setConversionService(@Qualifier("mvcConversionService") ConversionService conversionService) {
        this.conversionService = conversionService;
    }

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

    @Override
    @Transactional
    public void deleteAllSurveys() {
        surveyRepository.deleteAll();
    }

    @Override
    public Survey addSurvey(SurveyModel surveyModel) {
        log.debug("surveyModel:{}", surveyModel);
        return Optional.ofNullable(conversionService.convert(surveyModel, Survey.class))
                .map(survey -> {
                    survey = surveyRepository.save(survey);
                    log.debug("survey:{}", survey);
                    return survey;
                })
                .orElseThrow(() -> new MappingException("Survey model not converted"));
    }
}
