package com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.articles.reader.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * The type Image dto.
 */
@Data
class ImageDto {

    @JsonProperty("source")
    private String url;

}
