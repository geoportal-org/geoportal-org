package com.eversis.esa.geoss.proxy.service.impl;

import com.eversis.esa.geoss.proxy.domain.MostPopular;
import com.eversis.esa.geoss.proxy.domain.MostPopularAreasModel;
import com.eversis.esa.geoss.proxy.domain.MostPopularCatalogsModel;
import com.eversis.esa.geoss.proxy.domain.MostPopularKeywordsModel;
import com.eversis.esa.geoss.proxy.domain.MostPopularOrganisationsModel;
import com.eversis.esa.geoss.proxy.domain.MostPopularResourcesModel;
import com.eversis.esa.geoss.proxy.domain.NumberOfSearchesModel;
import com.eversis.esa.geoss.proxy.domain.Search;
import com.eversis.esa.geoss.proxy.service.StatisticsService;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Buckets;
import co.elastic.clients.elasticsearch._types.aggregations.DateHistogramBucket;
import co.elastic.clients.elasticsearch._types.aggregations.FieldDateMath;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsAggregate;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.JsonData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The type Statistics service.
 */
@Slf4j
@Service
public class StatisticsServiceImpl implements StatisticsService {

    private static final String INDEX_NAME = "geoss_index";
    private static final String AGGREGATION_DATE_HISTOGRAM_NAME = "aggr_date_histogram";
    private static final String AGGREGATION_TERMS_NAME = "aggr_terms";
    private static final String SESSION_DATE_FIELD_NAME = "session_date";
    private static final String SESSION_TIMESTAMP_FIELD_NAME = "session_timestamp";
    private static final String UI_RESOURCE_NAME_FIELD_NAME = "ui_resource_name";
    private static final String UI_ORGANISATION_FIELD_NAME = "ui_organisation";
    private static final String DS_ST_NAME_FIELD_NAME = "ds_st";
    private static final String DS_BBOX_NAME_FIELD_NAME = "ds_bbox";
    private static final String DS_SOURCES_GROUP_KEY_FIELD_NAME = "ds_sources_group.key";
    private static final String DS_SOURCES_GROUP_VALUE_FIELD_NAME = "ds_sources_group.value";
    private static final int MIN_DOC_COUNT_VALUE = 0;
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm.SSS";
    /**
     * The Elasticsearch client.
     */
    ElasticsearchClient elasticsearchClient;

    /**
     * Instantiates a new Statistics service.
     *
     * @param elasticsearchClient the elasticsearch client
     */
    public StatisticsServiceImpl(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    @Override
    public List<Search> getNumberOfSearches(NumberOfSearchesModel numberOfSearchesModel) {
        return extractNumberOfSearches(numberOfSearchesModel);
    }

    @Override
    public List<MostPopular> getMostPopularResources(MostPopularResourcesModel mostPopularResourcesModel) {
        return extractMostPopularResources(mostPopularResourcesModel);
    }

    @Override
    public List<MostPopular> getMostPopularKeywords(MostPopularKeywordsModel mostPopularKeywordsModel) {
        return extractMostPopularKeywords(mostPopularKeywordsModel);
    }

    @Override
    public List<MostPopular> getMostPopularCatalogs(MostPopularCatalogsModel mostPopularCatalogsModel) {
        return extractMostPopularCatalogs(mostPopularCatalogsModel);
    }

    @Override
    public List<MostPopular> getMostPopularOrganizations(MostPopularOrganisationsModel mostPopularOrganisationsModel) {
        return extractMostPopularOrganisations(mostPopularOrganisationsModel);
    }

    @Override
    public List<MostPopular> getMostPopularAreas(MostPopularAreasModel mostPopularAreasModel) {
        return extractMostPopularAreas(mostPopularAreasModel);
    }

    private List<Search> extractNumberOfSearches(NumberOfSearchesModel numberOfSearchesModel) {
        List<Search> searches = new ArrayList<>();
        final String finalInterval = getFinalInterval(numberOfSearchesModel.getInterval());

        try {
            Query termQuery = TermQuery.of(t -> t
                    .field(DS_SOURCES_GROUP_KEY_FIELD_NAME)
                    .value(numberOfSearchesModel.getDsSourcesGroupKey())
            )._toQuery();

            Query rangeQuery = RangeQuery.of(t -> t
                    .field(SESSION_TIMESTAMP_FIELD_NAME)
                    .gte(JsonData.of(Double.valueOf(numberOfSearchesModel.getPeriod().getMin()).longValue()))
                    .lte(JsonData.of(Double.valueOf(numberOfSearchesModel.getPeriod().getMax()).longValue()))
            )._toQuery();

            Query complexQuery;
            if (!numberOfSearchesModel.getDsSourcesGroupKey().isEmpty()) {
                complexQuery = BoolQuery.of(b -> b.must(termQuery).must(rangeQuery))._toQuery();
            } else {
                complexQuery = BoolQuery.of(b -> b.must(rangeQuery))._toQuery();
            }

            Query filterQuery = BoolQuery.of(b -> b.filter(complexQuery))._toQuery();

            SearchResponse<Void> response = elasticsearchClient.search(b -> b
                            .index(INDEX_NAME)
                            .size(numberOfSearchesModel.getResults())
                            .query(filterQuery)
                            .aggregations(AGGREGATION_DATE_HISTOGRAM_NAME, a -> a
                                    .dateHistogram(h -> h
                                            .field(SESSION_DATE_FIELD_NAME)
                                            .fixedInterval(builder -> builder.time(finalInterval))
                                            .format(DATE_FORMAT)
                                            .minDocCount(MIN_DOC_COUNT_VALUE)
                                            .extendedBounds(fn -> fn
                                                    .min(FieldDateMath.of(v -> v.value(
                                                            (double) numberOfSearchesModel.getPeriod().getMin())))
                                                    .max(FieldDateMath.of(v -> v.value(
                                                            (double) numberOfSearchesModel.getPeriod().getMax())))
                                            )
                                    )
                            ),
                    Void.class
            );

            List<DateHistogramBucket> buckets = response.aggregations()
                    .get(AGGREGATION_DATE_HISTOGRAM_NAME)
                    .dateHistogram()
                    .buckets().array();

            for (DateHistogramBucket bucket : buckets) {
                searches.add(new Search(bucket.key(), bucket.keyAsString(), bucket.docCount()));
            }

        } catch (IOException e) {
            throw new RuntimeException("Could not get number of seraches. Error: " + e.getMessage());
        }
        return searches;
    }

    private List<MostPopular> extractMostPopularResources(MostPopularResourcesModel mostPopularResourcesModel) {
        List<MostPopular> mostPopular = new ArrayList<>();

        Query rangeQuery = RangeQuery.of(t -> t
                .field(SESSION_TIMESTAMP_FIELD_NAME)
                .gte(JsonData.of(Double.valueOf(mostPopularResourcesModel.getPeriod().getMin()).longValue()))
                .lte(JsonData.of(Double.valueOf(mostPopularResourcesModel.getPeriod().getMax()).longValue()))
        )._toQuery();

        SearchResponse<Void> response;
        try {
            response = elasticsearchClient.search(b -> b
                            .index(INDEX_NAME)
                            .size(mostPopularResourcesModel.getResults())
                            .query(rangeQuery)
                            .aggregations(AGGREGATION_TERMS_NAME, a -> a
                                    .terms(ta -> ta.field(UI_RESOURCE_NAME_FIELD_NAME)
                                            .size(mostPopularResourcesModel.getSize()))
                            ),
                    Void.class
            );
            getMostPopularFromBuckets(mostPopular, response);
        } catch (IOException e) {
            throw new RuntimeException("Could not get most popular resources. Error: " + e.getMessage());
        }
        return mostPopular;
    }

    private List<MostPopular> extractMostPopularKeywords(MostPopularKeywordsModel mostPopularKeywordsModel) {
        List<MostPopular> mostPopular = new ArrayList<>();

        Query termQuery = TermQuery.of(t -> t
                .field(DS_SOURCES_GROUP_KEY_FIELD_NAME)
                .value(mostPopularKeywordsModel.getDsSourcesGroupKey())
        )._toQuery();

        Query rangeQuery = RangeQuery.of(t -> t
                .field(SESSION_TIMESTAMP_FIELD_NAME)
                .gte(JsonData.of(Double.valueOf(mostPopularKeywordsModel.getPeriod().getMin()).longValue()))
                .lte(JsonData.of(Double.valueOf(mostPopularKeywordsModel.getPeriod().getMax()).longValue()))
        )._toQuery();

        Query complexQuery;
        if (!mostPopularKeywordsModel.getDsSourcesGroupKey().isEmpty()) {
            complexQuery = BoolQuery.of(b -> b.must(termQuery).must(rangeQuery))._toQuery();
        } else {
            complexQuery = BoolQuery.of(b -> b.must(rangeQuery))._toQuery();
        }

        Query filterQuery = BoolQuery.of(b -> b.filter(complexQuery))._toQuery();

        try {
            SearchResponse<Void> response = elasticsearchClient.search(b -> b
                            .index(INDEX_NAME)
                            .size(mostPopularKeywordsModel.getResults())
                            .query(filterQuery)
                            .aggregations(AGGREGATION_TERMS_NAME, a -> a
                                    .terms(t -> t.field(DS_ST_NAME_FIELD_NAME)
                                            .size(1000))
                            ),
                    Void.class
            );
            getMostPopularFromBuckets(mostPopular, response);
        } catch (IOException e) {
            throw new RuntimeException("Could not get most popular keywords. Error: " + e.getMessage());
        }
        return mostPopular;
    }

    private List<MostPopular> extractMostPopularCatalogs(MostPopularCatalogsModel mostPopularCatalogsModel) {
        List<MostPopular> mostPopular = new ArrayList<>();

        Query rangeQuery = RangeQuery.of(t -> t
                .field(SESSION_TIMESTAMP_FIELD_NAME)
                .gte(JsonData.of(Double.valueOf(mostPopularCatalogsModel.getPeriod().getMin()).longValue()))
                .lte(JsonData.of(Double.valueOf(mostPopularCatalogsModel.getPeriod().getMax()).longValue()))
        )._toQuery();

        try {
            SearchResponse<Void> response = elasticsearchClient.search(b -> b
                            .index(INDEX_NAME)
                            .size(mostPopularCatalogsModel.getResults())
                            .query(rangeQuery)
                            .aggregations(AGGREGATION_TERMS_NAME, a -> a
                                    .terms(t -> t.field(DS_SOURCES_GROUP_VALUE_FIELD_NAME)
                                            .size(1000))
                            ),
                    Void.class
            );
            getMostPopularFromBuckets(mostPopular, response);
        } catch (IOException e) {
            throw new RuntimeException("Could not get most popular catalogs. Error: " + e.getMessage());
        }
        return mostPopular;
    }

    private List<MostPopular> extractMostPopularOrganisations(
            MostPopularOrganisationsModel mostPopularOrganisationsModel) {
        List<MostPopular> mostPopular = new ArrayList<>();

        Query rangeQuery = RangeQuery.of(t -> t
                .field(SESSION_TIMESTAMP_FIELD_NAME)
                .gte(JsonData.of(Double.valueOf(mostPopularOrganisationsModel.getPeriod().getMin()).longValue()))
                .lte(JsonData.of(Double.valueOf(mostPopularOrganisationsModel.getPeriod().getMax()).longValue()))
        )._toQuery();

        try {
            SearchResponse<Void> response = elasticsearchClient.search(b -> b
                            .index(INDEX_NAME)
                            .size(mostPopularOrganisationsModel.getResults())
                            .query(rangeQuery)
                            .aggregations(AGGREGATION_TERMS_NAME, a -> a
                                    .terms(t -> t.field(UI_ORGANISATION_FIELD_NAME)
                                            .size(mostPopularOrganisationsModel.getSize()))
                            ),
                    Void.class
            );
            getMostPopularFromBuckets(mostPopular, response);
        } catch (IOException e) {
            throw new RuntimeException("Could not get most popular organisations. Error: " + e.getMessage());
        }
        return mostPopular;
    }

    private List<MostPopular> extractMostPopularAreas(MostPopularAreasModel mostPopularAreasModel) {
        List<MostPopular> mostPopular = new ArrayList<>();

        Query termQuery = TermQuery.of(t -> t
                .field(DS_SOURCES_GROUP_KEY_FIELD_NAME)
                .value(mostPopularAreasModel.getDsSourcesGroupKey())
        )._toQuery();

        Query rangeQuery = RangeQuery.of(t -> t
                .field(SESSION_TIMESTAMP_FIELD_NAME)
                .gte(JsonData.of(Double.valueOf(mostPopularAreasModel.getPeriod().getMin()).longValue()))
                .lte(JsonData.of(Double.valueOf(mostPopularAreasModel.getPeriod().getMax()).longValue()))
        )._toQuery();

        Query complexQuery;
        if (!mostPopularAreasModel.getDsSourcesGroupKey().isEmpty()) {
            complexQuery = BoolQuery.of(b -> b.must(termQuery).must(rangeQuery))._toQuery();
        } else {
            complexQuery = BoolQuery.of(b -> b.must(rangeQuery))._toQuery();
        }

        Query filterQuery = BoolQuery.of(b -> b.filter(complexQuery))._toQuery();

        try {
            SearchResponse<Void> response = elasticsearchClient.search(b -> b
                            .index(INDEX_NAME)
                            .size(mostPopularAreasModel.getResults())
                            .query(filterQuery)
                            .aggregations(AGGREGATION_TERMS_NAME, a -> a
                                    .terms(t -> t.field(DS_BBOX_NAME_FIELD_NAME)
                                            .size(1000))
                            ),
                    Void.class
            );
            getMostPopularFromBuckets(mostPopular, response);
        } catch (IOException e) {
            throw new RuntimeException("Could not get most popular areas. Error: " + e.getMessage());
        }
        return mostPopular;
    }

    private void getMostPopularFromBuckets(List<MostPopular> mostPopular, SearchResponse<Void> response) {
        Map<String, Aggregate> aggregations = response.aggregations();
        for (Map.Entry<String, Aggregate> entry : aggregations.entrySet()) {
            Aggregate aggregate = entry.getValue();
            StringTermsAggregate sterms = aggregate.sterms();
            Buckets<StringTermsBucket> sbuckets = sterms.buckets();
            List<StringTermsBucket> bucArr = sbuckets.array();
            for (StringTermsBucket bucObj : bucArr) {
                mostPopular.add(new MostPopular(bucObj.key().stringValue(), bucObj.docCount()));
            }
        }
    }

    private String getFinalInterval(String interval) {
        String finalInterval = interval;
        if (interval.contains("w")) {
            String numberString = interval.substring(0, interval.indexOf("w"));
            int number = Integer.parseInt(numberString);
            int result = number * 7;
            finalInterval = result + "d";
        } else if (interval.contains("M")) {
            String numberString = interval.substring(0, interval.indexOf("M"));
            int number = Integer.parseInt(numberString);
            int result = number * 30;
            finalInterval = result + "d";
        }
        return finalInterval;
    }

}
