package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.model;

import com.eversis.esa.geoss.curatedrelationships.search.model.entity.AccessPolicy;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.DashboardContents;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ResourceEntryELK {

    private String id;
    private String code;
    private String title;
    private String summary;
    private String logo;
    private DashboardContents dashboardContents;
    private String dataSource;
    private String displayDataSource;
    private LocalDateTime publishDate;
    private OrganisationELK organisation;
    private BoundingBoxELK coverage;
    @JsonProperty("hasChildren")
    private boolean isParent;
    @JsonProperty("childrenTypes")
    private String childrenTypes;
    private AccessPolicy accessPolicy;
    @JsonProperty("parentId")
    private List<String> parentIds = new ArrayList<>();
    @JsonProperty("type")
    private List<String> types = new ArrayList<>();
    private List<String> keywords = new ArrayList<>();
    private List<TransferOptionELK> transferOptions = new ArrayList<>();

}
