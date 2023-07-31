package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.constants.geosscr;

import java.util.HashMap;
import java.util.Map;

import static com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.constants.geosscr.GeossCrEntryElasticsearchFields.EXTENSIONS_SUMMARY_FIELD;
import static com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.constants.geosscr.GeossCrEntryElasticsearchFields.EXTENSIONS_SUMMARY_SEARCH_FIELD;
import static com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.constants.geosscr.GeossCrEntryElasticsearchFields.SUMMARY_FIELD;
import static com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.constants.geosscr.GeossCrEntryElasticsearchFields.SUMMARY_SEARCH_FIELD;
import static com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.constants.geosscr.GeossCrEntryElasticsearchFields.TAGS_RAW_FIELD;
import static com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.constants.geosscr.GeossCrEntryElasticsearchFields.TAGS_SEARCH_FIELD;
import static com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.constants.geosscr.GeossCrEntryElasticsearchFields.TITLE_FIELD;
import static com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.constants.geosscr.GeossCrEntryElasticsearchFields.TITLE_RAW_FIELD;
import static com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.constants.geosscr.GeossCrEntryElasticsearchFields.TITLE_SEARCH_FIELD;
import static com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.constants.geosscr.GeossCrEntryElasticsearchFields.TITLE_TRIGRAM_FIELD;
import static com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.constants.geosscr.GeossCrEntryElasticsearchFields.TRANSFER_OPTIONS_NAME_FIELD;
import static com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.constants.geosscr.GeossCrEntryElasticsearchFields.TRANSFER_OPTIONS_TITLE_FIELD;

public class GeossCrEntryElasticsearchFieldWeights {

    public static final Map<String, Float> SEARCH_PHRASE_FIELDS_WEIGHTS = new HashMap<>();

    static {
        SEARCH_PHRASE_FIELDS_WEIGHTS.put(TITLE_FIELD, 3.0f);
        SEARCH_PHRASE_FIELDS_WEIGHTS.put(TITLE_RAW_FIELD, 4.0f);
        SEARCH_PHRASE_FIELDS_WEIGHTS.put(TITLE_SEARCH_FIELD, 4.0f);
        SEARCH_PHRASE_FIELDS_WEIGHTS.put(TITLE_TRIGRAM_FIELD, 1.0f);
        SEARCH_PHRASE_FIELDS_WEIGHTS.put(SUMMARY_FIELD, 1.0f);
        SEARCH_PHRASE_FIELDS_WEIGHTS.put(SUMMARY_SEARCH_FIELD, 1.0f);
        SEARCH_PHRASE_FIELDS_WEIGHTS.put(TAGS_RAW_FIELD, 3.0f);
        SEARCH_PHRASE_FIELDS_WEIGHTS.put(TAGS_SEARCH_FIELD, 2.0f);
        SEARCH_PHRASE_FIELDS_WEIGHTS.put(TRANSFER_OPTIONS_NAME_FIELD, 1.0f);
        SEARCH_PHRASE_FIELDS_WEIGHTS.put(TRANSFER_OPTIONS_TITLE_FIELD, 1.0f);
        SEARCH_PHRASE_FIELDS_WEIGHTS.put(EXTENSIONS_SUMMARY_FIELD, 1.0f);
        SEARCH_PHRASE_FIELDS_WEIGHTS.put(EXTENSIONS_SUMMARY_SEARCH_FIELD, 1.0f);
    }

    private GeossCrEntryElasticsearchFieldWeights() {
        throw new IllegalStateException("Utility class");
    }
}
