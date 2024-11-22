package com.eversis.esa.geoss.personaldata.survey.controller;

import com.eversis.esa.geoss.common.hateoas.PageMapper;
import com.eversis.esa.geoss.personaldata.survey.domain.Survey;
import com.eversis.esa.geoss.personaldata.survey.model.SurveyModel;
import com.eversis.esa.geoss.personaldata.survey.search.SearchQuery;
import com.eversis.esa.geoss.personaldata.survey.search.SearchQueryParser;
import com.eversis.esa.geoss.personaldata.survey.service.SurveyService;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.validation.Valid;
import java.util.Collections;
import java.util.List;

/**
 * The type Survey controller.
 */
@Log4j2
@RequiredArgsConstructor
@RepositoryRestController("/surveys")
@ResponseBody
@Tag(name = "surveys")
public class SurveyController {

    private final SearchQueryParser searchQueryParser = new SearchQueryParser();

    private final SurveyService surveyService;

    private final PageMapper pageMapper;

    private final EntityLinks entityLinks;

    /**
     * Parse and explain search query.
     *
     * @param query the query
     * @return the string
     */
    @RequestMapping(path = "/search", method = RequestMethod.OPTIONS)
    String search(@RequestParam("q") String query) {
        List<SearchQuery> searchQueries = searchQueryParser.parse(query);
        return surveyService.explain(searchQueries);
    }

    /**
     * Search surveys using query languages. Query format: [field] [operator] [value]. Where [field] - survey field
     * name, [value] - value to search, value with special characters should be in single quotes, date should be in ISO
     * format yyyy-MM-dd'T'HH:mm:ss.SSSXXX e.g. '2032-11-08T01:30:00.000Z', [operator] can be one of: (gt|>) - greater
     * than, (lt|<) - less than, (ge|>:) - greater than or equal, (le|<:) - less than or equal, (ne|!:) - not equal, :
     * (eq|-) equal, (like|~) - like. Multiple query are supported with logical operators: and &, or |. e.g. from eq
     * 'test@example.com' and classification like test
     *
     * @param query the query
     * @param pageable the pageable
     * @return the paged model
     */
    @GetMapping("/search")
    PagedModel<EntityModel<Survey>> search(@RequestParam("q") String query,
            @ParameterObject @PageableDefault Pageable pageable) {
        List<SearchQuery> searchQueries = searchQueryParser.parse(query);
        Page<Survey> search = surveyService.search(searchQueries, pageable);
        return pageMapper.toPagedModel(search, Survey.class, this::surveyLinks, this::surveyLinks);
    }

    /**
     * Deletes all surveys.
     *
     * @return a response entity with no content
     */
    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAllSurveys() {
        surveyService.deleteAllSurveys();
        return ResponseEntity.noContent().build();
    }

    /**
     * Post survey entity model. Only for migration old surveys, with override create date.
     *
     * @param surveyModel the survey model
     * @return the entity model
     */
    @Hidden
    @PostMapping("/hidden")
    @ResponseStatus(HttpStatus.CREATED)
    EntityModel<Survey> postSurvey(@RequestBody @Valid SurveyModel surveyModel) {
        Survey survey = surveyService.addSurvey(surveyModel);
        return EntityModel.of(survey, surveyLinks(survey));
    }

    private List<Link> surveyLinks(Survey survey) {
        if (survey == null) {
            return Collections.emptyList();
        }
        return List.of(
                entityLinks.linkToItemResource(Survey.class, survey.getId())
        );
    }

    private List<Link> surveyLinks() {
        return List.of(
                entityLinks.linkToCollectionResource(Survey.class)
        );
    }
}
