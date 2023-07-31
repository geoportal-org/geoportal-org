package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class EntryExtensionELK implements Serializable {

    private Integer entryExtensionId;
    private String summary;
    private List<String> keywords = new ArrayList<>();

    private String username;
    private String userId;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

}
