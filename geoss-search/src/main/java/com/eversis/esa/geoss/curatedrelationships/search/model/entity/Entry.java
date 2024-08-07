package com.eversis.esa.geoss.curatedrelationships.search.model.entity;

import com.eversis.esa.geoss.curatedrelationships.search.model.DataSource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * The type Entry.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Entry implements Serializable {

    private String id;
    private String code;
    private String title;
    private String summary;
    private String logo;
    private DashboardContents dashboardContents;
    private DataSource dataSource;
    private DataSource displayDataSource;
    private LocalDateTime publishDate;
    private Organisation organisation;
    private BoundingBox coverage;
    private boolean isParent;
    private List<String> childrenTypes;
    private List<String> parentIds;
    private AccessPolicy accessPolicy;
    private List<EntryType> types;
    private List<Keyword> keywords;
    private List<TransferOption> transferOptions;

}
