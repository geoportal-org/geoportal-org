package com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.articles.reader.dto;

import lombok.Data;

/**
 * The type Continue element.
 */
@Data
public class ContinueElement {

    private String cmcontinue;

    /**
     * Has next page boolean.
     *
     * @return the boolean
     */
    public boolean hasNextPage() {
        return cmcontinue != null;
    }

}
