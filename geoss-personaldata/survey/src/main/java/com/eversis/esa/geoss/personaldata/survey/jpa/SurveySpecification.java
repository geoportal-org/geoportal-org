package com.eversis.esa.geoss.personaldata.survey.jpa;

import com.eversis.esa.geoss.personaldata.survey.domain.Survey;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.Instant;
import java.util.StringTokenizer;

/**
 * The type Survey specification.
 */
@RequiredArgsConstructor
public class SurveySpecification implements Specification<Survey> {

    private final SearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<Survey> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        return switch (criteria.getOperation()) {
            case GREATER_THAN -> greaterThan(root, builder);
            case LESS_THAN -> lessThan(root, builder);
            case GREATER_THAN_EQUAL -> greaterThanOrEqualTo(root, builder);
            case LESS_THAN_EQUAL -> lessThanOrEqualTo(root, builder);
            case NOT_EQUAL -> notEqual(root, builder);
            case EQUAL -> equal(root, builder);
            case LIKE -> like(root, builder);
            case LIKE_START -> likeStart(root, builder);
            case LIKE_END -> likeEnd(root, builder);
            case IN -> in(root, builder);
            case NOT_IN -> notIn(root, builder);
            default -> throw new IllegalStateException("Unexpected value: " + criteria.getOperation());
        };
    }

    private Predicate greaterThan(Root<Survey> root, CriteriaBuilder builder) {
        Path<?> path = getPath(root);
        if (path.getJavaType().isAssignableFrom(Instant.class)) {
            return builder.greaterThan(getPath(root), Instant.parse(criteria.getValue()));
        } else {
            return builder.greaterThan(getPath(root), criteria.getValue());
        }
    }

    private Predicate lessThan(Root<Survey> root, CriteriaBuilder builder) {
        Path<?> path = getPath(root);
        if (path.getJavaType().isAssignableFrom(Instant.class)) {
            return builder.lessThan(getPath(root), Instant.parse(criteria.getValue()));
        } else {
            return builder.lessThan(getPath(root), criteria.getValue());
        }
    }

    private Predicate greaterThanOrEqualTo(Root<Survey> root, CriteriaBuilder builder) {
        Path<?> path = getPath(root);
        if (path.getJavaType().isAssignableFrom(Instant.class)) {
            return builder.greaterThanOrEqualTo(getPath(root), Instant.parse(criteria.getValue()));
        } else {
            return builder.greaterThanOrEqualTo(getPath(root), criteria.getValue());
        }
    }

    private Predicate lessThanOrEqualTo(Root<Survey> root, CriteriaBuilder builder) {
        Path<?> path = getPath(root);
        if (path.getJavaType().isAssignableFrom(Instant.class)) {
            return builder.lessThanOrEqualTo(getPath(root), Instant.parse(criteria.getValue()));
        } else {
            return builder.lessThanOrEqualTo(getPath(root), criteria.getValue());
        }
    }

    private Predicate notEqual(Root<Survey> root, CriteriaBuilder builder) {
        Path<?> path = getPath(root);
        if (path.getJavaType().isAssignableFrom(Instant.class)) {
            return builder.notEqual(path, Instant.parse(criteria.getValue()));
        } else {
            return builder.notEqual(path, criteria.getValue());
        }
    }

    private Predicate equal(Root<Survey> root, CriteriaBuilder builder) {
        Path<?> path = getPath(root);
        if (path.getJavaType().isAssignableFrom(Instant.class)) {
            return builder.equal(path, Instant.parse(criteria.getValue()));
        } else {
            return builder.equal(path, criteria.getValue());
        }
    }

    private Predicate like(Root<Survey> root, CriteriaBuilder builder) {
        Path<?> path = getPath(root);
        if (path.getJavaType().isAssignableFrom(Instant.class)) {
            return builder.like(getPath(root), criteria.getValue());
        } else {
            return builder.like(builder.lower(getPath(root)), "%" + criteria.getValue().toLowerCase() + "%");
        }
    }

    private Predicate likeStart(Root<Survey> root, CriteriaBuilder builder) {
        Path<?> path = getPath(root);
        if (path.getJavaType().isAssignableFrom(Instant.class)) {
            return builder.like(getPath(root), criteria.getValue());
        } else {
            return builder.like(builder.lower(getPath(root)), "%" + criteria.getValue().toLowerCase());
        }
    }

    private Predicate likeEnd(Root<Survey> root, CriteriaBuilder builder) {
        Path<?> path = getPath(root);
        if (path.getJavaType().isAssignableFrom(Instant.class)) {
            return builder.like(getPath(root), criteria.getValue());
        } else {
            return builder.like(builder.lower(getPath(root)), criteria.getValue().toLowerCase() + "%");
        }
    }

    private Predicate in(Root<Survey> root, CriteriaBuilder builder) {
        return builder.in(getPath(root)).value(criteria.getValue());
    }

    private Predicate notIn(Root<Survey> root, CriteriaBuilder builder) {
        return builder.not(getPath(root)).in(criteria.getValue());
    }

    private <T> Path<T> getPath(Root<Survey> root) {
        Path<T> path = null;
        StringTokenizer tokenizer = new StringTokenizer(criteria.getKey(), ".");
        while (tokenizer.hasMoreElements()) {
            if (path == null) {
                path = root.get(tokenizer.nextToken());
            } else {
                path = path.get(tokenizer.nextToken());
            }
        }
        return path;
    }
}
