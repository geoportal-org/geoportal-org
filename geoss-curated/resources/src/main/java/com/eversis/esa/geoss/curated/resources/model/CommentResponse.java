package com.eversis.esa.geoss.curated.resources.model;

import lombok.Data;

/**
 * The type Comment response.
 */
@Data
public class CommentResponse {

    private Double score;

    private String createdDate;

    private String comment;

    private String userName;

    private String userId;

}
