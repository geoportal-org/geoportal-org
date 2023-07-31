package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.query;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Component;

import java.util.Set;
import javax.validation.constraints.NotNull;

@Component
public class ExtensionQueryFactory {

    private static final String EXTENSION_ID_SEPARATOR = "-";

    /**
     * Creates Elasticsearch query, which will match only specific document ids.
     *
     * @param ids list of document ids
     * @param dataSource dataSource of provided ids
     */
    public QueryBuilder buildIdsQuery(@NotNull Set<String> ids, @NotNull String dataSource) {
        String[] formattedIds = ids.stream()
                .map(id -> dataSource + EXTENSION_ID_SEPARATOR + id)
                .toArray(String[]::new);
        return QueryBuilders.idsQuery().addIds(formattedIds);
    }

}
