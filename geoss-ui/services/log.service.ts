import es from 'elasticsearch'
import uuid from 'uuid'
import { Liferay, AppVueObj, BaseUrl } from '@/data/global'
import UAParser from 'ua-parser-js'
import { GeneralFiltersGetters } from '@/stores/general-filters/general-filters-getters'
import { GranulaFiltersGetters } from '@/stores/granula-filters/granula-filters-getters'
import { IrisFiltersGetters } from '@/stores/iris-filters/iris-filters-getters'
import { MyWorkspaceGetters } from '@/stores/my-workspace/my-workspace-getters'
import { SearchGetters } from '@/stores/search/search-getters'
import { FacetedFiltersGetters } from '@/stores/faceted-filters/faceted-filters-getters'
import SearchEngineService from '@/services/search-engine.service'
import UtilsService from '@/services/utils.service'
import { MapCoordinate } from '@/interfaces/MapCoordinate'
import { makeRequest, GeneralApiService } from './general.api.service'

const _PAQ = '_paq'
const LogService = {
    /*------------------------------------*/
    /*----- elastic client init ----------*/
    /*------------------------------------*/

    portalLogIndice: 'geoss_index',
    portalLogType: 'geoss_data',
    client: undefined,
    elasticsearch_live: false,
    userAgent: new UAParser().getResult(),
    /*-----------------------------------------------*/
    /*--- log query action/event to elastic & MATOMO ----*/
    /*-----------------------------------------------*/

    friendlySiteUrl() {
        let friendlySideUrl = ''
        let url =
            typeof Liferay !== 'undefined'
                ? Liferay.ThemeDisplay.getLayoutURL()
                : 'http://geoss.devel.esaportal.eu/home'
        const webPart = '/community/'
        const webPos = url.indexOf(webPart)
        if (webPos === -1) {
            friendlySideUrl = '/'
        } else {
            url = url.slice(webPos + webPart.length)
            friendlySideUrl = '/' + url.split('/')
        }
        if (friendlySideUrl.localeCompare('/guest') === 0) {
            friendlySideUrl = '/'
        }
        return friendlySideUrl
    },

    logSearchResult(
        pOperation,
        id?,
        className?,
        entryIdVal?,
        entryDbVal?,
        uiActionVal?
    ) {
        return new Promise((resolve, reject) => {
            if (LogService.elasticsearch_live) {
                let queryUrl = null
                if (pOperation === 'search_query') {
                    queryUrl = SearchEngineService.getDabOpenSearchUrl() + '?'
                    const filtersParams =
                        AppVueObj.app.$store.getters[
                            SearchGetters.filtersParams
                        ]
                    for (const key of Object.keys(filtersParams)) {
                        if (key) {
                            queryUrl += '&'
                        }
                        queryUrl += `${key}=${filtersParams[key]}`
                    }
                }

                let bbox = ''
                const { S, W, N, E }: MapCoordinate =
                    AppVueObj.app.$store.getters[GeneralFiltersGetters.state]
                        .selectedAreaCoordinates
                if (S && W && N && W) {
                    bbox = `${S} ${W} ${N} ${E}`
                }

                LogService.client.bulk(
                    {
                        body: [
                            // action description
                            {
                                index: {
                                    _index: LogService.portalLogIndice,
                                    _type: LogService.portalLogType,
                                },
                            },
                            // the document to index
                            LogService.addCommonProperties({
                                ui_object_id: id,
                                ui_object_class: className,
                                session_site_url: LogService.friendlySiteUrl(),
                                ui_source:
                                    pOperation === 'search_query' &&
                                    AppVueObj.app.$store.getters[
                                        GeneralFiltersGetters.state
                                    ].requestId
                                        ? 'dab'
                                        : entryDbVal,
                                ui_entry_id: entryIdVal,
                                ui_action: uiActionVal,
                                ds_query_url: queryUrl,
                                ds_reqID:
                                    AppVueObj.app.$store.getters[
                                        GeneralFiltersGetters.state
                                    ].requestId,
                                ds_si:
                                    pOperation === 'search_query' &&
                                    AppVueObj.app.$store.getters[
                                        SearchGetters.startIndex
                                    ]
                                        ? AppVueObj.app.$store.getters[
                                              SearchGetters.startIndex
                                          ].toString()
                                        : null,
                                ds_ct:
                                    pOperation === 'search_query' &&
                                    AppVueObj.app.$store.getters[
                                        GeneralFiltersGetters.state
                                    ]
                                        ? AppVueObj.app.$store.getters[
                                              GeneralFiltersGetters.state
                                          ].resultsPerPage.toString()
                                        : null,
                                ds_ts: AppVueObj.app.$store.getters[
                                    GeneralFiltersGetters.state
                                ].dateFrom,
                                ds_te: AppVueObj.app.$store.getters[
                                    GeneralFiltersGetters.state
                                ].dateTo,
                                ds_st: AppVueObj.app.$store.getters[
                                    GeneralFiltersGetters.state
                                ].phrase,
                                ds_rel: AppVueObj.app.$store.getters[
                                    GeneralFiltersGetters.state
                                ].boundingBoxRelation,
                                ds_kwd:
                                    pOperation === 'search_query'
                                        ? AppVueObj.app.$store.getters[
                                              FacetedFiltersGetters.keyword
                                          ]
                                        : null,
                                ds_parents_group: {
                                    key: AppVueObj.app.$store.getters[
                                        MyWorkspaceGetters.search
                                    ].folder
                                        ? AppVueObj.app.$store.getters[
                                              MyWorkspaceGetters.search
                                          ].folder.value
                                        : '',
                                    value: AppVueObj.app.$store.getters[
                                        MyWorkspaceGetters.search
                                    ].folder
                                        ? AppVueObj.app.$store.getters[
                                              MyWorkspaceGetters.search
                                          ].folder.label
                                        : '',
                                },
                                ds_sba: null,
                                ds_sources_group: AppVueObj.app.$store.getters[
                                    GeneralFiltersGetters.state
                                ].sources.lenth
                                    ? {
                                          key: AppVueObj.app.$store.getters[
                                              GeneralFiltersGetters.state
                                          ].sources,
                                          value: AppVueObj.app.$store.getters[
                                              GeneralFiltersGetters.state
                                          ].sources,
                                      }
                                    : {},
                                ds_views_group: AppVueObj.app.$store.getters[
                                    GeneralFiltersGetters.state
                                ].viewId
                                    ? {
                                          key: AppVueObj.app.$store.getters[
                                              GeneralFiltersGetters.state
                                          ].viewId,
                                          value: AppVueObj.app.$store.getters[
                                              GeneralFiltersGetters.state
                                          ].viewId,
                                      }
                                    : {},
                                ds_bbox: bbox,
                                ds_gdc: AppVueObj.app.$store.getters[
                                    GeneralFiltersGetters.state
                                ].geossDataCore,
                                ds_w3w: null,
                                ds_frmt:
                                    pOperation === 'search_query'
                                        ? AppVueObj.app.$store.getters[
                                              FacetedFiltersGetters.format
                                          ]
                                        : null,
                                ds_prot:
                                    pOperation === 'search_query'
                                        ? AppVueObj.app.$store.getters[
                                              FacetedFiltersGetters.protocol
                                          ]
                                        : null,
                                ds_score:
                                    pOperation === 'search_query'
                                        ? AppVueObj.app.$store.getters[
                                              FacetedFiltersGetters.score
                                          ]
                                        : null,
                                operation: pOperation,
                            }),
                        ],
                        ...UtilsService.getAccessKeyObject(),
                    },
                    (err, resp) => {
                        if (err) {
                            reject(err)
                        } else if (resp.items[0].create.status === 201) {
                            resolve(resp.items[0].create._id)
                        } else {
                            reject(resp)
                        }
                    }
                )
            }
        }).finally(() => {
            if (!UtilsService.isWidget()) {
                const paq = window[_PAQ]
                if (paq) {
                    paq.push([
                        'trackEvent',
                        'Search',
                        'phrase',
                        entryIdVal || '',
                    ])
                }
            }
        })
    },

    /*-----------------------------------------------*/
    /*--- log error to elastic  -----------------*/
    /*-----------------------------------------------*/
    logResourceError(
        attachUrl: boolean,
        result,
        pMessage,
        pOperation,
        pResult,
        pResultDetails
    ) {
        let linkUrlToShort = ''
        if (attachUrl) {
            linkUrlToShort =
                window.location.protocol +
                '//' +
                window.location.host +
                window.location.pathname +
                '?'
            linkUrlToShort +=
                AppVueObj.app.$store.getters[GeneralFiltersGetters.shareParams]
            linkUrlToShort +=
                AppVueObj.app.$store.getters[FacetedFiltersGetters.shareParams]
            linkUrlToShort +=
                AppVueObj.app.$store.getters[GranulaFiltersGetters.shareParams]
            linkUrlToShort +=
                AppVueObj.app.$store.getters[IrisFiltersGetters.shareParams]
            linkUrlToShort +=
                AppVueObj.app.$store.getters[MyWorkspaceGetters.shareParams]
        }

        const bulkObject = {
            body: [
                {
                    index: {
                        _index: LogService.portalLogIndice,
                        _type: LogService.portalLogType,
                    },
                },
                LogService.addCommonProperties({
                    ui_entry_id: result ? result.id : null,
                    result: pResult,
                    operation: pOperation,
                    message: pMessage,
                    result_details: pResultDetails,
                    session_site_url: LogService.friendlySiteUrl(),
                    short_url: null,
                }),
            ],
        }

        GeneralApiService.shortenLink(linkUrlToShort).then(
            (response) => {
                if (response.data && response.data.id) {
                    bulkObject.body[1].short_url = response.data.id
                }
                return new Promise((resolve, reject) => {
                    LogService.client.bulk(
                        {
                            ...bulkObject,
                            ...UtilsService.getAccessKeyObject(),
                        },
                        (err, resp) => {
                            if (err) {
                                reject(err)
                            } else if (resp.items[0].create.status === 201) {
                                resolve(resp.items[0].create._id)
                            } else {
                                reject(resp)
                            }
                        }
                    )
                })
            },
            () => {
                return new Promise((resolve, reject) => {
                    LogService.client.bulk(
                        {
                            ...bulkObject,
                            ...UtilsService.getAccessKeyObject(),
                        },
                        (err, resp) => {
                            if (err) {
                                reject(err)
                            } else if (resp.items[0].create.status === 201) {
                                resolve(resp.items[0].create._id)
                            } else {
                                reject(resp)
                            }
                        }
                    )
                })
            }
        )
    },

    /*-----------------------------------------------*/
    /*--- log click action/event to elastic & MATOMO ----*/
    /*-----------------------------------------------*/
    logElementClick(
        id,
        className,
        entryIdVal,
        entryDbVal,
        uiActionVal,
        uiLabelVal,
        uiOrganisationVal,
        uiResourceNameVal,
        pOperation?
    ) {
        return new Promise((resolve, reject) => {
            if (LogService.elasticsearch_live) {
                if (uiOrganisationVal) {
                    uiOrganisationVal = uiOrganisationVal.replace(/\s\s+/g, '')
                }
                if (uiOrganisationVal) {
                    uiOrganisationVal = uiOrganisationVal.replace(/\s\s+/g, '')
                }

                LogService.client.bulk(
                    {
                        body: [
                            // action description
                            {
                                index: {
                                    _index: LogService.portalLogIndice,
                                    _type: LogService.portalLogType,
                                },
                            },
                            // the document to index
                            LogService.addCommonProperties({
                                ui_object_id: id,
                                ui_object_class: className,
                                ui_source: entryDbVal,
                                session_site_url: LogService.friendlySiteUrl(),
                                ui_entry_id: entryIdVal,
                                ui_action: uiActionVal,
                                ui_label: uiLabelVal, // never in use (?)
                                ui_organisation: uiOrganisationVal,
                                ui_resource_name: uiResourceNameVal,
                                operation: pOperation,
                            }),
                        ],
                        ...UtilsService.getAccessKeyObject(),
                    },
                    (err, resp) => {
                        if (err) {
                            reject(err)
                        } else if (resp.items[0].create.status === 201) {
                            resolve(resp.items[0].create._id)
                        } else {
                            reject(resp)
                        }
                    }
                )
            } else {
                resolve()
            }
        }).finally(() => {
            if (!UtilsService.isWidget()) {
                const paq = window[_PAQ]
                if (paq) {
                    paq.push([
                        'trackEvent',
                        'Click',
                        uiActionVal,
                        uiResourceNameVal,
                    ])
                }
            }
        })
    },

    /*-----------------------------------------------*/
    /*--- log events for recommendations ------------*/
    /*-----------------------------------------------*/
    logRecommendationData(eventCategory, eventAction, eventValue) {
        if (!UtilsService.isWidget()) {
            const paq = window[_PAQ]
            if (paq) {
                paq.push(['trackEvent', eventCategory, eventAction, eventValue])
            }
        }
    },

    otherFacetedParams(facetedFiltersObject) {
        let otherFacetedParams = ''
        for (const param of ['format', 'protocol', 'score']) {
            if (facetedFiltersObject[param]) {
                otherFacetedParams += `&${param}=${facetedFiltersObject[param]}`
            }
        }
        if (!otherFacetedParams) {
            return null
        } else {
            otherFacetedParams = otherFacetedParams.substring(1)
        }
        return otherFacetedParams
    },

    /*-----------------------------------------------*/
    /*--- log user sign-in to elastic  --------------*/
    /*-----------------------------------------------*/
    logSignIn() {
        return new Promise((resolve, reject) => {
            if (AppVueObj.app.$cookies.get('geoss_justSignedIn')) {
                LogService.client.bulk(
                    {
                        body: [
                            {
                                index: {
                                    _index: LogService.portalLogIndice,
                                    _type: LogService.portalLogType,
                                },
                            },
                            LogService.addCommonProperties({
                                result: 'Successfully logged in',
                                operation: 'Login attempt',
                                session_site_url: LogService.friendlySiteUrl(),
                            }),
                        ],
                        ...UtilsService.getAccessKeyObject(),
                    },
                    (err, resp) => {
                        if (err) {
                            reject(err)
                        } else if (resp.items[0].create.status === 201) {
                            resolve(resp.items[0].create._id)
                        } else {
                            reject(resp)
                        }
                    }
                )
                AppVueObj.app.$cookies.remove('geoss_justSignedIn')
            } else {
                resolve()
            }
        })
    },

    /*------------------------------------*/
    /*----- elastic log for last actions per resource entries ----------*/
    /*------------------------------------*/
    getRecentActionsLog(resultsIds: string[] | string): Promise<any[]> {
        if (typeof resultsIds === 'string') {
            resultsIds = resultsIds.split(',')
        }
        return new Promise((resolve, reject) => {
            if (LogService.elasticsearch_live) {
                // query to search all element-click-logs which: 1)refers to currently presented results and 2)contains any ui_action
                let elasticesearchSearchQuery =
                    '{"query": {"bool": {"must": {"exists": {"field": "ui_action"}}, "must_not": {"exists": {"field": "ds_reqID"}}, "should": ['
                let matchStrGlob = ''

                for (const id of resultsIds) {
                    matchStrGlob += '{"term": {"ui_entry_id": "' + id + '"}},'
                }

                elasticesearchSearchQuery += matchStrGlob
                elasticesearchSearchQuery = elasticesearchSearchQuery.replace(
                    /,\s*$/,
                    ''
                )
                elasticesearchSearchQuery += '],"minimum_should_match" : 1}}}'

                LogService.client.search(
                    {
                        size: 10000,
                        index: 'geoss_index',
                        body: elasticesearchSearchQuery,
                        ...UtilsService.getAccessKeyObject(),
                    },
                    (error, response) => {
                        if (error) {
                            reject(error)
                        } else {
                            resolve(response.hits.hits)
                        }
                    }
                )
            } else {
                resolve([])
            }
        })
    },

    getPopularWords: (queryString, limit) =>
        new Promise((resolve, reject) => {
            if (LogService.elasticsearch_live) {
                const elasticesearchSearchQuery = {
                    query: {
                        match_phrase_prefix: {
                            ds_st: {
                                query: queryString,
                            },
                        },
                    },
                    size: 0,
                    aggs: {
                        group_by_ds_st: {
                            terms: {
                                field: 'ds_st',
                                size: limit,
                            },
                        },
                    },
                }

                const elasticesearchSearchQueryString = JSON.stringify(
                    elasticesearchSearchQuery
                )
                LogService.client.search(
                    {
                        size: 0,
                        index: 'geoss_index',
                        body: elasticesearchSearchQueryString,
                        ...UtilsService.getAccessKeyObject(),
                    },
                    (error, response) => {
                        if (error) {
                            reject(error)
                        } else {
                            if (response.aggregations) {
                                resolve(
                                    response.aggregations.group_by_ds_st.buckets
                                )
                            }
                        }
                    }
                )
            } else {
                reject(new Error('Elasticsearch error.'))
            }
        }),

    getSeeAlsoWords: (queryString, limit, addMixedTerms) =>
        new Promise((resolve, reject) => {
            if (limit) {
                const relatedUrl = `${SearchEngineService.getInternalOpenSearchUrlRaw()}/api/concepts?st=${queryString}&ct=${limit}${UtilsService.getAccessKeyString()}`
                makeRequest('get', relatedUrl, null, true, null, true)
                    .then((data) => {
                        if (addMixedTerms) {
                            let queryArray = queryString.split(' ')
                            const forbiddenWords = [
                                'and',
                                'or',
                                'a',
                                'an',
                                'the',
                                'of',
                                'for',
                                'in',
                                'to',
                            ]
                            queryArray = queryArray.filter(
                                (item) =>
                                    !forbiddenWords.includes(item.toLowerCase())
                            )
                            if (
                                queryArray.length === 2 ||
                                queryArray.length === 3
                            ) {
                                data.unshift(`${queryArray.join(' AND ')}`)
                                data.unshift(`${queryArray.join(' OR ')}`)
                            }
                        }
                        resolve(data)
                    })
                    .catch((error) => {
                        reject(error)
                    })
            } else {
                reject(new Error('Related phrases are off.'))
            }
        }),

    getSeeAlsoRecommendations: (queryString) =>
        new Promise((resolve, reject) => {
            const recommendationsdUrl = `${SearchEngineService.getInternalOpenSearchUrlRaw()}/api/recommendations?st=${queryString}${UtilsService.getAccessKeyString()}`
            makeRequest('get', recommendationsdUrl, null, true, null, true)
                .then((data) => {
                    resolve(data)
                })
                .catch((error) => {
                    reject(error)
                })
        }),

    getAllSuggestions(queryString, lenList, lenRelated) {
        const promises = [
            LogService.getPopularWords(queryString, lenList).catch(
                (error) => error
            ),
            LogService.getSeeAlsoWords(queryString, lenRelated, false).catch(
                (error) => error
            ),
        ]

        return Promise.all(promises).then(([suggested, related]) => {
            return [suggested, related]
        })
    },

    /*------------------------------------*/
    /*----- elastic client init ----------*/
    /*------------------------------------*/
    createElasticSearchClient() {
        // ElasticSearch is no longer available for Widget users
        if (UtilsService.isWidget()) {
            return
        }
        let logsearchHost = `${process.env.VUE_APP_ELASTIC_SEARCH_URL}`
        if (UtilsService.isWidget()) {
            // widget users
            logsearchHost = `${BaseUrl()}/logsearch`
        } else if (window.location.origin.includes('//localhost')) {
            // localhost development (standalone FE and Liferay version)
            logsearchHost = 'https://geoss.devel.esaportal.eu/logsearch'
        } else if (process.env.VUE_APP_ELASTIC_SEARCH_URL.startsWith('/')) {
            // production packages (dev, uat, sit, prod)
            logsearchHost = `${window.location.origin}${process.env.VUE_APP_ELASTIC_SEARCH_URL}`
        }

        return new Promise((resolve) => {
            LogService.client = new es.Client({
                hosts: logsearchHost,
            })
            LogService.client.ping(
                {
                    requestTimeout: 10000,

                    // undocumented params are appended to the query string
                    hello: 'elasticsearch',
                    ...UtilsService.getAccessKeyObject(),
                },
                (error) => {
                    LogService.elasticsearch_live = !error
                    resolve()
                }
            )
        })
    },

    dateInUTCFullYear() {
        const d = new Date()

        const month = d.getUTCMonth() + 1
        const day = d.getUTCDate()
        const hour = d.getUTCHours()
        const minute = d.getUTCMinutes()
        const second = d.getUTCSeconds()
        const milisecond = d.getUTCMilliseconds()

        return (
            d.getUTCFullYear() +
            '-' +
            ('' + month).padStart(2, '0') +
            '-' +
            ('' + day).padStart(2, '0') +
            ' ' +
            ('' + hour).padStart(2, '0') +
            ':' +
            ('' + minute).padStart(2, '0') +
            ':' +
            ('' + second).padStart(2, '0') +
            '.' +
            ('' + milisecond).padStart(3, '0')
        )
    },

    addCommonProperties(document) {
        // session properties
        document.session_login =
            typeof Liferay !== 'undefined'
                ? Liferay.ThemeDisplay.getUserId()
                : null
        document.session_user_email =
            typeof Liferay !== 'undefined' && Liferay.ThemeDisplay.isSignedIn()
                ? AppVueObj.app.$cookies.get('geoss_email')
                : null
        document.session_id =
            typeof Liferay !== 'undefined'
                ? AppVueObj.app.$cookies.get(
                      'LFR_SESSION_STATE_' + Liferay.ThemeDisplay.getUserId()
                  )
                : null
        document.session_timestamp = Date.now()
        document.session_date = LogService.dateInUTCFullYear()

        // user agent properties
        document.ua = LogService.userAgent.ua
        document.ua_browser_name = LogService.userAgent.browser.name
        document.ua_browser_version = LogService.userAgent.browser.version
        document.ua_engine_name = LogService.userAgent.engine.name
        document.ua_engine_version = LogService.userAgent.engine.version
        document.ua_os_name = LogService.userAgent.os.name
        document.ua_os_version = LogService.userAgent.os.version
        document.ua_device_model = LogService.userAgent.device.model
        document.ua_device_vendor = LogService.userAgent.device.vendor
        document.ua_device_type = LogService.userAgent.device.type
        document.ua_cpu_architecture = LogService.userAgent.cpu.architecture

        // other properties
        document.win_width = window.innerWidth
        document.win_height = window.innerHeight

        return document
    },
}

export default LogService
