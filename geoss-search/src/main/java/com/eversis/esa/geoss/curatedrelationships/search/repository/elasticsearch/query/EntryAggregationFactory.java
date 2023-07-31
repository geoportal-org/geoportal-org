package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.query;

import com.eversis.esa.geoss.curatedrelationships.search.model.Facets;
import com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.constants.geosscr.GeossCrEntryElasticsearchQueryConstansts;
import com.eversis.esa.geoss.curatedrelationships.search.utils.StringUtils;

import org.elasticsearch.script.Script;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;

/**
 * The type Entry aggregation factory.
 */
@Component
public class EntryAggregationFactory {

    /**
     * Creates list of Elasticsearch aggregation's builders. Each key in provided Map is mapped to single aggregation,
     * which results in single bucket. Although by default Elasticsearch does not enable creation of single bucket from
     * multiple fields, it is possible to omit this restriction by using script query or by adding additional field in
     * index mapping.
     *
     * @param facetFields each entry corresponds to single aggregation
     * @return the list
     */
    public List<AggregationBuilder> buildAggregations(Map<Facets, List<String>> facetFields) {
        if (facetFields == null) {
            return Collections.emptyList();
        }

        return facetFields.entrySet().stream()
                .map(stringListEntry -> createAggregation(stringListEntry.getKey().getName(),
                        stringListEntry.getValue()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private Optional<AggregationBuilder> createAggregation(String aggregationName, List<String> fields) {
        if (StringUtils.isBlank(aggregationName) || CollectionUtils.isEmpty(fields)) {
            return Optional.empty();
        }

        if (fields.size() == 1) {
            return Optional.of(AggregationBuilders
                    .terms(aggregationName)
                    .field(fields.get(0)));
        }

        return Optional.of(AggregationBuilders
                .terms(aggregationName)
                .script(createScriptForFields(fields)));
    }

    private Script createScriptForFields(@NotNull List<String> fields) {
        String scriptString = fields.stream()
                .map(field -> "doc['" + field + "'].value")
                .collect(Collectors.joining(
                        " + '" + GeossCrEntryElasticsearchQueryConstansts.MUTLI_FIELD_AGGREGATIONS_SEPARAOTR + "' + "));

        return new Script(scriptString);
    }
}
