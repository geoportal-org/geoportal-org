package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.query;

import com.eversis.esa.geoss.curatedrelationships.search.model.BoundingBoxRelation;
import com.eversis.esa.geoss.curatedrelationships.search.model.DateRange;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.BoundingBox;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.EntryType;
import com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.mapper.impl.GeoShapeMapper;

import org.elasticsearch.ElasticsearchGenerationException;
import org.elasticsearch.common.geo.builders.EnvelopeBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.GeoShapeQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

import static com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.constants.geosscr.GeossCrEntryElasticsearchFields.COVERAGE_FIELD;
import static com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.constants.geosscr.GeossCrEntryElasticsearchFields.DISPLAY_DATASOURCE_FIELD;
import static com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.constants.geosscr.GeossCrEntryElasticsearchFields.KEYWORDS_FIELD;
import static com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.constants.geosscr.GeossCrEntryElasticsearchFields.ORGANIZATION_TITLE_RAW_FIELD;
import static com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.constants.geosscr.GeossCrEntryElasticsearchFields.PARENT_ID_FIELD;
import static com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.constants.geosscr.GeossCrEntryElasticsearchFields.PUBLISH_DATE_FIELD;
import static com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.constants.geosscr.GeossCrEntryElasticsearchFields.RESOURCE_ENTRY_TYPE_FIELD;
import static com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.constants.geosscr.GeossCrEntryElasticsearchFields.SOURCE_ID_FIELD;
import static com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.constants.geosscr.GeossCrEntryElasticsearchFields.TRANSFER_OPTIONS_PROTOCOL_FIELD;

@Component
public class EntryFilterQueryFactory {

    QueryBuilder createDateQuery(DateRange dateRange) {
        BoolQueryBuilder mainDateQueryBuilder = QueryBuilders.boolQuery();
        RangeQueryBuilder dateRangeQueryBuilder = QueryBuilders.rangeQuery(PUBLISH_DATE_FIELD);
        dateRange.getStartDateTime().ifPresent(dateRangeQueryBuilder::gte);
        dateRange.getEndDateTime().ifPresent(dateRangeQueryBuilder::lte);
        mainDateQueryBuilder.should(dateRangeQueryBuilder);

        BoolQueryBuilder missingFieldQueryBuilder = QueryBuilders.boolQuery();
        missingFieldQueryBuilder.mustNot(QueryBuilders.existsQuery(PUBLISH_DATE_FIELD));
        mainDateQueryBuilder.should(missingFieldQueryBuilder);
        return mainDateQueryBuilder;
    }

    TermsQueryBuilder createResourceEntryTypeQuery(Set<EntryType> entryTypes) {
        return QueryBuilders.termsQuery(RESOURCE_ENTRY_TYPE_FIELD,
                entryTypes.stream().map(EntryType::getName).collect(Collectors.toList()));
    }

    GeoShapeQueryBuilder createGeoShapeQuery(BoundingBoxRelation boxRelation, BoundingBox boundingBox) {
        GeoShapeQueryBuilder geoShapeQueryBuilder = null;
        BoundingBox shrunkenBoundingBox = GeoShapeMapper.resizeBoundingBox(boundingBox, -0.0001, -0.0001, 0.0001, 0.0001);
        EnvelopeBuilder envelopeBuilder = GeoShapeMapper.mapEnvelopeFromBoundingBox(shrunkenBoundingBox);
        try {
            switch (boxRelation) {
                case CONTAINS:
                    geoShapeQueryBuilder = QueryBuilders.geoWithinQuery(COVERAGE_FIELD, envelopeBuilder);
                    break;
                case DISJOINT:
                    geoShapeQueryBuilder = QueryBuilders.geoDisjointQuery(COVERAGE_FIELD, envelopeBuilder);
                    break;
                case OVERLAPS:
                default:
                    geoShapeQueryBuilder = QueryBuilders.geoIntersectionQuery(COVERAGE_FIELD, envelopeBuilder);
                    break;
            }
        } catch (IOException e) {
            throw new ElasticsearchGenerationException("Could not create GeoShape query", e);
        }
        return geoShapeQueryBuilder;
    }

    TermsQueryBuilder createParentsQuery(Set<String> parents) {
        return QueryBuilders.termsQuery(PARENT_ID_FIELD, parents);
    }

    TermsQueryBuilder createSourcesQuery(Set<String> sources) {
        return QueryBuilders.termsQuery(SOURCE_ID_FIELD, sources);
    }

    TermsQueryBuilder createOrganizationQuery(String organizationName) {
        return QueryBuilders.termsQuery(ORGANIZATION_TITLE_RAW_FIELD, organizationName);
    }

    TermsQueryBuilder createProtocolQuery(String protocol) {
        return QueryBuilders.termsQuery(TRANSFER_OPTIONS_PROTOCOL_FIELD, protocol);
    }

    TermsQueryBuilder createKeywordQuery(String keyword) {
        return QueryBuilders.termsQuery(KEYWORDS_FIELD, keyword);
    }

    TermsQueryBuilder createDataSourceQuery(String keyword) {
        return QueryBuilders.termsQuery(DISPLAY_DATASOURCE_FIELD, keyword);
    }
}
