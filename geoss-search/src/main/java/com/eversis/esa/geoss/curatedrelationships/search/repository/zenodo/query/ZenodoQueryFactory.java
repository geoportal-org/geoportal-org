package com.eversis.esa.geoss.curatedrelationships.search.repository.zenodo.query;

import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

/**
 * The type Zenodo query factory.
 */
@Component
public class ZenodoQueryFactory {

    /**
     * Build id query string.
     *
     * @param id the id
     * @return the string
     */
    public String buildIdQuery(@NotNull String id) {
        return new ZenodoQueryBuilder()
                .id(id)
                .build();
    }

}
