import { SearchFilterKeys, SearchParamName } from './searchKeys';

export const buildSavedSearchUrl = (searchParams:  {[key: string]: any}) => {
    const params: {[key: string]: string} = {};

    for (const searchParam in searchParams) {
        const searchParamValue = searchParams[searchParam];
        if (!searchParamValue) continue;

        switch (searchParam) {
            case 'query':
                params[SearchParamName.F_PHRASE] = searchParamValue;
                break;
            case 'currMap':
                params[SearchParamName.M_ACTIVE_LAYER_TILE_ID] = searchParamValue;
                break;
            case 'fullAndOpenDataset':
                params[SearchParamName.F_GEOSS_DATA_CORE] = searchParamValue;
                break;
            case 'dateFrom':
                params[SearchParamName.F_DATE_FROM] = searchParamValue;
                break;
            case 'dateTo':
                params[SearchParamName.F_DATE_TO] = searchParamValue;
                break;
            case 'datePeriod':
                params[SearchParamName.F_DATE_PERIOD] = searchParamValue;
                break;
            case 'aoiOption':
                params[SearchParamName.F_LOCATION_TYPE] = searchParamValue;
                break;
            case 'aoiBoundingBox':
                params[SearchParamName.F_SELECTED_AREA_COORDINATES] = searchParamValue;
                break;
            case 'aoiGeolocation':
                params[SearchParamName.F_GOOGLE_PLACES_INPUT] = searchParamValue;
                break;
            case 'aoiRelation':
                params[SearchParamName.F_BOUNDING_BOX_RELATION] = searchParamValue;
                break;
            case 'queryParams':
                if (Object.keys(searchParams['queryParams']).length) {
                    if (searchParams['queryParams'][SearchFilterKeys.SOURCES]) {
                        params[SearchParamName.F_SOURCES] = searchParams['queryParams'][SearchFilterKeys.SOURCES];
                    }
                    if (searchParams['queryParams'][SearchFilterKeys.VIEWID]) {
                        params[SearchParamName.F_VIEW_ID] = searchParams['queryParams'][SearchFilterKeys.VIEWID];
                    }
                }
                break;
            default:
                break;
        }
    }

    if (!searchParams.targetId) {
        params[SearchParamName.F_DATA_SOURCE] = searchParams.dataSource;
    } else {
        params[SearchParamName.F_PARENT_REFS] = buildParentRefJson(searchParams.targetId, searchParams.dataSource);
    }

    if (searchParams.filterParams && Object.keys(searchParams.filterParams).length) {
        for (const filterParamName in searchParams.filterParams) {
            const filterParamValue = searchParams.filterParams[filterParamName];

            if (!filterParamValue) continue;

            // filter set: faceted-filters
            switch(filterParamName) {
                case SearchFilterKeys.KEYWORD:
                    params[SearchParamName.F_KEYWORD] = filterParamValue;
                    break;
                case SearchFilterKeys.FORMAT:
                    params[SearchParamName.F_FORMAT] = filterParamValue;
                    break;
                case SearchFilterKeys.SOURCE:
                    params[SearchParamName.F_SOURCE] = filterParamValue;
                    break;
                case SearchFilterKeys.PROTOCOL:
                    params[SearchParamName.F_PROTOCOL] = filterParamValue;
                    break;
                case SearchFilterKeys.ORGANISATION:
                    params[SearchParamName.F_ORGANISATION] = filterParamValue;
                    break;
                case SearchFilterKeys.SCORE:
                    params[SearchParamName.F_SCORE] = filterParamValue;
                    break;
                }

            // filter set: granula-filters
            switch(filterParamName) {
                case SearchFilterKeys.PROD_TYPE:
                    params[SearchParamName.F_PRODUCT_TYPE] = filterParamValue;
                    break;
                case SearchFilterKeys.SAR_POL_CH:
                    params[SearchParamName.F_SENSOR_POLARISATION] = filterParamValue;
                    break;
                case SearchFilterKeys.SENSOR_OP_MODE:
                    params[SearchParamName.F_SENSOR_MODE] = filterParamValue;
                    break;
                case SearchFilterKeys.SENSOR_SWATH:
                    params[SearchParamName.F_SENSOR_SWATH] = filterParamValue;
                    break;
                case SearchFilterKeys.REL_ORBIT:
                    params[SearchParamName.F_RELATIVE_ORBIT] = filterParamValue;
                    break;
                case SearchFilterKeys.ROW:
                    params[SearchParamName.F_ROW] = filterParamValue;
                    break;
                case SearchFilterKeys.PATH:
                    params[SearchParamName.F_PATH] = filterParamValue;
                    break;
                case SearchFilterKeys.S3_INSTRUMENT_IDX:
                    params[SearchParamName.F_INSTRUMENT] = filterParamValue;
                    break;
                case SearchFilterKeys.S3_PRODUCT_LEVEL:
                    params[SearchParamName.F_PRODUCT_LEVEL] = filterParamValue;
                    break;
                case SearchFilterKeys.S3_TIMELINESS:
                    params[SearchParamName.F_TIMELINES] = filterParamValue;
                    break;
                case SearchFilterKeys.CLOUDCP:
                    params[SearchParamName.F_CLOUD_COVERAGE] = filterParamValue;
                    break;
                case SearchFilterKeys.DATE_FROM_GRANULA:
                    params[SearchParamName.F_DATE_FROM] = filterParamValue;
                    break;
                case SearchFilterKeys.DATE_TO_GRANULA:
                    params[SearchParamName.F_DATE_TO] = filterParamValue;
                    break;
                }

            // filter set: iris-filters
            switch(filterParamName) {
                case SearchFilterKeys.MAGT:
                    params[SearchParamName.F_MAGNITUDE_TYPE] = filterParamValue;
                    break;
                case SearchFilterKeys.EVT_ORD:
                    params[SearchParamName.F_SORT] = filterParamValue;
                    break;
                case SearchFilterKeys.MIN_MAG:
                    params[SearchParamName.F_MIN_MAGNITUDE] = filterParamValue;
                    break;
                case SearchFilterKeys.MAX_MAG:
                    params[SearchParamName.F_MAX_MAGNITUDE] = filterParamValue;
                    break;
                case SearchFilterKeys.MIN_DEP:
                    params[SearchParamName.F_MIN_DEPTH] = filterParamValue;
                    break;
                case SearchFilterKeys.MAX_DEP:
                    params[SearchParamName.F_MAX_DEPTH] = filterParamValue;
                    break;
                case SearchFilterKeys.DATE_FROM_IRIS:
                    params[SearchParamName.F_DATE_FROM] = filterParamValue;
                    break;
                case SearchFilterKeys.DATE_TO_IRIS:
                    params[SearchParamName.F_DATE_TO] = filterParamValue;
                    break;
                }
        }
    }

    return buildUrl(params);
}

export const buildParentRefJson = (targetId: string, dataSource: string) => {
    const array: Array<object> = [];
    const json: {[key: string]: string} = {};

    json.ID = targetId;
    json.DATA_SOURCE = dataSource;

    array.push(json);

    return JSON.stringify(array)
}

export const buildUrl = (params: {[key: string]: string}) => {
    const baseUrl: string = '/';
    let query: string = '';

    for (const param in params) {
        query += `${param}=${encodeURIComponent(params[param])}&`;
    }

    const url = baseUrl + "?" + query;
    return url;
}
