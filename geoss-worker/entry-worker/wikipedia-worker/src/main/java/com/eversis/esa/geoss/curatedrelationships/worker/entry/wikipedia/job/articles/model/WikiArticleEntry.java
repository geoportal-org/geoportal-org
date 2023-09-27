package com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.articles.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The type Wiki article entry.
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(of = "code")
public class WikiArticleEntry {

    private String title;
    private String summary;
    private String logo;
    private String keywords;
    private String tags;
    private Integer typeId;
    private Integer definitionTypeId;
    private Integer accessPolicyId;
    private String code;
    private Integer sourceId;
    private Integer dataSourceId;
    private TransferOption transferOption;

}
