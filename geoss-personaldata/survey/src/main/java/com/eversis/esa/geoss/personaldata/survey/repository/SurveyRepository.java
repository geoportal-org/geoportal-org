package com.eversis.esa.geoss.personaldata.survey.repository;

import com.eversis.esa.geoss.personaldata.survey.domain.Survey;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * The interface Survey repository.
 */
@RepositoryRestResource(path = "surveys", collectionResourceRel = "surveys", itemResourceRel = "survey")
@Tag(name = "survey")
public interface SurveyRepository extends JpaRepository<Survey, Long> {

}
