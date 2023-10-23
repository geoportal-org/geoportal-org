package com.eversis.esa.geoss.curated.resources.model;

import com.eversis.esa.geoss.curated.common.model.DataSourceModel;
import com.eversis.esa.geoss.curated.common.model.TypeModel;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

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
    private DataSourceModel dataSource;

    @NotNull
    private DataSourceModel displayDataSource;

    @NotNull
    private DefinitionTypeModel definitionType;

    private String userId;

    @NotNull
    private List<TransferOptionModel> transferOptions = new ArrayList<>();

}
