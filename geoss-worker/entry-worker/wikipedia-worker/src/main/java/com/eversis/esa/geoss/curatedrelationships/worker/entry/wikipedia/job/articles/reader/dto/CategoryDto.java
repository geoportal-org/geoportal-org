package com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.articles.reader.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * The type Category dto.
 */
@Data
class CategoryDto {

    @JsonProperty("title")
    private String title;

    /**
     * Get formatted title string.
     *
     * @return the string
     */
    public String getFormattedTitle() {
        return title != null ? title.replaceFirst("Category:", "").trim() : "";
    }

}
