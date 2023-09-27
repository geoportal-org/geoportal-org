package com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.articles.reader.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * The type Category member dto.
 */
@Data
public class CategoryMemberDto {

    @JsonProperty("pageid")
    private String pageId;

}
