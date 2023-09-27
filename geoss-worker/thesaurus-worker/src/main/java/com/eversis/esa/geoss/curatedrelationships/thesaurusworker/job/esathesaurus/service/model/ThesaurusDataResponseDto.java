package com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.esathesaurus.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * The type Thesaurus data response dto.
 */
@Data
public class ThesaurusDataResponseDto {

    @JsonProperty("graph")
    private List<EntryDto> dataEntries;

}
