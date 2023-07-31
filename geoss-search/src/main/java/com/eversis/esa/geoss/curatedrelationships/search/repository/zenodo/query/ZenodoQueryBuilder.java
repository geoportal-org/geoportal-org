package com.eversis.esa.geoss.curatedrelationships.search.repository.zenodo.query;

import org.springframework.web.util.UriComponentsBuilder;

class ZenodoQueryBuilder {

    private UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromPath("/api/records");

    ZenodoQueryBuilder id(String id) {
        urlBuilder.pathSegment(id);
        return this;
    }

    String build() {
        return urlBuilder.build().toUriString();
    }

}
