export enum SearchFilterKeys {
    // datePeriod values
    DATE_PERIOD_LAST_WEEK = "lastWeek",
    DATE_PERIOD_LAST_MONTH = "lastMonth",
    DATE_PERIOD_LAST_YEAR = "lastYear",
    DATE_PERIOD_LAST_10_YEARS = "last10Years",

    // aoiOption values
    AOI_OPTION_CONTINENT_COUNTRY = "continent_country",
    AOI_OPTION_COORDINATES = "coordinates",
    AOI_OPTION_GOLOCATION = "geolocation",

    // dataSource Values
    DATA_SOURCE_DAB = "dab",
    //TODO add support for other data sources, what are the values?
    //new column to table (for saved search, popular search and bookmarks)

    // query params
    SOURCES = "sources",
    VIEWID = "viewid",

    // filter set: faceted-filters
    KEYWORD = "keyword",
    FORMAT = "format",
    SOURCE = "source",
    PROTOCOL = "protocol",
    ORGANISATION = "organisation",
    SCORE = "score",

    // filter set: granula-filters
    PROD_TYPE = "prodType",
    SAR_POL_CH = "sarPolCh",
    SENSOR_OP_MODE = "sensorOpMode",
    SENSOR_SWATH = "sensorSwath",
    REL_ORBIT = "relOrbit",
    ROW = "row",
    PATH = "path",
    S3_INSTRUMENT_IDX = "s3InstrumentIdx",
    S3_PRODUCT_LEVEL = "s3ProductLevel",
    S3_TIMELINESS = "s3Timeliness",
    CLOUDCP = "cloudcp",
    DATE_FROM_GRANULA = "dateFromGranula",
    DATE_TO_GRANULA = "dateToGranula",

    // filter set: iris-filters
    MAGT = "magt",
    EVT_ORD = "evtOrd",
    MIN_MAG = "minMag",
    MAX_MAG = "maxMag",
    MIN_DEP = "minDep",
    MAX_DEP = "maxDep",
    DATE_FROM_IRIS = "dateFromIris",
    DATE_TO_IRIS = "dateToIris",
}

export enum SearchParamName {

    // URL params
    TARGET_ID = "targetId",
    POPULAR_SEARCH_ID = "popularSearchId",
    SAVED_SEARCH_ID = "savedSearchId",

    GEOSS_CR_OPENSEARCH_TARGET_IDS = "targetIds",
    GEOSS_CR_OPENSEARCH_START_INDEX = "si",
    GEOSS_CR_OPENSEARCH_ITEMS_PER_PAGE = "ct",
    GEOSS_CR_OPENSEARCH_DATA_SOURCE = "ds",

    M_ACTIVE_LAYER_TILE_ID = "m:activeLayerTileId",
    M_LAYERS = "m:layers",

    F_PHRASE = "f:phrase",
    F_DATA_SOURCE = "f:dataSource",
    F_PARENT_REFS = "f:parentRefs",
    F_GEOSS_DATA_CORE = "f:geossDataCore",
    F_DATE_FROM = "f:dateFrom",
    F_DATE_TO = "f:dateTo",
    F_DATE_PERIOD = "f:datePeriod",
    F_LOCATION_TYPE = "f:locationType",
    F_SELECTED_AREA_COORDINATES = "f:selectedAreaCoordinates",
    F_GOOGLE_PLACES_INPUT = "f:googlePlacesInput",
    F_BOUNDING_BOX_RELATION = "f:boundingBoxRelation",

    // query params
    F_SOURCES = "f:sources",
    F_VIEW_ID = "f:viewId",

    // filter set: faceted-filters
    F_KEYWORD = "f:keyword",
    F_FORMAT = "f:format",
    F_SOURCE = "f:source",
    F_PROTOCOL = "f:protocol",
    F_ORGANISATION = "f:organisation",
    F_SCORE = "f:score",

    // filter set: granula-filters
    F_PRODUCT_TYPE = "f:productType",
    F_SENSOR_POLARISATION = "f:sensorPolarisation",
    F_SENSOR_MODE = "f:sensorMode",
    F_SENSOR_SWATH = "f:sensorSwath",
    F_RELATIVE_ORBIT = "f:relativeOrbit",
    F_ROW = "f:row",
    F_PATH = "f:path",
    F_INSTRUMENT = "f:instrument",
    F_PRODUCT_LEVEL = "f:productLevel",
    F_TIMELINES = "f:timeliness",
    F_CLOUD_COVERAGE = "f:cloudCoverage",
    // for dates F_DATE_FROM and F_DATE_TO should be overwritten

    // filter set: iris-filters
    F_MAGNITUDE_TYPE = "f:magnitudeType",
    F_SORT = "f:sort",
    F_MIN_MAGNITUDE = "f:minMagnitude",
    F_MAX_MAGNITUDE = "f:maxMagnitude",
    F_MIN_DEPTH = "f:minDepth",
    F_MAX_DEPTH = "f:maxDepth",
    // for dates F_DATE_FROM and F_DATE_TO should be overwritten

    // Nested JSON params for F_PARENT_REFS
    ID = "id",
    DATA_SOURCE = "dataSource",

    // Nested JSON params for M_LAYERS
    TYPE = "type",
    TITLE = "title",
    URL = "url",
    LEGEND = "legend",
}
