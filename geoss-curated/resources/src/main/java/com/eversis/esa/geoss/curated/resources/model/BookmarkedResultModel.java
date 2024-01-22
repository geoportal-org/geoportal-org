package com.eversis.esa.geoss.curated.resources.model;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

/**
 * The type Bookmarked result model.
 */
@Data
public class BookmarkedResultModel {

    @NotNull
    private String name;

    @NotNull
    private String targetId;

    private String entryXml;

    @NotNull
    private String userId;

    private String currMap;

    private Long groupId;

    @NotNull
    private String dataSource;

    private String sourceBaseUrl;

    private Integer valid;

}
