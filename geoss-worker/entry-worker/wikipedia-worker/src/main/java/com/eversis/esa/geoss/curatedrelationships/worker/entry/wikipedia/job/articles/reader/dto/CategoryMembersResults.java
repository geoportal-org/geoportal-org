package com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.articles.reader.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * The type Category members results.
 */
@Data
public class CategoryMembersResults {

    @JsonProperty("continue")
    private ContinueElement continueElement;

    @JsonProperty("query")
    private CategoryMembersWrapper queryResults;

    /**
     * Gets category members.
     *
     * @return the category members
     */
    public List<CategoryMemberDto> getCategoryMembers() {
        return queryResults != null ? queryResults.getCategoryMembers() : Collections.emptyList();
    }

    /**
     * Gets next page query identifier.
     *
     * @return the next page query identifier
     */
    public String getNextPageQueryIdentifier() {
        return continueElement != null ? continueElement.getCmcontinue() : "";
    }

    /**
     * Has next page boolean.
     *
     * @return the boolean
     */
    public boolean hasNextPage() {
        return continueElement != null && continueElement.hasNextPage();
    }

}
