package com.eversis.esa.geoss.personaldata.survey.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.Data;

/**
 * The type Survey.
 */
@Data
public class SurveyModel {

    @JsonProperty(access = Access.READ_ONLY)
    private Long id;

    private String dateTime;
    private String from;

    private String classification;

    private String foundLookingFor;

    private String lookingFor;

    private String searchCriteria;

    private String visualization;

    private String adequately;

    private String interest;

    private String organized;

    private String impression;

    // @JsonProperty(access = Access.READ_ONLY)
    // @JsonUnwrapped
    // private AuditableModel auditable = new AuditableModel();
}
