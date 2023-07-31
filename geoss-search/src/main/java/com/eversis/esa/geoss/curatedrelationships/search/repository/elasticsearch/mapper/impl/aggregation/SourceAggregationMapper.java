package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.mapper.impl.aggregation;

import com.eversis.esa.geoss.curatedrelationships.search.model.common.Facet;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.impl.CompoundTermFacet;
import com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.constants.geosscr.GeossCrEntryElasticsearchQueryConstansts;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class SourceAggregationMapper {

    List<Facet> collectSourceFacetsFromStringFacets(List<Facet> source) {
        return source.stream()
                .map(i -> getSourceFacet(i.getTermId(), i.getCount()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private Optional<Facet> getSourceFacet(String value, long count) {
        if (value == null) {
            return Optional.empty();
        }
        String[] splited = value.split(Pattern.quote(GeossCrEntryElasticsearchQueryConstansts.MUTLI_FIELD_AGGREGATIONS_SEPARAOTR));
        if (splited == null || splited.length != 2 || ("null".equalsIgnoreCase(splited[0]) && "null".equalsIgnoreCase(splited[1]))) {
            return Optional.empty();
        }
        return Optional.of(new CompoundTermFacet(splited[0], splited[1], count));
    }

}
