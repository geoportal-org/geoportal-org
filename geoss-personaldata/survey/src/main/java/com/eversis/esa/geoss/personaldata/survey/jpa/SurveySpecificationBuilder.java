package com.eversis.esa.geoss.personaldata.survey.jpa;

import com.eversis.esa.geoss.common.domain.AuditableEmbeddable_;
import com.eversis.esa.geoss.personaldata.survey.domain.Survey;
import com.eversis.esa.geoss.personaldata.survey.domain.Survey_;
import com.eversis.esa.geoss.personaldata.survey.search.SearchQuery;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * The type Survey specification builder.
 */
@Log4j2
public class SurveySpecificationBuilder {

    private static final Map<String, SearchOperation> OPERATIONS = Map.of(
            ">", SearchOperation.GREATER_THAN,
            "<", SearchOperation.LESS_THAN,
            ">:", SearchOperation.GREATER_THAN_EQUAL,
            "<:", SearchOperation.LESS_THAN_EQUAL,
            "!:", SearchOperation.NOT_EQUAL,
            ":", SearchOperation.EQUAL,
            "~", SearchOperation.LIKE
    );

    private static final Map<String, SearchLogical> LOGICAL = Map.of(
            "&", SearchLogical.AND,
            "|", SearchLogical.OR
    );

    private List<SearchQuery> searchQueries = Collections.emptyList();

    /**
     * With survey specification builder.
     *
     * @param searchQueries the search queries
     * @return the survey specification builder
     */
    public SurveySpecificationBuilder with(List<SearchQuery> searchQueries) {
        this.searchQueries = searchQueries;
        return this;
    }

    /**
     * Build specification.
     *
     * @return the specification
     */
    public Specification<Survey> build() {
        Specification<Survey> specification = null;
        for (SearchQuery searchQuery : searchQueries) {
            SearchCriteria criteria = new SearchCriteria();
            criteria.setKey(getPath(searchQuery.key()));
            criteria.setValue(searchQuery.value());
            criteria.setOperation(OPERATIONS.get(searchQuery.operator()));
            SearchLogical logical = (searchQuery.logical() != null) ? LOGICAL.get(searchQuery.logical()) : null;
            log.trace("{} {} {} {}", logical, criteria.getKey(), criteria.getOperation(), criteria.getValue());
            if (specification == null) {
                specification = Specification.where(new SurveySpecification(criteria));
            } else {
                if (logical == SearchLogical.AND) {
                    specification = specification.and(new SurveySpecification(criteria));
                } else if (logical == SearchLogical.OR) {
                    specification = specification.or(new SurveySpecification(criteria));
                } else {
                    throw new IllegalStateException("Unexpected value: " + logical);
                }
            }
        }
        return specification;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (SearchQuery searchQuery : searchQueries) {
            if (searchQuery.logical() != null) {
                sb.append(" ").append(LOGICAL.get(searchQuery.logical())).append(" ");
            }
            sb.append(searchQuery.key()).append(" ");
            sb.append(OPERATIONS.get(searchQuery.operator())).append(" ");
            sb.append(searchQuery.value());
        }
        return sb.toString();
    }

    private String getPath(String key) {
        return switch (key) {
            case "createdBy" -> Survey_.AUDITABLE + "." + AuditableEmbeddable_.CREATED_BY;
            case "createdOn" -> Survey_.AUDITABLE + "." + AuditableEmbeddable_.CREATED_DATE;
            case "modifiedBy" -> Survey_.AUDITABLE + "." + AuditableEmbeddable_.LAST_MODIFIED_BY;
            case "modifiedOn" -> Survey_.AUDITABLE + "." + AuditableEmbeddable_.LAST_MODIFIED_DATE;
            default -> key;
        };
    }
}
