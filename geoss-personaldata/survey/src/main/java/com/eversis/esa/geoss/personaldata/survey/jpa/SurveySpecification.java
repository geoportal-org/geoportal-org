package com.eversis.esa.geoss.personaldata.survey.jpa;

import com.eversis.esa.geoss.personaldata.survey.domain.Survey;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

/**
 * The type Survey specification.
 */
@RequiredArgsConstructor
public class SurveySpecification implements Specification<Survey> {

    private final SearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<Survey> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        return switch (criteria.getOperation()) {
            case GREATER_THAN -> builder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());
            case LESS_THAN -> builder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());
            case GREATER_THAN_EQUAL ->
                    builder.greaterThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
            case LESS_THAN_EQUAL ->
                    builder.lessThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
            case NOT_EQUAL -> builder.notEqual(root.get(criteria.getKey()), criteria.getValue());
            case EQUAL -> builder.equal(root.get(criteria.getKey()), criteria.getValue());
            case LIKE -> builder.like(builder.lower(root.get(criteria.getKey())),
                    "%" + criteria.getValue().toString().toLowerCase() + "%");
            case LIKE_START -> builder.like(builder.lower(root.get(criteria.getKey())),
                    "%" + criteria.getValue().toString().toLowerCase());
            case LIKE_END -> builder.like(builder.lower(root.get(criteria.getKey())),
                    criteria.getValue().toString().toLowerCase() + "%");
            case IN -> builder.in(root.get(criteria.getKey())).value(criteria.getValue());
            case NOT_IN -> builder.not(root.get(criteria.getKey())).in(criteria.getValue());
            default -> throw new IllegalStateException("Unexpected value: " + criteria.getOperation());
        };
    }
}
