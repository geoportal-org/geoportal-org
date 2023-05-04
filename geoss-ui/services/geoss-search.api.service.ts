import axios from 'axios'
import { Source, View } from '@/interfaces/GeneralFilters'
import SearchEngineService from '@/services/search-engine.service'
import SpinnerService from '@/services/spinner.service'
import UtilsService from '@/services/utils.service'
import { BaseUrl, Liferay, AppVueObj } from '~/data/global'
import LogService from '@/services/log.service'
import { GeneralFiltersGetters } from '~/store/generalFilters/general-filters-getters'
import { GeneralFiltersActions } from '~/store/generalFilters/general-filters-actions'
import { MapCoordinate } from '@/interfaces/MapCoordinate'
import { Timers } from '~/data/timers'
import PopupCloseService from '@/services/popup-close.service'
import { PopupActions } from '@/store/popup/popup-actions'
import { parseXMLToJSON, makeRequest } from './general.api.service'
import { GeneralGetters } from '@/store/general/general-getters'
import { SearchGetters } from '@/store/search/search-getters'
import { SearchActions } from '@/store/search/search-actions'
import {
    DataSource,
    DataSources,
    DataPromiseId,
    DataSourcesMask,
    DataSourceGroup,
    DataOrigin,
} from '@/interfaces/DataSources'
import DabRequestTooLong from '@/components/Search/DabRequestTooLong.vue'
import ErrorPopup from '@/components/ErrorPopup.vue'
import MapCoordinatesUtils from '@/services/map/coordinates-utils'
import { DownloadFile } from '@/interfaces/DownloadFile'
import TutorialTagsService from './tutorial-tags.service'
import webSettingsAPI from '@/api/webSettings'
import { SearchEngineGetters } from '~/store/searchEngine/search-engine-getters'

interface Window {
    URL?: any
    webkitURL?: any
}

declare var window: Window
declare const google: {
    maps: {
        places: {
            PlacesService: new (arg0: HTMLDivElement) => any
            PlacesServiceStatus: { OK: any }
        }
    }
}

const cache = {
    sources: null as Source[] | null,
    views: null as View[] | null,
    popularSearch: null,
    params: {
        dab: '',
        data: '',
        amerigeoss: '',
        nextgeoss: '',
        information: '',
        zenodo: '',
        wikipedia: '',
        services: '',
    },
}

const emptyOutputDataObject = {
    dabResults: null,
    dataResults: null,
    amerigeossResults: null,
    nextgeossResults: null,
    informationResults: null,
    zenodoResults: null,
    wikipediaResults: null,
    servicesResults: null,
}

const vlabAuthorizationHeaders = {
    headers: {
        'x-vlab-apikey': '7qm9uezuj27SjqE7FTAEj3iNeKbmpRaHasg7zVs3',
        accept: 'application/json',
    },
}

export type CancelCurrentrequest =
    | 'dab'
    | 'data'
    | 'amerigeoss'
    | 'nextgeoss'
    | 'information'
    | 'zenodo'
    | 'wikipedia'
    | 'services'

export const cancellableRequests = {
    dab: 'dab',
    data: 'data',
    amerigeoss: 'amerigeoss',
    nextgeoss: 'nextgeoss',
    information: 'information',
    zenodo: 'zenodo',
    wikipedia: 'wikipedia',
    services: 'services',
}

const sendLiferayRequest = (
    url: string,
    data: {
        groupId: any
        start?: number
        end?: number
        name?: string
        targetId?: any
        currMap?: string
        dataSource?: string
        score?: number
        comment?: string
        targetIds?: string
        popularSearchId?: number
        query?: string
        fullAndOpenDataset?: boolean
        dateFrom?: string
        dateTo?: string
        datePeriod?: string
        aoiOption?: string
        aoiBoundingBox?: string
        aoiGeolocation?: string
        aoiW3W?: string
        aoiRelation?: string
        cloudCoverageFrom?: number
        cloudCoverageTo?: number
        queryParams?: any
        folder?: string
        filterParams?: any
        filterSet?: string
        runId?: string
        userId?: any
        path?: string
        workflowId?: any
    },
    noSpinner?: boolean
) => {
    const formData = new FormData()
    formData.append('cmd', JSON.stringify({ [url]: data }))
    if (!UtilsService.isWidget()) {
        formData.append('p_auth', Liferay.authToken)
    }
    return makeRequest(
        'post',
        `${BaseUrl()}/api/jsonws/invoke`,
        formData,
        noSpinner
    )
}

const GeossSearchApiService = {
    getSourcesOptions(catalogsUrl: string): Promise<Source[]> {
        return webSettingsAPI.getCatalogs(catalogsUrl)

        // if (cache.sources) {
        //     return Promise.resolve(cache.sources)
        // }
        // return makeRequest(
        //     'get',
        //     SearchEngineService.getCatalogsAndViewsUrl(),
        //     null,
        //     true
        // ).then(
        //     (data: {
        //         catalogs: Source[] | null
        //         views: View[] | null
        //         PopularSearch: null
        //     }) => {
        //         cache.sources = data.catalogs
        //         cache.views = data.views
        //         cache.popularSearch = data.PopularSearch
        //         console.log(cache.sources)
        //         return cache.sources
        //     }
        // )
    },

    getViewsOptions(): Promise<Source[]> {
        return webSettingsAPI.getViews()

        // if (cache.views) {
        //     return Promise.resolve(cache.views)
        // }
        // return makeRequest(
        //     'get',
        //     SearchEngineService.getCatalogsAndViewsUrl(),
        //     null,
        //     true
        // ).then(
        //     (data: {
        //         catalogs: Source[] | null
        //         views: View[] | null
        //         PopularSearch: null
        //     }) => {
        //         cache.sources = data.catalogs
        //         cache.views = data.views
        //         cache.popularSearch = data.PopularSearch
        //         return cache.views
        //     }
        // )
    },

    getWhat3WordsOptions(phrase: string) {
        const url = `https://api.what3words.com/v2/autosuggest?addr=${phrase}&key=7ZYEO18T&lang=en&format=json&display=full`
        return makeRequest('get', url, null, true).then(
            (data: { suggestions: any[] }) => {
                if (data.suggestions) {
                    return data.suggestions.map(
                        (item: { words: any; geometry: any }) => ({
                            words: item.words,
                            geometry: item.geometry,
                        })
                    )
                }
                return []
            }
        )
    },

    getDataSourcesParams(params: { [x: string]: any }) {
        const requestsParams = {
            dab: '',
            data: 'ds=geoss_cr&geoss_cr_type=data_resource',
            amerigeoss: 'ds=amerigeoss_ckan',
            nextgeoss: '',
            information: 'ds=geoss_cr&geoss_cr_type=information_resource',
            services: 'ds=geoss_cr&geoss_cr_type=service_resource',
            zenodo: `sort=bestmatch&size=12&q=${
                AppVueObj.app.$store.getters[GeneralFiltersGetters.state].phrase
            }`,
            wikipedia: 'ds=wikipedia',
        }

        for (const key of Object.keys(params)) {
            if (requestsParams.dab) {
                requestsParams.dab += '&'
            }
            requestsParams.data += '&'
            requestsParams.amerigeoss += '&'
            requestsParams.nextgeoss += '&'
            requestsParams.services += '&'
            requestsParams.information += '&'
            requestsParams.wikipedia += '&'

            if (key === 'si') {
                requestsParams.dab += `${key}=${
                    AppVueObj.app.$store.getters[SearchGetters.startIndexes].dab
                }`
                requestsParams.data += `${key}=${
                    AppVueObj.app.$store.getters[SearchGetters.startIndexes]
                        .data
                }`
                requestsParams.amerigeoss += `${key}=${
                    AppVueObj.app.$store.getters[SearchGetters.startIndexes]
                        .amerigeoss
                }`
                requestsParams.nextgeoss += `${key}=${
                    AppVueObj.app.$store.getters[SearchGetters.startIndexes]
                        .nextgeoss
                }`
                requestsParams.services += `${key}=${
                    AppVueObj.app.$store.getters[SearchGetters.startIndexes]
                        .services
                }`
                requestsParams.information += `${key}=${
                    AppVueObj.app.$store.getters[SearchGetters.startIndexes]
                        .information
                }`
                requestsParams.zenodo += `&page=${
                    (AppVueObj.app.$store.getters[SearchGetters.startIndexes]
                        .zenodo -
                        1) /
                        12 +
                    1
                }`
                requestsParams.wikipedia += `${key}=${
                    AppVueObj.app.$store.getters[SearchGetters.startIndexes]
                        .wikipedia
                }`
            } else if (key === 'parents') {
                const parents =
                    AppVueObj.app.$store.getters[SearchGetters.parentIds] !== ''
                        ? AppVueObj.app.$store.getters[SearchGetters.parentIds]
                        : params[key]
                requestsParams.dab += `${key}=${parents}`
                requestsParams.data += `${key}=${parents}`
                requestsParams.amerigeoss += `${key}=${parents}`
                requestsParams.information += `${key}=${parents}`
                requestsParams.services += `${key}=${parents}`
                requestsParams.zenodo += `&keywords=${parents}` // lets Zenodo return "no_results" durign drill step, TODO: ommit this request while drill down
            } else if (key === 'cloudcp') {
                requestsParams.dab += `${key}=${encodeURIComponent(
                    params[key]
                )}`
                requestsParams.data += `${key}=${encodeURIComponent(
                    params[key]
                )}`
                requestsParams.amerigeoss += `${key}=${encodeURIComponent(
                    params[key]
                )}`
                requestsParams.information += `${key}=${encodeURIComponent(
                    params[key]
                )}`
                requestsParams.services += `${key}=${encodeURIComponent(
                    params[key]
                )}`
                requestsParams.zenodo += `${key}=${encodeURIComponent(
                    params[key]
                )}`
            } else if (
                [
                    'kwd',
                    'frmt',
                    'sources',
                    'prot',
                    'organisationName',
                    'sscScore',
                ].includes(key)
            ) {
                const datasource = AppVueObj.app.$store.getters[
                    SearchGetters.dataSource
                ] as DataSource
                requestsParams[datasource] += `${key}=${params[key]}`
            } else {
                requestsParams.dab += `${key}=${params[key]}`
                requestsParams.data += `${key}=${params[key]}`
                requestsParams.amerigeoss += `${key}=${params[key]}`
                requestsParams.nextgeoss += `${key}=${params[key]}`
                requestsParams.information += `${key}=${params[key]}`
                requestsParams.services += `${key}=${params[key]}`
                requestsParams.wikipedia += `${key}=${params[key]}`
            }
        }

        for (const requestsParam of Object.keys(
            requestsParams
        ) as Array<DataSource>) {
            requestsParams[requestsParam] = requestsParams[
                requestsParam as DataSource
            ].replace(/&+$/, '')
        }

        requestsParams.nextgeoss =
            GeossSearchApiService.mapRequestParamsForNextgeoss(
                requestsParams.nextgeoss
            )
        return requestsParams
    },

    mapRequestParamsForNextgeoss(params: string) {
        return params
            .replace('st=', 'q=')
            .replace('ct=', 'rows=')
            .replace('si=', 'start_index=')
            .replace('ts=', 'timerange_start=')
            .replace('te=', 'timerange_end=')
            .replace(/&tf=.*?&/, '&')
            .replace(/&rel=.*?&/, '&')
            .replace(/&viewid=.*?&/, '&')
            .replace(/&searchFields=.*?&/, '&')
    },

    isIdenticalToCacheParams(params: any) {
        const sourceParams = GeossSearchApiService.getDataSourcesParams(params)
        const phraseRegex = /&?reqID=[a-zA-Z1-9]*/
        for (const dataSource of Object.keys(
            sourceParams
        ) as Array<DataSource>) {
            let match = phraseRegex.exec(sourceParams[dataSource])
            if (match) {
                sourceParams[dataSource] =
                    sourceParams[dataSource].substr(0, match.index) +
                    sourceParams[dataSource].substr(
                        match.index + match[0].length
                    )
            }

            match = phraseRegex.exec(cache.params[dataSource])
            if (match) {
                cache.params[dataSource] =
                    cache.params[dataSource].substr(0, match.index) +
                    cache.params[dataSource].substr(
                        match.index + match[0].length
                    )
            }
        }
        return JSON.stringify(sourceParams) === JSON.stringify(cache.params)
    },

    clearCacheParams() {
        for (const dataSource of Object.keys(
            cache.params
        ) as Array<DataSource>) {
            cache.params[dataSource] = ''
        }
    },

    isJsonString(element: string) {
        try {
            JSON.parse(element)
        } catch (e) {
            return false
        }
        return true
    },

    getResults(
        requestParams: any,
        workflow: any,
        theSameTab: boolean | undefined | null,
        targetSource: DataSource,
        theSameSource: boolean | undefined | null
    ): Promise<
        | {
              dabResults: any
              dataResults: any
              amerigeossResults: any
              nextgeossResults: any
              informationResults: any
              servicesResults: any
              zenodoResults: any
              wikipediaResults: any
          }
        | string
    > {
        /*
         * 1. targetId - gets single entry (or entries separated by coma) from specific dataSource, used in share link
         * 2. targetSource - used in "Switch to" buttons, drill-downs, fires single request to specific dataSource, works with:
         * 	a) fetchOtherSources - asynchronously asks for sources from other tabs
         * 	b) theSameSource - used in drill-down, sets other sources results to null
         * 	c) freezeSources - array of freezed sources, returns results already stored without sending requests
         * 3. hiddenSources - array of disabled dataSources
         * 4. theSameTab - used in pagination, single request to current dataSource, leaves other results untouched
         */
        const otherSourcesLoading =
            AppVueObj.app.$store.getters[SearchGetters.otherSourcesLoading]
        const targetId =
            UtilsService.getUrlParam('targetId') ||
            AppVueObj.app.$store.getters[SearchGetters.targetIds]

        if (targetId && targetId !== '') {
            if (
                typeof targetId === 'number' ||
                !GeossSearchApiService.isJsonString(targetId)
            ) {
                const dataSource: Array<DataSource> =
                    UtilsService.getUrlParam('f:dataSource').split(',') ||
                    AppVueObj.app.$store.getters[
                        SearchGetters.dataSource
                    ].split(',')
                const outputDataObject = JSON.parse(
                    JSON.stringify(emptyOutputDataObject)
                )
                return GeossSearchApiService.getTargetById(
                    targetId + '',
                    dataSource[0]
                ).then((data) => {
                    if (DataOrigin[dataSource[0]] === 'geoss_cr') {
                        for (const entry of data.entry) {
                            const hub = GeossSearchApiService.getEntryHub(entry)
                            if (!outputDataObject[`${hub}Results`]) {
                                outputDataObject[`${hub}Results`] = {
                                    entry: [],
                                    'opensearch:itemsPerPage': 10,
                                    'opensearch:startIndex': 1,
                                    'opensearch:totalResults': 0,
                                }
                            }
                            outputDataObject[`${hub}Results`].entry.push(entry)
                            outputDataObject[`${hub}Results`][
                                'opensearch:totalResults'
                            ]++
                        }
                    } else {
                        outputDataObject[`${dataSource[0]}Results`] = data
                    }
                    AppVueObj.app.$store.dispatch(
                        SearchActions.setTargetIds,
                        ''
                    )
                    return outputDataObject
                })
            } else {
                /*
                 * multi dataSource target id's case, when targetId is presented as stringified object
                 * used in user contributed "switch to" indicator
                 * (may be used in "show on map - all" in bookmarks and geo-likes in the future)
                 */
                const targetIdObject = JSON.parse(targetId)
                const outputDataObject = JSON.parse(
                    JSON.stringify(emptyOutputDataObject)
                )
                const promises = []
                for (const source in targetIdObject) {
                    if (targetIdObject[source]) {
                        promises.push(
                            GeossSearchApiService.getTargetById(
                                targetIdObject[source].join(),
                                source as DataSource
                            ).then((data) => {
                                if (!outputDataObject[`${source}Results`]) {
                                    outputDataObject[`${source}Results`] = {}
                                }
                                outputDataObject[`${source}Results`] = data
                            })
                        )
                    }
                }
                return Promise.all(promises).then(() => {
                    for (const source in DataOrigin) {
                        if (outputDataObject[`${source}Results`]) {
                            AppVueObj.app.$store.dispatch(
                                SearchActions.setDataSource,
                                { value: source }
                            )
                            break
                        }
                    }
                    AppVueObj.app.$store.dispatch(
                        SearchActions.setTargetIds,
                        ''
                    )
                    return outputDataObject
                })
            }
        } else {
            const resultsTimeout = setTimeout(() => {
                AppVueObj.app.$store.dispatch(PopupActions.openPopup, {
                    contentId: 'dab-request-too-long',
                    component: DabRequestTooLong,
                })
            }, Timers.dabRequest)

            let params = requestParams

            if (UtilsService.isWidget()) {
                params = {
                    ...requestParams,
                    accesskey:
                        AppVueObj.app.$store.getters[
                            GeneralGetters.widgetAccessKey
                        ],
                }
            }

            const requestsParams = (cache.params =
                GeossSearchApiService.getDataSourcesParams(params))
            const promises = []
            const hiddenSources =
                AppVueObj.app.$store.getters[SearchGetters.hiddenDataSources]
            const freezeSources =
                AppVueObj.app.$store.getters[SearchGetters.freezeDataSources]

            if (!theSameTab) {
                for (const sourceName in DataPromiseId) {
                    if (
                        !hiddenSources.includes(sourceName) &&
                        !freezeSources.includes(sourceName) &&
                        (!targetSource || // no targetSource, so all requests
                            (!theSameSource &&
                                (DataSourceGroup[targetSource] ===
                                    DataOrigin[sourceName as DataSource] || // get request for the same origin
                                    DataSourceGroup[targetSource] ===
                                        DataSourceGroup[
                                            sourceName as DataSource
                                        ])) || // get request for the same tab
                            (theSameSource && targetSource === sourceName)) // get request only for the same dataSource
                    ) {
                        const requestWorkflow =
                            DataPromiseId[sourceName as DataSource] ===
                                'cr_information' ||
                            DataPromiseId[sourceName as DataSource] ===
                                'cr_services'
                                ? workflow
                                : false
                        promises.push({
                            id: DataPromiseId[sourceName as DataSource],
                            request: requestWorkflow
                                ? null
                                : makeRequest(
                                      'get',
                                      `${
                                          SearchEngineService.getSearchUrl()[
                                              sourceName as DataSource
                                          ]
                                      }?${
                                          requestsParams[
                                              sourceName as DataSource
                                          ]
                                      }`,
                                      null,
                                      false,
                                      {
                                          requestId:
                                              cancellableRequests[
                                                  sourceName as DataSource
                                              ],
                                          timeout: Timers.dabRequestMax,
                                          headers: { Accept: '*/*' },
                                      } as any,
                                      true
                                  ),
                        })
                    } else if (freezeSources.includes(sourceName)) {
                        for (const frozenSource of freezeSources) {
                            promises.push({
                                id: DataPromiseId[frozenSource as DataSource],
                                request: new Promise((resolve) => {
                                    return resolve(
                                        AppVueObj.app.$store.getters[
                                            (<any>SearchGetters)[
                                                `${
                                                    frozenSource as DataSource
                                                }Results`
                                            ]
                                        ]
                                    )
                                }),
                            })
                        }
                    }
                }

                // Load SeeAlso and Recommendations
                // LogService.getSeeAlsoWords(requestParams.st, 10, true)
                //     .catch((error: any) => error)
                //     .then((result: any) => {
                //         AppVueObj.app.$store.dispatch(
                //             SearchActions.setRecentSeeAlsoPhrases,
                //             result
                //         )
                //     })
                // LogService.getSeeAlsoRecommendations(requestParams.st)
                //     .catch((error: any) => error)
                //     .then((result: any) => {
                //         AppVueObj.app.$store.dispatch(
                //             SearchActions.setRecentSeeAlsoRecommendations,
                //             result
                //         )
                //     })
            } else {
                const sourceName: DataSource =
                    AppVueObj.app.$store.getters[SearchGetters.dataSource]
                promises.push({
                    id: DataPromiseId[sourceName],
                    request: makeRequest(
                        'get',
                        `${SearchEngineService.getSearchUrl()[sourceName]}?${
                            requestsParams[sourceName]
                        }`,
                        null,
                        true,
                        {
                            requestId: cancellableRequests.dab,
                            timeout: Timers.dabRequestMax,
                            headers: { Accept: '*/*' },
                        } as any,
                        true
                    ),
                })
            }

            const allResultsPromises = []

            for (const promise of promises) {
                if (!promise.request) {
                    continue
                }
                allResultsPromises.push(
                    new Promise((resolve) => {
                        promise.request
                            .then((data: any) => {
                                if (
                                    freezeSources.includes(
                                        (<any>DataSourcesMask)[promise.id]
                                    )
                                ) {
                                    let jsonData: {
                                        dataSource: DataSource
                                        entry: any[]
                                        totalResults: number
                                    } = data
                                    if (!jsonData) {
                                        jsonData = {
                                            dataSource:
                                                promise.id as DataSource,
                                            entry: [],
                                            totalResults: 0,
                                        }
                                    }
                                    return resolve(jsonData)
                                }

                                if (promise.id === 'zenodo') {
                                    const jsonData = {
                                        dataSource: promise.id,
                                        entry: data.hits.hits,
                                        totalResults: data.hits.total,
                                    }
                                    if (jsonData.totalResults === 0) {
                                        return resolve({
                                            dataSource: promise.id,
                                            error: 'no_results',
                                        })
                                    } else {
                                        // Add information about datasource to each entry
                                        jsonData.entry = jsonData.entry.map(
                                            (item: { dataSource: any }) => {
                                                item.dataSource = promise.id
                                                return item
                                            }
                                        )
                                        const targetIds = jsonData.entry.map(
                                            (result: { id: any }) => result.id
                                        )
                                        const dataOrigin =
                                            DataOrigin[
                                                (<any>DataSourcesMask)[
                                                    promise.id
                                                ] as DataSource
                                            ]
                                        return Promise.all([
                                            GeossSearchApiService.getDabResultsRatings(
                                                targetIds,
                                                dataOrigin
                                            ),
                                            LogService.getRecentActionsLog(
                                                targetIds
                                            ),
                                        ])
                                            .then(([ratings, views]) => {
                                                if (ratings) {
                                                    for (const result of jsonData.entry) {
                                                        result.rating =
                                                            ratings.find(
                                                                (rating: {
                                                                    targetId: string
                                                                }) =>
                                                                    rating.targetId ===
                                                                    result.id +
                                                                        ''
                                                            )
                                                    }
                                                }
                                                if (views) {
                                                    for (const view of views) {
                                                        const result =
                                                            jsonData.entry.find(
                                                                (item: {
                                                                    id: any
                                                                }) =>
                                                                    item.id ===
                                                                    view._source
                                                                        .ui_entry_id
                                                            )
                                                        if (result) {
                                                            result.views =
                                                                result.views
                                                                    ? result.views +
                                                                      1
                                                                    : 1
                                                        }
                                                    }
                                                }
                                            })
                                            .finally(() => {
                                                return resolve(jsonData)
                                            })
                                    }
                                }

                                /* Strip "atom:" prefix from starting and ending tags of all responses except of Zenodo */
                                data =
                                    GeossSearchApiService.stripAtomPrefix(data)
                                const jsonData = JSON.parse(
                                    parseXMLToJSON(data)
                                )

                                // Make sure jsonData.feed.entry is an array
                                if (
                                    jsonData &&
                                    jsonData.feed &&
                                    jsonData.feed.entry &&
                                    jsonData.feed.entry.constructor !== Array
                                ) {
                                    jsonData.feed.entry = [jsonData.feed.entry]

                                    // Add information about datasource to each entry
                                    jsonData.feed.entry =
                                        jsonData.feed.entry.map(
                                            (item: { dataSource: any }) => {
                                                item.dataSource = promise.id
                                                return item
                                            }
                                        )
                                }

                                jsonData.feed.dataSource = promise.id

                                if (
                                    jsonData.feed.dataSource === 'nextgeoss' &&
                                    jsonData.feed.entry
                                ) {
                                    jsonData.feed.entry =
                                        GeossSearchApiService.setNextgeossId(
                                            jsonData.feed.entry
                                        )
                                }

                                if (
                                    jsonData.feed &&
                                    jsonData.feed.entry &&
                                    jsonData.feed.entry.length &&
                                    UtilsService.getArrayByString(
                                        jsonData.feed,
                                        'entry.id'
                                    )
                                ) {
                                    const firstId =
                                        UtilsService.getPropByString(
                                            jsonData.feed,
                                            'entry.id'
                                        )
                                    if (firstId && firstId !== 'errmsgid') {
                                        const targetIds =
                                            jsonData.feed.entry.map(
                                                (result: { id: any }) =>
                                                    result.id
                                            )
                                        const dataOrigin =
                                            DataOrigin[
                                                (<any>DataSourcesMask)[
                                                    promise.id
                                                ] as DataSource
                                            ]
                                        return Promise.all([
                                            GeossSearchApiService.getDabResultsRatings(
                                                targetIds,
                                                dataOrigin
                                            ),
                                            LogService.getRecentActionsLog(
                                                targetIds
                                            ),
                                        ])
                                            .then(([ratings, views]) => {
                                                if (ratings) {
                                                    for (const result of jsonData
                                                        .feed.entry) {
                                                        result.rating =
                                                            ratings.find(
                                                                (rating: {
                                                                    targetId: any
                                                                }) =>
                                                                    rating.targetId ===
                                                                    result.id
                                                            )
                                                    }
                                                }
                                                if (views) {
                                                    for (const view of views) {
                                                        const result =
                                                            jsonData.feed.entry.find(
                                                                (item: {
                                                                    id: any
                                                                }) =>
                                                                    item.id ===
                                                                    view._source
                                                                        .ui_entry_id
                                                            )
                                                        if (result) {
                                                            result.views =
                                                                result.views
                                                                    ? result.views +
                                                                      1
                                                                    : 1
                                                        }
                                                    }
                                                }
                                            })
                                            .finally(() => {
                                                return resolve(jsonData.feed)
                                            })
                                    }
                                }

                                if (
                                    jsonData.feed &&
                                    jsonData.feed.entry &&
                                    jsonData.feed.entry.length &&
                                    UtilsService.getArrayByString(
                                        jsonData.feed,
                                        'entry.id'
                                    )
                                ) {
                                    const firstEntry =
                                        UtilsService.getPropByString(
                                            jsonData.feed,
                                            'entry'
                                        )
                                    if (
                                        firstEntry &&
                                        firstEntry.id === 'errmsgid'
                                    ) {
                                        return resolve({
                                            dataSource: promise.id,
                                            error: firstEntry.title,
                                        })
                                    }
                                }

                                if (jsonData.feed) {
                                    if (
                                        jsonData.feed[
                                            'opensearch:totalResults'
                                        ] === 0 ||
                                        jsonData.feed.totalResults === 0
                                    ) {
                                        return resolve({
                                            dataSource: promise.id,
                                            error: 'no_results',
                                        })
                                    }
                                }
                                return resolve({
                                    dataSource: promise.id,
                                    error: { jsonData },
                                })
                            })
                            .catch((err: { code: string; message: any }) => {
                                if (axios.isCancel(err)) {
                                    if (AppVueObj.storeStateBackup) {
                                        UtilsService.popFromHistory()
                                    }
                                    return resolve({
                                        dataSource: promise.id,
                                        error: 'cancel',
                                    })
                                } else if (err && err.code === 'ECONNABORTED') {
                                    return resolve({
                                        dataSource: promise.id,
                                        error: 'timeout',
                                    })
                                }
                                return resolve({
                                    dataSource: promise.id,
                                    error: err.message,
                                })
                            })
                    })
                )
            }

            if (!otherSourcesLoading) {
                SpinnerService.showSpinner(null, true)
            } else {
                AppVueObj.app.$store.dispatch(SearchActions.setParentIds, '')
                AppVueObj.app.$store.dispatch(
                    SearchActions.setFreezeDataSources,
                    []
                )
            }
            return Promise.all(allResultsPromises)
                .then((results) => {
                    if (theSameTab) {
                        const outputDataObject = JSON.parse(
                            JSON.stringify(emptyOutputDataObject)
                        )
                        const dataSource =
                            AppVueObj.app.$store.getters[
                                SearchGetters.dataSource
                            ]
                        outputDataObject[`${dataSource}Results`] = results[0]
                        return outputDataObject
                    }
                    return {
                        dabResults: results.filter(
                            (res: any) => res.dataSource === 'dab'
                        )[0] || { dataSource: 'dab', error: 'no_results' },
                        dataResults: results.filter(
                            (res: any) => res.dataSource === 'cr_data'
                        )[0] || { dataSource: 'data', error: 'no_results' },
                        amerigeossResults: results.filter(
                            (res: any) => res.dataSource === 'amerigeoss_ckan'
                        )[0] || {
                            dataSource: 'amerigeoss',
                            error: 'no_results',
                        },
                        nextgeossResults: results.filter(
                            (res: any) => res.dataSource === 'nextgeoss'
                        )[0] || {
                            dataSource: 'nextgeoss',
                            error: 'no_results',
                        },
                        informationResults: results.filter(
                            (res: any) => res.dataSource === 'cr_information'
                        )[0] || {
                            dataSource: 'information',
                            error: 'no_results',
                        },
                        zenodoResults: results.filter(
                            (res: any) => res.dataSource === 'zenodo'
                        )[0] || { dataSource: 'zenodo', error: 'no_results' },
                        wikipediaResults: results.filter(
                            (res: any) => res.dataSource === 'wikipedia'
                        )[0] || {
                            dataSource: 'wikipedia',
                            error: 'no_results',
                        },
                        servicesResults: results.filter(
                            (res: any) => res.dataSource === 'cr_services'
                        )[0] || { dataSource: 'services', error: 'no_results' },
                    }
                })
                .finally(() => {
                    if (!otherSourcesLoading) {
                        SpinnerService.hideSpinner()
                    } else {
                        AppVueObj.app.$store.dispatch(
                            SearchActions.setOtherSourcesLoading,
                            false
                        )
                    }
                    clearTimeout(resultsTimeout)
                    TutorialTagsService.refreshTagsAll()
                    PopupCloseService.closePopup('dab-request-too-long')
                })
        }
    },

    getDabResultMetadata(id: string) {
        const url = `${SearchEngineService.getDabBaseUrl()}/cswisogeo?service=CSW&request=GetRecordById&id=${id}&outputschema=http://www.isotc211.org/2005/gmi&elementSetName=full`
        return makeRequest('get', url)
            .then((data: string) => {
                const jsonData = JSON.parse(parseXMLToJSON(data))
                const mainData =
                    jsonData['csw:GetRecordByIdResponse']['gmi:MI_Metadata']
                if (mainData) {
                    if (
                        mainData['gmd:contact'] &&
                        mainData['gmd:contact'].constructor !== Array
                    ) {
                        mainData['gmd:contact'] = [mainData['gmd:contact']]
                    }

                    return mainData
                }
                return Promise.reject()
            })
            .catch((thrown: { status?: any; statusText?: any }) => {
                if (!axios.isCancel(thrown)) {
                    thrown = thrown || {}
                    LogService.logResourceError(
                        null,
                        id,
                        null,
                        'Attempting to access metadata',
                        'Present Metadata Error',
                        `Status code: ${thrown.status}, Status text: ${thrown.statusText}`
                    )
                    const props = {
                        title: AppVueObj.app.$tc('general.error'),
                        subtitle: AppVueObj.app.$tc('errors.noMetadata'),
                    }
                    AppVueObj.app.$store.dispatch(PopupActions.openPopup, {
                        contentId: 'error',
                        component: ErrorPopup,
                        props,
                    })
                }
                return Promise.reject(thrown)
            })
    },

    loadLayerFile(urlToResource: any, type: any) {
        return makeRequest(
            'get',
            SearchEngineService.getCheckLayerFileUrl(urlToResource, type)
        )
    },

    getCustomDownloadOptionsWCS(url: string) {
        return Promise.all([
            makeRequest('get', url + '&request=GetCapabilities'),
            makeRequest('get', url + '&request=DescribeCoverage'),
        ]).then(([getCapabilities, describeCoverage]) => {
            getCapabilities = JSON.parse(parseXMLToJSON(getCapabilities))
            describeCoverage = JSON.parse(parseXMLToJSON(describeCoverage))
            return {
                getCapabilities,
                describeCoverage,
            }
        })
    },

    getCustomDownloadOptions(url: string) {
        return makeRequest(
            'get',
            url + '?service=WPS&request=execute&identifier=gi-axe-capabilities'
        ).then((data: string) => {
            const jsonData = JSON.parse(parseXMLToJSON(data))
            if (jsonData['ns2:ExecuteResponse']) {
                return UtilsService.getPropByString(
                    jsonData,
                    'ns2:ExecuteResponse.ns2:ProcessOutputs.ns2:Output.ns2:Data.ns2:ComplexData.ns2:capabilityCubes.ns2:capabilityCube'
                )
            } else if (jsonData['ns3:ExecuteResponse']) {
                return UtilsService.getPropByString(
                    jsonData,
                    'ns3:ExecuteResponse.ns3:ProcessOutputs.ns3:Output.ns3:Data.ns3:ComplexData.ns2:capabilityCubes.ns2:capabilityCube'
                )
            }
        })
    },

    checkAsyncDownloadStatus(file: DownloadFile) {
        let resolvePromise: (arg0: string | any) => void

        const promise = new Promise((resolve) => {
            resolvePromise = resolve
        })

        const data = {
            timeout: null,
            promise,
        }

        const extentionMap: { [key: string]: string } = {
            'application/gml+xml': 'xml',
            'application/gtopo30': 'gtopo30',
            'application/x-gzip': 'gz',
            'image/jpeg': 'jpg',
            'image/png': 'png',
            'image/tiff': 'tif',
            'text/plain': 'txt',
        }

        let downloadName = ''

        if (extentionMap[file.format]) {
            downloadName = `${file.download}.${extentionMap[file.format]}`
        }

        if (file.type === 'wcs') {
            makeRequest('get', file.checkStatusUrl, null, true, {
                timeout: 1200000,
                responseType: 'blob',
            })
                .then((data: BlobPart) => {
                    window.URL = window.URL || window.webkitURL
                    const blob = new Blob([data], { type: file.format })
                    const blobUrl = window.URL.createObjectURL(blob)
                    resolvePromise({ url: blobUrl, download: downloadName })
                })
                .catch((error: any) => {
                    resolvePromise('error')
                    console.warn(error)
                })
        } else {
            let method: 'post' | 'get' = 'get'
            let payload = null
            let config = {}
            if (file.checkStatusPayload) {
                method = 'post'
                payload = file.checkStatusPayload
                config = {
                    headers: {
                        'content-type': 'application/xml',
                    },
                }
            }
            const repetitiveRequest = (url: string) => {
                makeRequest('get', url, null, true)
                    .then(
                        (data: {
                            data: any
                            status: { toString: () => string }
                            message: { toString: () => string }
                            timeout: string | NodeJS.Timeout
                        }) => {
                            if (data.data) {
                                resolvePromise(data.data)
                            } else if (
                                data.status.toString().toLowerCase() ===
                                    'fail' ||
                                data.message.toString().toLowerCase() ===
                                    'stop' ||
                                data.status.toString().toLowerCase() === 'error'
                            ) {
                                resolvePromise('error')
                            } else {
                                if (data.timeout !== 'cancel') {
                                    data.timeout = setTimeout(() => {
                                        repetitiveRequest(url)
                                    }, 4000)
                                }
                            }
                        }
                    )
                    .catch(() => resolvePromise('error'))
            }

            makeRequest(method, file.checkStatusUrl, payload, true, config)
                .then((data: string) => {
                    const jsonData = JSON.parse(parseXMLToJSON(data))
                    if (jsonData['ns2:ExecuteResponse']) {
                        repetitiveRequest(
                            jsonData['ns2:ExecuteResponse']._attributes
                                .statusLocation
                        )
                    } else if (jsonData['ns3:ExecuteResponse']) {
                        repetitiveRequest(
                            jsonData['ns3:ExecuteResponse']._attributes
                                .statusLocation
                        )
                    }
                })
                .catch((error: { response: { data: string } }) => {
                    const jsonData = JSON.parse(
                        parseXMLToJSON(error.response.data)
                    )
                    resolvePromise({ status: 'error', response: jsonData })
                })
        }
        return data
    },

    prepareBulkDownloadRequest(jsonData: any[]) {
        jsonData = jsonData.map((item: { url: string }) => {
            item.url = item.url.split('&').join('&amp;')
            return item
        })

        let requestBody = `
			<?xml version="1.0" encoding="UTF-8"?>
			<wps:Execute version="1.0.0" service="WPS" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				xmlns:wfs="http://www.opengis.net/wfs"
				xmlns:wps="http://www.opengis.net/wps/1.0.0" xmlns:ows="http://www.opengis.net/ows/1.1"
				xmlns:gml="http://www.opengis.net/gml" xmlns:ogc="http://www.opengis.net/ogc"
				xmlns:wcs="http://www.opengis.net/wcs/1.1.1" xmlns:xlink="http://www.w3.org/1999/xlink"
				xsi:schemaLocation="http://www.opengis.net/wps/1.0.0 http://schemas.opengis.net/wps/1.0.0/wpsAll.xsd">
				<ows:Identifier>bulk-download</ows:Identifier>
				<wps:DataInputs>`

        // @ts-ignore
        jsonData = jsonData.forEach((item: { title: any; url: any }) => {
            requestBody += `
					<wps:Input>
						<ows:Identifier>download</ows:Identifier>
						<ows:Title>${item.title}</ows:Title>
						<wps:Reference xlink:href="${item.url}"></wps:Reference>
					</wps:Input>`
        })

        requestBody += `
				</wps:DataInputs>
				<wps:ResponseForm>
					<wps:ResponseDocument storeExecuteResponse="true">
						<wps:Output>
							<ows:Identifier>Bulk download report</ows:Identifier>
						</wps:Output>
					</wps:ResponseDocument>
				</wps:ResponseForm>
			</wps:Execute>`

        return requestBody.trim()
    },

    getUserContributions(entryIds: string, dataOrigin: string) {
        return makeRequest(
            'get',
            `${SearchEngineService.getInternalOpenSearchUrlRaw()}/api/extensions/details?ds=${dataOrigin}&ids=${entryIds}${UtilsService.getAccessKeyString()}`,
            null,
            true
        )
            .then((data: any[]) => {
                if (typeof data === 'object') {
                    return data[0] || {}
                }
                return {}
            })
            .catch((error: any) => {
                console.warn('Error: ', error)
                return {}
            })
    },

    entryExtensionPassToModerator(formData: any) {
        return makeRequest(
            'post',
            `/community/guest/geoss-curated-resources?p_p_id=geosscuratedresources_WAR_geossportlet&p_p_lifecycle=2&p_p_state=normal&p_p_mode=view&doAsGroupId=${Liferay.ThemeDisplay.getScopeGroupId()}&p_p_resource_id=ADD_RECORD_EXTENSION`,
            formData
        )
            .then((data: any) => {
                if (typeof data === 'object') {
                    return data
                }
                return false
            })
            .catch(() => {
                return false
            })
    },

    entryExtensionAdvancedViewLink(entryId: string, dataOrigin: string) {
        return `/group/control_panel/manage?p_p_id=geosscuratedrelationshipsextension_WAR_geossportlet&p_p_lifecycle=0&p_p_state=maximized&p_p_mode=view&doAsGroupId=${Liferay.ThemeDisplay.getScopeGroupId()}&controlPanelCategory=current_site.users&_geosscuratedrelationshipsextension_WAR_geossportlet_mvcPath=%2Fhtml%2Fgeosscuratedrelationshipsextension%2Fadd_entity.jsp&_geosscuratedrelationshipsextension_WAR_geossportlet_entryCode=${entryId}&_geosscuratedrelationshipsextension_WAR_geossportlet_dataSource=${dataOrigin}`
    },

    entryExtensionRemove(extensionType: string, extensionData: any) {
        const endpointUrl = `/community/guest/geoss-curated-resources?p_p_id=geosscuratedresources_WAR_geossportlet&p_p_lifecycle=2&p_p_state=normal&p_p_mode=view&doAsGroupId=20181&p_p_resource_id=${extensionType}`
        return makeRequest('post', endpointUrl, extensionData)
            .then((data: any) => {
                if (typeof data === 'object') {
                    return data
                }
                return false
            })
            .catch(() => {
                return false
            })
    },

    entryExtensionRemoveAvailable(extensionId: string) {
        const endpointUrl = `/community/guest/geoss-curated-resources?p_p_id=geosscuratedresources_WAR_geossportlet&p_p_lifecycle=2&p_p_state=normal&p_p_mode=view&doAsGroupId=20181&p_p_resource_id=CAN_REMOVE_EXTENSION_DATA&_geosscuratedresources_WAR_geossportlet_entryExtensionId=${extensionId}`
        return makeRequest('post', endpointUrl)
            .then((data: any) => {
                if (typeof data === 'object') {
                    return data
                }
                return false
            })
            .catch(() => {
                return false
            })
    },

    entryRelationsAPI(
        getPendingRelations: boolean = false,
        relationsData: any = {}
    ) {
        const relationsEndpoint = getPendingRelations
            ? 'GET_PENDING_ENTRY_RELATIONS'
            : AppVueObj.app.$store.getters[SearchGetters.crRelationParams]
                  .endpoint
        let relationsUrl = `/community/guest/geoss-curated-resources?p_p_id=geosscuratedresources_WAR_geossportlet&p_p_lifecycle=2&p_p_state=normal&p_p_mode=view&doAsGroupId=20181&p_p_resource_id=${relationsEndpoint}`
        if (
            AppVueObj.app.$store.getters[SearchGetters.crRelationParams]
                .workflowInstanceId
        ) {
            relationsUrl += `&_geosscuratedresources_WAR_geossportlet_workflowInstanceId=${
                AppVueObj.app.$store.getters[SearchGetters.crRelationParams]
                    .workflowInstanceId
            }`
            relationsUrl += `&_geosscuratedresources_WAR_geossportlet_srcEntryId=${
                AppVueObj.app.$store.getters[SearchGetters.crRelationParams]
                    .crRelationSrcId
            }`
        }
        return makeRequest('post', relationsUrl, relationsData)
            .then((data: any) => {
                if (typeof data === 'object') {
                    return data
                }
                return false
            })
            .catch(() => {
                return false
            })
    },

    getBookmarks(start: number = 0, end: number = 9999) {
        return sendLiferayRequest(
            '/geoss-service-portlet.bookmarkedresult/get-bookmarked-results-by-user-id-and-group-id',
            {
                groupId: Liferay.ThemeDisplay.getScopeGroupId(),
                start,
                end,
            },
            true
        )
            .then((data: { items: any }) => {
                if (typeof data === 'object' && data.items) {
                    return data
                }
                return false
            })
            .catch(() => {
                return false
            })
    },

    addBookmark(
        name: string,
        targetId: any,
        currMap: string,
        dataSource: string
    ) {
        return sendLiferayRequest(
            '/geoss-service-portlet.bookmarkedresult/add-bookmarked-result',
            {
                groupId: Liferay.ThemeDisplay.getScopeGroupId() || 0,
                name,
                targetId,
                currMap,
                dataSource,
            }
        )
            .then((data: { id: any }) => {
                if (typeof data === 'object' && data.id) {
                    return data
                }
                return false
            })
            .catch(() => {
                return false
            })
    },

    removeBookmark(targetId: string, dataSource: string) {
        return sendLiferayRequest(
            '/geoss-service-portlet.bookmarkedresult/delete-bookmarked-result-by-target-id',
            {
                groupId: Liferay.ThemeDisplay.getScopeGroupId() || 0,
                targetId,
                dataSource,
            }
        )
            .then(() => {
                return true
            })
            .catch(() => {
                return false
            })
    },

    getGeoLikes(start: number = 0, end: number = 9999) {
        return sendLiferayRequest(
            '/geoss-service-portlet.resourceratingentry/get-resource-ratings-by-user-id-and-group-id',
            {
                groupId:
                    !UtilsService.isWidget() && typeof Liferay !== 'undefined'
                        ? Liferay.ThemeDisplay.getScopeGroupId() || 0
                        : 0,
                start,
                end,
            },
            true
        )
            .then((data: { items: any }) => {
                if (typeof data === 'object' && data.items) {
                    return data
                }
                return false
            })
            .catch(() => {
                return false
            })
    },

    rateResource(
        name: string,
        targetId: string,
        score: number,
        comment: string,
        dataSource: string
    ) {
        return sendLiferayRequest(
            '/geoss-service-portlet.resourceratingentry/update-resource-ratings',
            {
                groupId: Liferay.ThemeDisplay.getScopeGroupId() || 0,
                name,
                targetId,
                score,
                comment,
                dataSource,
            }
        )
            .then((data: { targetId: any }) => {
                if (typeof data === 'object' && data.targetId) {
                    return data
                }
                return false
            })
            .catch(() => {
                return false
            })
    },

    removeGeoLike(targetId: string, dataSource: string) {
        return sendLiferayRequest(
            '/geoss-service-portlet.resourceratingentry/delete-resource-rating',
            {
                groupId: Liferay.ThemeDisplay.getScopeGroupId() || 0,
                targetId,
                dataSource,
            }
        )
            .then(() => {
                return true
            })
            .catch(() => {
                return false
            })
    },

    getRatings(targetIds: string, dataSource: string) {
        return sendLiferayRequest(
            '/geoss-service-portlet.resourceratingentry/get-resource-ratings',
            {
                groupId:
                    !UtilsService.isWidget() && typeof Liferay !== 'undefined'
                        ? Liferay.ThemeDisplay.getScopeGroupId() || 0
                        : 0,
                targetIds: encodeURIComponent(targetIds).replace(/'/g, '%27'),
                dataSource,
            },
            true
        )
            .then((data: { stats: any }) => {
                if (typeof data === 'object' && data.stats) {
                    return data
                }
                return false
            })
            .catch(() => {
                return false
            })
    },

    getComments(targetId: string, dataSource: string) {
        return sendLiferayRequest(
            '/geoss-service-portlet.resourceratingentry/get-resource-comments',
            {
                groupId:
                    !UtilsService.isWidget() && typeof Liferay !== 'undefined'
                        ? Liferay.ThemeDisplay.getScopeGroupId() || 0
                        : SearchEngineService.getMainSiteGroupId(),
                targetId,
                dataSource,
            }
        )
            .then((data: any[]) => {
                if (typeof data === 'object' && data['entries']) {
                    return data
                }
                return false
            })
            .catch(() => {
                return false
            })
    },

    getPopularSearches() {
        return sendLiferayRequest(
            '/geoss-service-portlet.popularsearch/get-available-popular-searches',
            {
                groupId:
                    !UtilsService.isWidget() && typeof Liferay !== 'undefined'
                        ? Liferay.ThemeDisplay.getScopeGroupId() || 0
                        : 0,
                start: 0,
                end: 9999,
            },
            true
        )
            .then((data: { items: any }) => {
                if (typeof data === 'object' && data.items) {
                    return data.items
                }
                return false
            })
            .catch(() => {
                return Promise.resolve(false)
            })
    },

    getPopularSearchById(id: number) {
        return GeossSearchApiService.getPopularSearches().then(
            (data: any[]) => {
                if (data) {
                    return data.find((item: { id: number }) => item.id === id)
                }
                return null
            }
        )
    },

    getSavedSearches() {
        return sendLiferayRequest(
            '/geoss-service-portlet.savedsearch/get-user-saved-searches',
            {
                groupId: Liferay.ThemeDisplay.getScopeGroupId() || 0,
                start: 0,
                end: 9999,
            },
            true
        )
            .then((data: { items: any }) => {
                if (typeof data === 'object' && data.items) {
                    return data.items
                }
                return false
            })
            .catch(() => {
                return Promise.resolve(false)
            })
    },

    getSavedSearchById(id: number) {
        return GeossSearchApiService.getSavedSearches().then((data: any[]) => {
            if (data) {
                return data.find((item: { id: number }) => item.id === id)
            }
            return null
        })
    },

    getImagePathProxy(imagePath: string) {
        const url = `/community/guest/geoss-resources?p_p_id=geossresources_WAR_geossportlet&p_p_lifecycle=2&p_p_resource_id=IMAGES_PROXY`
        return makeRequest('post', url, imagePath, false, {
            headers: { 'Content-Type': 'text/plain' },
        })
            .then((data: { uniqueId: any }) => {
                if (data && data.uniqueId) {
                    return data.uniqueId
                }
                return null
            })
            .catch(() => {
                return null
            })
    },

    addSavedSearch(searchData: {
        name: string
        popularSearchId: number
        query: string
        fullAndOpenDataset: boolean
        dateFrom: string
        dateTo: string
        datePeriod: string
        aoiOption: string
        aoiBoundingBox: string
        aoiGeolocation: string
        aoiW3W: string
        aoiRelation: string
        cloudCoverageFrom: number
        cloudCoverageTo: number
        queryParams: any
        folder: string
        targetId: string
        filterParams: any
        filterSet: string
        currMap: string
        groupId: number
        dataSource: string
    }) {
        return sendLiferayRequest(
            '/geoss-service-portlet.savedsearch/add-saved-search',
            searchData
        )
            .then((data: { id: any }) => {
                if (typeof data === 'object' && data.id) {
                    return true
                }
                return false
            })
            .catch(() => {
                return false
            })
    },

    addSavedRun(runName: string, workflow: any, runId: string) {
        return sendLiferayRequest(
            '/geoss-service-portlet.savedrun/add-saved-run',
            {
                name:
                    runName !== ''
                        ? runName
                        : `${new Date()
                              .toLocaleString('en-GB')
                              .replace(/,/g, '')} ${workflow.name}`,
                runId,
                userId: Liferay.ThemeDisplay.getUserId(),
                groupId: Liferay.ThemeDisplay.getScopeGroupId(),
                path: '',
                workflowId: workflow.id,
            }
        )
    },

    getSavedRuns(
        workflowUrl: string,
        isSignedIn: boolean,
        startIndex: number,
        resultsPerPage: number
    ) {
        let url = '/geoss-service-portlet.savedrun/get-all-saved-runs'
        if (isSignedIn) {
            url = '/geoss-service-portlet.savedrun/get-user-saved-runs'
        }
        return sendLiferayRequest(url, {
            groupId: Liferay.ThemeDisplay.getScopeGroupId(),
            start: startIndex,
            end: startIndex + resultsPerPage,
        }).then(
            ({ totalCount, items }: { totalCount: number; items: any[] }) => {
                return Promise.all(
                    items.map(
                        (item: {
                            status: string
                            runId: string
                            messageList: any
                            result: any
                            showLogs: boolean
                            outputs: any
                            showOutputs: boolean
                        }) => {
                            item.status = 'UNKNOWN'
                            return GeossSearchApiService.getRunStatus(
                                item.runId
                            ).then(
                                ({
                                    status,
                                    messageList,
                                    result,
                                }: {
                                    status: string
                                    messageList: any
                                    result: any
                                }) => {
                                    item.status = status
                                    item.messageList = messageList
                                    item.result = result
                                    item.showLogs = false
                                    if (
                                        status === 'COMPLETED' ||
                                        status === 'SUCCESS'
                                    ) {
                                        return GeossSearchApiService.getRunOutputs(
                                            item.runId
                                        ).then(({ outputs }: any) => {
                                            item.outputs = outputs
                                            item.showOutputs = false
                                        })
                                    }
                                }
                            )
                        }
                    )
                )
                    .catch(() => {
                        return { totalCount, items }
                    })
                    .then(() => {
                        return { totalCount, items }
                    })
            }
        )
    },

    getRunStatus(runId: string) {
        return makeRequest(
            'get',
            `${SearchEngineService.getKpBaseUrl()}/runs/${runId}/status`,
            null,
            false,
            vlabAuthorizationHeaders
        )
    },

    getRunOutputs(runId: string) {
        return makeRequest(
            'get',
            `${SearchEngineService.getKpBaseUrl()}/runs/${runId}/outputs`,
            null,
            false,
            vlabAuthorizationHeaders
        )
    },

    getRunCoordinates(runId: string) {
        // Getting outputs coordinates:
        // If user provided bbox during RUN, get from bbox input
        // Otherwise get and merge used in RUN resource bboxes
        // Note: To be changed into VLAB request=GetCapabilities
        // when available, to get fast and precise output coordinates
        if (!runId || typeof runId === 'undefined') {
            return null
        }
        return makeRequest(
            'get',
            `${SearchEngineService.getKpBaseUrl()}/runs/${runId}`,
            null,
            false,
            vlabAuthorizationHeaders
        )
            .then((data: { inputs: any[] }) => {
                if (data) {
                    const bbox = data.inputs
                        .filter(
                            (item: { valueSchema: string }) =>
                                item.valueSchema === 'bbox'
                        )
                        .map((resource: { value: any }) => resource.value)
                    if (typeof bbox[0] !== 'undefined') {
                        let coordinate = bbox[0].split(',')
                        coordinate = {
                            W: coordinate[0] * 1,
                            S: coordinate[1] * 1,
                            E: coordinate[2] * 1,
                            N: coordinate[3] * 1,
                        }
                        return coordinate
                    } else {
                        const resources = data.inputs
                            .filter(
                                (item: { valueSchema: string }) =>
                                    item.valueSchema === 'sat_product'
                            )
                            .map(
                                (resource: { value: any }) =>
                                    `SatelliteScene_SENTINEL_2A__sep__${resource.value}`
                            )
                        return makeRequest(
                            'get',
                            `${BaseUrl()}/community/guest/geoss-resources?p_p_id=geossresources_WAR_geossportlet&p_p_lifecycle=2&p_p_state=normal&p_p_mode=view&p_p_cacheability=cacheLevelPage&controlPanelCategory=current_site.configuration&p_p_resource_id=BOOKMARKED_FEED&_geossresources_WAR_geossportlet_targetIds=${resources.join()}&_geossresources_WAR_geossportlet_dataSource=dab`
                        )
                            .then((data: string) => {
                                if (data) {
                                    const jsonData = JSON.parse(
                                        parseXMLToJSON(data)
                                    )
                                    if (
                                        jsonData &&
                                        jsonData.feed &&
                                        jsonData.feed.entry
                                    ) {
                                        const boxes = []
                                        for (const entry of jsonData.feed
                                            .entry) {
                                            boxes.push(entry.box.split(' '))
                                        }
                                        let coordinate: any =
                                            MapCoordinatesUtils.mergeBoxes(
                                                boxes
                                            )
                                        coordinate = {
                                            W: coordinate[1] * 1,
                                            S: coordinate[0] * 1,
                                            E: coordinate[3] * 1,
                                            N: coordinate[2] * 1,
                                        }
                                        return coordinate
                                    } else {
                                        return null
                                    }
                                } else {
                                    return null
                                }
                            })
                            .catch((error: any) => {
                                console.warn(`getRunCoordinates: ${error}`)
                                return null
                            })
                    }
                } else {
                    return null
                }
            })
            .catch((error: any) => {
                console.warn(`getRunCoordinates: ${error}`)
                return null
            })
    },

    getDabResultsRatings(
        targetIds: string[] | string,
        dataOrigin: string = 'dab'
    ) {
        let targetIdsStr = ''
        if (typeof targetIds === 'string' || typeof targetIds === 'number') {
            targetIdsStr = targetIds
        } else {
            targetIdsStr = targetIds.join(',')
        }
        return GeossSearchApiService.getRatings(targetIdsStr, dataOrigin)
            .then((data: { stats: any }) => {
                return data.stats
            })
            .catch((err: { status: any; statusText: any }) => {
                LogService.logResourceError(
                    true,
                    null,
                    null,
                    'Search query',
                    'Search query is not available at the moment',
                    `Ajax problem, Status: ${err.status}, Status text: ${err.statusText}`
                )
            })
    },

    getDabResultComments(targetId: string) {
        return makeRequest(
            'get',
            SearchEngineService.getGetCommentsUrl(targetId)
        ).then((data: any[]) => {
            return data.entries
        })
    },

    sendSurveyData(data: any) {
        return makeRequest('get', SearchEngineService.getSurveyUrl(data)).then(
            (data: string) => {
                return data === 'success'
            }
        )
    },

    checkIfQueryLocation(query: string): Promise<{
        coordinate: MapCoordinate
        name: string
        updatedQuery: string
    }> {
        return new Promise((resolve, reject) => {
            if (!query) {
                return reject()
            }
            const separator = ' in '
            const separatorIndex = query.indexOf(separator)

            let location = query
            let updatedQuery = ''

            if (
                separatorIndex > 0 &&
                query.length > separatorIndex + separator.length
            ) {
                const tempQuery = query.slice(0, separatorIndex).trim()
                const tempLocation = query
                    .slice(separatorIndex + separator.length)
                    .trim()

                let isQuotationMarks = false
                if ((query.match(/"/g) || []).length === 2) {
                    const quotationIndex = []
                    for (let i = 0; i < query.length; i++) {
                        if (query[i] === '"') {
                            quotationIndex.push(i)
                        }
                    }
                    if (
                        separatorIndex > quotationIndex[0] &&
                        separatorIndex < quotationIndex[1]
                    ) {
                        isQuotationMarks = true
                    }
                } else if ((query.match(/"/g) || []).length) {
                    isQuotationMarks = true
                }

                if (tempQuery && tempLocation && !isQuotationMarks) {
                    location = tempLocation
                    updatedQuery = tempQuery
                } else {
                    return reject()
                }
            }

            if (typeof google !== 'undefined') {
                const service = new google.maps.places.PlacesService(
                    document.createElement('div')
                )
                service.textSearch(
                    {
                        query: location,
                    },
                    (results: string | any[], status: string) => {
                        if (
                            status === google.maps.places.PlacesServiceStatus.OK
                        ) {
                            if (!results.length) {
                                reject()
                            } else if (
                                results.length === 1 &&
                                results[0].geometry &&
                                results[0].geometry.viewport
                            ) {
                                const coordinate: MapCoordinate = {
                                    N: results[0].geometry.viewport
                                        .getNorthEast()
                                        .lat(),
                                    E: results[0].geometry.viewport
                                        .getNorthEast()
                                        .lng(),
                                    S: results[0].geometry.viewport
                                        .getSouthWest()
                                        .lat(),
                                    W: results[0].geometry.viewport
                                        .getSouthWest()
                                        .lng(),
                                }

                                const name = results[0].name

                                if (query.length > location.length) {
                                    resolve({ coordinate, name, updatedQuery })
                                } else if (
                                    query.length === location.length &&
                                    name &&
                                    (name.match(/,/g) || []).length < 2
                                ) {
                                    // if query contains just a location, check how many ',' are in geoCoordinates.name
                                    resolve({ coordinate, name, updatedQuery })
                                } else {
                                    reject()
                                }
                            } else {
                                reject()
                            }
                        } else {
                            if (status === 'OVER_QUERY_LIMIT') {
                                AppVueObj.app.$store.dispatch(
                                    GeneralFiltersActions.setGooglePlacesApiError,
                                    status
                                )
                            }
                            reject()
                        }
                    }
                )
            } else {
                reject()
            }
        })
    },

    getTargetById(targetId: string, dataSource: DataSource = 'dab') {
        const dataOrigin = DataOrigin[dataSource] || dataSource
        if (dataOrigin === DataSources.ZENODO) {
            const targetIds = targetId.split(',')
            const promises = []
            for (const targetId of targetIds) {
                promises.push(
                    makeRequest(
                        'get',
                        `${SearchEngineService.getServiceZenodoUrl()}${targetId}`,
                        null,
                        false,
                        { headers: { Accept: '*/*' } }
                    ).then((result: any) => {
                        if (!result) {
                            return {
                                dataSource: DataSources.ZENODO,
                                error: 'no_results',
                            }
                        } else {
                            return result
                        }
                    })
                )
            }
            return Promise.all(promises).then((results) => {
                const jsonData: {
                    dataSource: DataSource
                    entry: any[]
                    totalResults: number
                } = {
                    dataSource: DataSources.ZENODO,
                    entry: [],
                    totalResults: results.length,
                }
                for (const result of results) {
                    jsonData.entry.push(result)
                }

                const ratingsAndViews = [
                    GeossSearchApiService.getDabResultsRatings(
                        targetId,
                        dataOrigin
                    ),
                    LogService.getRecentActionsLog(targetId),
                ]

                Promise.all(ratingsAndViews).then(([ratings, views]) => {
                    if (ratings) {
                        for (const entry of jsonData.entry) {
                            entry.rating = ratings.find(
                                (rating: { targetId: string }) =>
                                    rating.targetId === entry.id + ''
                            )
                        }
                    }
                    if (views) {
                        for (const view of views) {
                            const result = jsonData.entry.find(
                                (item) => item.id === view._source.ui_entry_id
                            )
                            if (result) {
                                result.views = result.views
                                    ? result.views + 1
                                    : 1
                            }
                        }
                    }
                })

                return jsonData
            })
        } else {
            return Promise.all([
                makeRequest(
                    'get',
                    `${BaseUrl()}/community/guest/geoss-resources?p_p_id=geossresources_WAR_geossportlet&p_p_lifecycle=2&p_p_state=normal&p_p_mode=view&p_p_cacheability=cacheLevelPage&controlPanelCategory=current_site.configuration&p_p_resource_id=BOOKMARKED_FEED&_geossresources_WAR_geossportlet_targetIds=${targetId}&_geossresources_WAR_geossportlet_dataSource=${dataOrigin}`
                ),
                GeossSearchApiService.getDabResultsRatings(
                    targetId,
                    dataOrigin
                ),
                LogService.getRecentActionsLog(targetId),
            ]).then(([result, ratings, views]) => {
                if (result) {
                    result = GeossSearchApiService.stripAtomPrefix(result)
                    const jsonData = JSON.parse(parseXMLToJSON(result))

                    if (
                        jsonData &&
                        jsonData.feed &&
                        jsonData.feed.entry &&
                        jsonData.feed.entry.constructor !== Array
                    ) {
                        jsonData.feed.entry = [jsonData.feed.entry]
                    }

                    jsonData.feed.dataSource = dataOrigin

                    if (
                        jsonData.feed.dataSource === 'nextgeoss' &&
                        jsonData.feed.entry
                    ) {
                        jsonData.feed.entry =
                            GeossSearchApiService.setNextgeossId(
                                jsonData.feed.entry
                            )
                    }

                    if (ratings && jsonData.feed.entry) {
                        for (const entry of jsonData.feed.entry) {
                            entry.rating = ratings.find(
                                (rating: { targetId: any }) =>
                                    rating.targetId === entry.id
                            )
                        }
                    }

                    if (views) {
                        for (const view of views) {
                            const result = jsonData.feed.entry.find(
                                (item: { id: any }) =>
                                    item.id === view._source.ui_entry_id
                            )
                            if (result) {
                                result.views = result.views
                                    ? result.views + 1
                                    : 1
                            }
                        }
                    }

                    return jsonData.feed
                }
                return null
            })
        }
    },

    getWorkflow(url: string) {
        return Promise.all([
            makeRequest('get', url, null, false, vlabAuthorizationHeaders),
            makeRequest(
                'get',
                `${url}/inputs`,
                null,
                false,
                vlabAuthorizationHeaders
            ),
        ]).then(([data, { inputs }]) => {
            return {
                ...data,
                inputs,
            }
        })
    },

    getBpmn(url: string, addTimestamp: boolean) {
        if (addTimestamp) {
            url += `?t=${Date.now()}`
        }
        return makeRequest('get', url, null, false, {
            dataType: 'text',
            crossdomain: true,
        })
    },

    runWorkflow(url: string, data: any) {
        return makeRequest('post', url, data, false, vlabAuthorizationHeaders)
    },

    getStatisticsTimePeriods(seriesCodes: string[], areaCodes: number[]) {
        const baseUrl =
            'https://unstats.un.org/SDGAPI/v1/sdg/Series/TimePeriods'
        let seriesCodesString = ''
        let areaCodesString = ''
        for (const s of seriesCodes) {
            seriesCodesString += `&seriesCodes=${s}`
        }
        for (const a of areaCodes) {
            areaCodesString += `&areaCodes=${a}`
        }
        const postParams = `${seriesCodesString}${areaCodesString}`.replace(
            /^&+/g,
            ''
        )
        return makeRequest('post', `${baseUrl}`, postParams)
    },

    getStatisticsData(
        seriesCodes: string[],
        areaCodes: string[],
        timePeriod: string[]
    ) {
        const baseUrl = 'https://unstats.un.org/SDGAPI/v1/sdg/Series/Data'
        let seriesCodesString = ''
        let areaCodesString = ''
        let timePeriodString = ''
        for (const s of seriesCodes) {
            seriesCodesString += `&seriesCode=${s}`
        }
        for (const a of areaCodes) {
            areaCodesString += `&areaCode=${a}`
        }
        for (const t of timePeriod) {
            timePeriodString += `&timePeriod=${t}`
        }
        // Take only global statistics
        const dimensions = [{ name: 'Reporting Type', values: ['G'] }]
        const dimensionsString = `&dimensions=${encodeURIComponent(
            JSON.stringify(dimensions)
        )}`
        const pageSizeString = '&pageSize=500'
        return makeRequest(
            'get',
            `${baseUrl}?${seriesCodesString}${areaCodesString}${timePeriodString}${dimensionsString}${pageSizeString}`
        )
    },

    getSeriesDimensions(series: any[]) {
        const promises = []
        for (const s of series) {
            promises.push(
                makeRequest(
                    'get',
                    `https://unstats.un.org/SDGAPI/v1/sdg/Series/${s}/Dimensions`
                )
            )
        }
        return Promise.all(promises)
    },

    getUnepFileUrl(seriesCodes: string[]) {
        const baseUrl = 'https://unstats.un.org/SDGAPI/v1/sdg/Series/DataCSV'
        let seriesCodesString = ''
        for (const s of seriesCodes) {
            seriesCodesString += `&seriesCodes=${s}`
        }
        const postParams = seriesCodesString.replace(/^&+/g, '')
        return makeRequest('post', `${baseUrl}`, postParams)
    },

    safeString(str: { toString: () => string }) {
        return str
            .toString() // Convert to string
            .normalize('NFD') // Change diacritics
            .replace(/[\u0300-\u036f]/g, '') // Remove illegal characters
            .replace(/\s+/g, '-') // Change whitespace to dashes
            .toLowerCase() // Change to lowercase
            .replace(/&/g, '-and-') // Replace ampersand
            .replace(/[^a-z0-9\-]/g, '') // Remove anything that is not a letter, number or dash
            .replace(/-+/g, '-') // Remove duplicate dashes
            .replace(/^-*/, '') // Remove starting dashes
            .replace(/-*$/, '') // Remove trailing dashes
    },

    stripAtomPrefix(str: string) {
        return str.replace(/<atom:/g, '<').replace(/<\/atom:/g, '</')
    },

    setNextgeossId(entry: any) {
        return entry.map((item: { [x: string]: any; id: any }) => {
            item.id = item['dc:identifier']
            return item
        })
    },

    getEntryHub(entry: {
        category: { _attributes: { term: string; label: string } }
    }) {
        if (Array.isArray(entry.category)) {
            for (const category of entry.category) {
                if (category._attributes.term === 'geoss_cr') {
                    const hub = category._attributes.label.split('_')[0]
                    return hub === 'service' ? DataSources.SERVICES : hub
                }
            }
        } else {
            if (entry.category._attributes.term === 'geoss_cr') {
                const hub = entry.category._attributes.label.split('_')[0]
                return hub === 'service' ? DataSources.SERVICES : hub
            }
        }
    },
}

export default GeossSearchApiService
