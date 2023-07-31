package com.eversis.esa.geoss.curatedrelationships.search.model.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@EqualsAndHashCode(of = {"entryExtensionId"})
@ToString
@Builder
public class EntryExtension implements Serializable {

    private final Integer entryExtensionId;
    private String summary;
    private Set<String> keywords;

    private String username;
    private String userId;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

}
