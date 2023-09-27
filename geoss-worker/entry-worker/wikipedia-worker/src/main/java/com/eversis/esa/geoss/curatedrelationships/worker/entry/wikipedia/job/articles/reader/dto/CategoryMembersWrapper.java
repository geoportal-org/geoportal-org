package com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.articles.reader.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Category members wrapper.
 */
@Data
public class CategoryMembersWrapper {

    @JsonProperty("categorymembers")
    private List<CategoryMemberDto> categoryMembers = new ArrayList<>();

}
