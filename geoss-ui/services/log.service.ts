import es from 'elasticsearch'
import { Liferay, AppVueObj, BaseUrl } from '~/data/global'
import UAParser from 'ua-parser-js'
import { GeneralFiltersGetters } from '~/store/generalFilters/general-filters-getters'
import { GranulaFiltersGetters } from '~/store/granulaFilters/granula-filters-getters'
import { IrisFiltersGetters } from '~/store/irisFilters/iris-filters-getters'
import { MyWorkspaceGetters } from '~/store/myWorkspace/my-workspace-getters'
import { SearchGetters } from '@/store/search/search-getters'
import { FacetedFiltersGetters } from '~/store/facetedFilters/faceted-filters-getters'
import SearchEngineService from '@/services/search-engine.service'
import UtilsService from '@/services/utils.service'
import { MapCoordinate } from '@/interfaces/MapCoordinate'
import { makeRequest, GeneralApiService } from './general.api.service'

const _PAQ: string = '_paq'

const LogService: any = {
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
        pOperation: string,
        id?: string,
        className?: string,
        entryIdVal?: string,
        entryDbVal?: string,
        uiActionVal?: string
    ) {
        let queryUrl = null
        if (pOperation === 'search_query') {
            queryUrl = SearchEngineService.getDabOpenSearchUrl() + '?'
            const filtersParams =
                AppVueObj.app.$store.getters[SearchGetters.filtersParams]
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

        const body = LogService.addCommonProperties({
            operation: pOperation,
            uiObjectId: id,
            uiObjectClass: className,
            sessionSiteUrl: LogService.friendlySiteUrl(),
            uiSource:
                pOperation === 'search_query' &&
                AppVueObj.app.$store.getters[GeneralFiltersGetters.state]
                    .requestId
                    ? 'dab'
                    : entryDbVal,
            uiEntryid: entryIdVal,
            uiAction: uiActionVal,
            dsQueryUrl: queryUrl,
            dsReqId:
                AppVueObj.app.$store.getters[GeneralFiltersGetters.state]
                    .requestId,
            dsSi:
                pOperation === 'search_query' &&
                AppVueObj.app.$store.getters[SearchGetters.startIndex]
                    ? AppVueObj.app.$store.getters[
                          SearchGetters.startIndex
                      ].toString()
                    : null,
            dsCt:
                pOperation === 'search_query' &&
                AppVueObj.app.$store.getters[GeneralFiltersGetters.state]
                    ? AppVueObj.app.$store.getters[
                          GeneralFiltersGetters.state
                      ].resultsPerPage.toString()
                    : null,
            dsTs: AppVueObj.app.$store.getters[GeneralFiltersGetters.state]
                .dateFrom,
            dsTe: AppVueObj.app.$store.getters[GeneralFiltersGetters.state]
                .dateTo,

            dsSt: AppVueObj.app.$store.getters[GeneralFiltersGetters.state]
                .phrase,
            dsRel: AppVueObj.app.$store.getters[GeneralFiltersGetters.state]
                .boundingBoxRelation,
            dsKwd:
                pOperation === 'search_query'
                    ? AppVueObj.app.$store.getters[
                          FacetedFiltersGetters.keyword
                      ]
                    : null,
            dsParentsGroup: {
                key: AppVueObj.app.$store.getters[MyWorkspaceGetters.search]
                    .folder
                    ? AppVueObj.app.$store.getters[MyWorkspaceGetters.search]
                          .folder.value
                    : '',
                value: AppVueObj.app.$store.getters[MyWorkspaceGetters.search]
                    .folder
                    ? AppVueObj.app.$store.getters[MyWorkspaceGetters.search]
                          .folder.label
                    : '',
            },
            dsSba: null,
            dsSourcesGroup: AppVueObj.app.$store.getters[
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
            dsViewsGroup: AppVueObj.app.$store.getters[
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
            dsBbox: bbox,
            dsGdc: AppVueObj.app.$store.getters[GeneralFiltersGetters.state]
                .geossDataCore,
            dsW3w: null,
            dsFrmt:
                pOperation === 'search_query'
                    ? AppVueObj.app.$store.getters[FacetedFiltersGetters.format]
                    : null,
            dsProt:
                pOperation === 'search_query'
                    ? AppVueObj.app.$store.getters[
                          FacetedFiltersGetters.protocol
                      ]
                    : null,
            dsScore:
                pOperation === 'search_query'
                    ? AppVueObj.app.$store.getters[FacetedFiltersGetters.score]
                    : null,
        })
        fetch('https://gpp.devel.esaportal.eu/proxy/rest/log/logSearchResult', {
            method: 'POST',
            headers: {
                Authorization:
                    'Bearer ' + window.$nuxt.$cookies.get('elkAuthToken'),
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(body),
        })

        if (!UtilsService.isWidget()) {
            const paq: Array<any> = (<any>window)[_PAQ]
            if (paq) {
                paq.push(['trackEvent', 'Search', 'phrase', entryIdVal])
            }
        }
    },

    /*-----------------------------------------------*/
    /*--- log click action/event to elastic & MATOMO ----*/
    /*-----------------------------------------------*/
    logElementClick(
        id: string,
        className: string,
        entryIdVal: string,
        entryDbVal: string,
        uiActionVal: string,
        uiLabelVal: string,
        uiOrganisationVal: string,
        uiResourceNameVal: string,
        pOperation?: string
    ) {
        if (uiOrganisationVal) {
            uiOrganisationVal = uiOrganisationVal.replace(/\s\s+/g, '')
        }

        const body = LogService.addCommonProperties({
            uiObjectId: id,
            uiObjectClass: className,
            uiSource: entryDbVal,
            sessionSiteUrl: LogService.friendlySiteUrl(),
            uiEntryId: entryIdVal,
            uiAction: uiActionVal,
            uiLabel: uiLabelVal,
            uiOrganisation: uiOrganisationVal,
            uiResourceName: uiResourceNameVal,
            operation: pOperation,
        })

        fetch('https://gpp.devel.esaportal.eu/proxy/rest/log/logElementClick', {
            method: 'POST',
            headers: {
                Authorization:
                    'Bearer ' + window.$nuxt.$cookies.get('elkAuthToken'),
                'Content-Type': 'application/json',
                'accept': 'application/json'
            },
            body: JSON.stringify(body),
        })

        if (!UtilsService.isWidget()) {
            const paq: Array<any> = (<any>window)[_PAQ]
            if (paq) {
                paq.push([
                    'trackEvent',
                    'Click',
                    uiActionVal,
                    uiResourceNameVal,
                ])
            }
        }
    },

    /*-----------------------------------------------*/
    /*--- log error to elastic  -----------------*/
    /*-----------------------------------------------*/
    logResourceError(
        result: any,
        pMessage: string,
        pOperation: string,
        pResult: string,
        pResultDetails: string
    ) {
        const body = LogService.addCommonProperties({
            uiEntryId: result ? result.id : '',
            result: pResult,
            operation: pOperation,
            message: pMessage,
            resultDetails: pResultDetails,
            sessionSiteUrl: LogService.friendlySiteUrl(),
            shortUrl: 'test',
        })

        fetch(
            'https://gpp.devel.esaportal.eu/proxy/rest/log/logResourceError',
            {
                method: 'POST',
                headers: {
                    Authorization:
                        'Bearer ' + window.$nuxt.$cookies.get('elkAuthToken'),
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(body),
            }
        )
    },

    /*-----------------------------------------------*/
    /*--- log events for recommendations ------------*/
    /*-----------------------------------------------*/
    logRecommendationData(
        eventCategory: string,
        eventAction: string,
        eventValue: string
    ) {
        if (!UtilsService.isWidget()) {
            const paq: Array<any> = (<any>window)[_PAQ]
            if (paq) {
                paq.push(['trackEvent', eventCategory, eventAction, eventValue])
            }
        }
    },

    /*-----------------------------------------------*/
    /*--- log user sign-in to elastic  --------------*/
    /*-----------------------------------------------*/
    logSignIn() {
        // if(window.$nuxt.$cookies.get('geoss_justSignedIn'))
        const body = LogService.addCommonProperties({
            sessionSiteUrl: LogService.friendlySiteUrl(),
        })
        fetch('https://gpp.devel.esaportal.eu/proxy/rest/log/logSignIn', {
            method: 'POST',
            headers: {
                Authorization:
                    'Bearer ' + window.$nuxt.$cookies.get('elkAuthToken'),
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(body),
        })
    },

    addCommonProperties(document: any) {
        document.commonProperties = {
            sessionProperties: {
                sessionLogin:
                    typeof Liferay !== 'undefined'
                        ? Liferay.ThemeDisplay.getUserId().toString()
                        : null,
                sessionId:
                    typeof Liferay !== 'undefined'
                        ? window.$nuxt.$cookies.get(
                              'LFR_SESSION_STATE_' +
                                  Liferay.ThemeDisplay.getUserId().toString()
                          )
                        : null,
                sessionTimestamp: Date.now(),
                sessionDate: new Date().toISOString(),
            },
            userAgentProperties: {
                ua: LogService.userAgent.ua,
                uaBrowserName: LogService.userAgent.browser.name,
                uaBrowserVersion: LogService.userAgent.browser.version,
                uaEngineName: LogService.userAgent.engine.name,
                uaEngineVersion: LogService.userAgent.engine.version,
                uaOsName: LogService.userAgent.os.name,
                uaOsVersion: LogService.userAgent.os.version,
                uaDeviceModel: LogService.userAgent.device.model,
                uaDeviceVendor: LogService.userAgent.device.vendor,
                uaDeviceType: LogService.userAgent.device.type,
                uaCpuArchitecture: LogService.userAgent.cpu.architecture,
            },
            otherProperties: {
                winWidth: window.innerWidth,
                winHeight: window.innerHeight,
            },
        }
        return document
    },

    /*------------------------------------*/
    /*-----  LEGACY ----------*/
    /*------------------------------------*/

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
                    (error: any, response: any) => {
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

    getPopularWords: (queryString: string, limit: string | number) =>
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
                    (error: any, response: any) => {
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

    getSeeAlsoWords: (
        queryString: string,
        limit: string | number,
        addMixedTerms: boolean
    ) => {
        return
    },
    // new Promise((resolve, reject) => {
    //     if (limit) {
    //         const relatedUrl = `${SearchEngineService.getInternalOpenSearchUrlRaw()}/api/concepts?st=${queryString}&ct=${limit}${UtilsService.getAccessKeyString()}`
    //         makeRequest('get', relatedUrl, null, true, null, true)
    //             .then((data: Array<any>) => {
    //                 if (addMixedTerms) {
    //                     let queryArray = queryString.split(' ')
    //                     const forbiddenWords = [
    //                         'and',
    //                         'or',
    //                         'a',
    //                         'an',
    //                         'the',
    //                         'of',
    //                         'for',
    //                         'in',
    //                         'to',
    //                     ]
    //                     queryArray = queryArray.filter(
    //                         (item) =>
    //                             !forbiddenWords.includes(item.toLowerCase())
    //                     )
    //                     if (
    //                         queryArray.length === 2 ||
    //                         queryArray.length === 3
    //                     ) {
    //                         data.unshift(`${queryArray.join(' AND ')}`)
    //                         data.unshift(`${queryArray.join(' OR ')}`)
    //                     }
    //                 }
    //                 resolve(data)
    //             })
    //             .catch((error: any) => {
    //                 reject(error)
    //             })
    //     } else {
    //         reject(new Error('Related phrases are off.'))
    //     }
    // }),

    getSeeAlsoRecommendations: (queryString: string) =>
        new Promise((resolve, reject) => {
            const recommendationsdUrl = `${SearchEngineService.getInternalOpenSearchUrlRaw()}/api/recommendations?st=${queryString}${UtilsService.getAccessKeyString()}`
            makeRequest('get', recommendationsdUrl, null, true, null, true)
                .then((data: any) => {
                    resolve(data)
                })
                .catch((error: any) => {
                    reject(error)
                })
        }),

    async getAllSuggestions(
        queryString: string,
        limit: string | number,
    ) {

        let response = await fetch(
            `https://gpp.devel.esaportal.eu/proxy/rest/popular?query=${queryString}&limit=${limit}`,
            {
                headers: {
                    accept: 'application/json'
                },
            }
        )

        let rJson = await response.json()

        return rJson

        // const promises = [
        //     LogService.getPopularWords(queryString, lenList).catch(
        //         (error: any) => error
        //     ),
        //     LogService.getSeeAlsoWords(queryString, lenRelated, false).catch(
        //         (error: any) => error
        //     ),
        // ]

        // return Promise.all(promises).then(([suggested, related]) => {
        //     return [suggested, related]
        // })
    },

    /*------------------------------------*/
    /*----- elastic client init ----------*/
    /*------------------------------------*/
    createElasticSearchClient() {
        return
        // ElasticSearch is no longer available for Widget users
        if (UtilsService.isWidget()) {
            return
        }
        let logsearchHost = `${process.env.NUXT_ENV_ELASTIC_SEARCH_URL}`
        if (UtilsService.isWidget()) {
            // widget users
            logsearchHost = `${BaseUrl()}/logsearch`
        } else if (window.location.origin.includes('//localhost')) {
            // localhost development (standalone FE and Liferay version)
            logsearchHost = 'https://geoss.devel.esaportal.eu/logsearch'
        } else if (process.env.NUXT_ENV_ELASTIC_SEARCH_URL!.startsWith('/')) {
            // production packages (dev, uat, sit, prod)
            logsearchHost = `${window.location.origin}${process.env.NUXT_ENV_ELASTIC_SEARCH_URL}`
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
                (error: any) => {
                    LogService.elasticsearch_live = !error
                    resolve
                }
            )
        })
    },
}

export default LogService
