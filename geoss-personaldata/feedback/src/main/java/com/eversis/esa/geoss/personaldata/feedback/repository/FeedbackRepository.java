package com.eversis.esa.geoss.personaldata.feedback.repository;

import com.eversis.esa.geoss.personaldata.feedback.domain.Feedback;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * The interface Feedback repository.
 */
@RepositoryRestResource(path = "feedbacks", collectionResourceRel = "feedbacks", itemResourceRel = "feedback")
@Tag(name = "feedbacks")
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

}
