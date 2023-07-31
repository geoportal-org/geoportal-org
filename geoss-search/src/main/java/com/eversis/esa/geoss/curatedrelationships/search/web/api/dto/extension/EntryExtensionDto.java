package com.eversis.esa.geoss.curatedrelationships.search.web.api.dto.extension;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Set;

/**
 * The type Entry extension dto.
 */
@Getter
@EqualsAndHashCode(of = {"entryExtensionId"})
@ToString
@Builder
public class EntryExtensionDto implements Serializable {

    private final Integer entryExtensionId;
    private String summary;
    private Set<String> keywords;

}
