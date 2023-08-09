package com.eversis.esa.geoss.curated.resources.model;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * The type Entry model.
 */
@Data
public class EntryModel {

    @NotNull
    private String title;

    private String summary;

    private String logo;

    private String coverage;

    @NotNull
    private TypeModel type;

    private DashboardContentsModel dashboardContents;

    @NotNull
    private AccessPolicyModel accessPolicy;

    private String keywords;

    private String tags;

    private String code;

    @NotNull
    private OrganisationModel organisation;

    @NotNull
    private SourceModel source;

    @NotNull
    private DataSourceModel dataSources;

    @NotNull
    private DataSourceModel displayDataSources;

    @NotNull
    private DefinitionTypeModel definitionType;

    private Long userId;

}
