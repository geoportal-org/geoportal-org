package com.eversis.esa.geoss.curatedrelationships.search.repository.zenodo.query;

import org.springframework.web.util.UriComponentsBuilder;

/**
 * The type Zenodo query builder.
 */
class ZenodoQueryBuilder {

    private UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromPath("/api/records");

    /**
     * Id zenodo query builder.
     *
     * @param id the id
     * @return the zenodo query builder
     */
    ZenodoQueryBuilder id(String id) {
        urlBuilder.pathSegment(id);
        return this;
    }

    /**
     * Build string.
     *
     * @return the string
     */
    String build() {
        return urlBuilder.build().toUriString();
    }

}
