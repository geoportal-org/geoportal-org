import { SearchEngineGetters } from '~/store/searchEngine/search-engine-getters'
import { MyWorkspaceGetters } from '~/store/myWorkspace/my-workspace-getters'
import { BaseUrl, AppVueObj, Liferay } from '~/data/global'
import { GeneralFiltersGetters } from '~/store/generalFilters/general-filters-getters'
import { FacetedFiltersGetters } from '~/store/facetedFilters/faceted-filters-getters'
import { GranulaFiltersGetters } from '~/store/granulaFilters/granula-filters-getters'
import { IrisFiltersGetters } from '~/store/irisFilters/iris-filters-getters'
import { MapGetters } from '@/store/map/map-getters'
import { SearchGetters } from '@/store/search/search-getters'
import UtilsService from './utils.service'
import { DataOrigin, DataSource } from '@/interfaces/DataSources'

const SearchEngineService = {
    getDabOpenSearchUrl() {
        if (UtilsService.isWidget()) {
            return '/geossWidget/dab-url/opensearch/query'
        }
        return SearchEngineService.getDabBaseUrl() + '/opensearch/query'
    },

    getInternalOpenSearchUrl() {
        if (UtilsService.isWidget()) {
            return '/geossWidget/geoss-cr-url/opensearch'
        }
        return (
            AppVueObj.app.$store.getters[
                SearchEngineGetters.internalOpenSearchUrl
            ] + '/opensearch'
        )
    },

    getInternalOpenSearchUrlRaw() {
        if (UtilsService.isWidget()) {
            return '/geossWidget/geoss-cr-url'
        }
        return AppVueObj.app.$store.getters[
            SearchEngineGetters.internalOpenSearchUrl
        ]
    },

    getNextgeossOpenSearchUrl() {
        if (UtilsService.isWidget()) {
            return '/geossWidget/nextgeoss/opensearch/search.atom'
        }
        return 'https://catalogue.nextgeoss.eu/opensearch/search.atom'
    },

    getServiceZenodoUrl() {
        // The same for portal and widget users as Zenodo request cannot be proxied (internal Zenodo's mechanism)
        return 'https://zenodo.org/api/records/'
    },

    getBulkDownloadUrl() {
        return SearchEngineService.getDabBaseUrl() + '/gwps'
    },

    getMainSiteGroupId() {
        return '20181'
    },

    getSearchUrl() {
        return {
            dab: SearchEngineService.getDabOpenSearchUrl(),
            data: SearchEngineService.getInternalOpenSearchUrl(),
            amerigeoss: SearchEngineService.getInternalOpenSearchUrl(),
            nextgeoss: SearchEngineService.getNextgeossOpenSearchUrl(),
            information: SearchEngineService.getInternalOpenSearchUrl(),
            zenodo: SearchEngineService.getServiceZenodoUrl(),
            wikipedia: SearchEngineService.getInternalOpenSearchUrl(),
            services: SearchEngineService.getInternalOpenSearchUrl(),
        }
    },

    getConfigValue(settings: any[], paramName: string) {
        const param = settings.find((item) => item.name === paramName)
        if (param) {
            return param.value
        }
        return null
    },

    getResourceUrl(resourceId: string, params: any) {
        const prefix = BaseUrl()
        let friendlySiteUrl = 'guest'
        let urlLoc = BaseUrl() + '/home'
        const webPart = '/community/'
        const webPos = urlLoc.indexOf(webPart)
        if (webPos > -1) {
            urlLoc = urlLoc.slice(webPos + webPart.length)
            const arrUrl = urlLoc.split('/')
            friendlySiteUrl = arrUrl[0]
        }

        let url =
            prefix +
            '/community/' +
            friendlySiteUrl +
            '/geoss-resources?p_p_id=geossresources_WAR_geossportlet&p_p_lifecycle=2&' +
            'p_p_state=normal&p_p_mode=view&p_p_cacheability=cacheLevelPage&controlPanelCategory=current_site.configuration' +
            '&p_p_resource_id=' +
            resourceId

        if (params) {
            Object.keys(params).forEach((key) => {
                // escape value to be safe for URL and HTML attribute.
                const value = encodeURIComponent(params[key]).replace(
                    /'/g,
                    '%27'
                )
                url += '&_geossresources_WAR_geossportlet_' + key + '=' + value
            })
        }
        return url
    },

    getLayerKmlUrl(originalKmlFileUrl: string) {
        if (!originalKmlFileUrl.startsWith('http')) {
            originalKmlFileUrl = window.location.origin + originalKmlFileUrl
        }
        return SearchEngineService.getResourceUrl('LAYER_KML', {
            url: originalKmlFileUrl,
        })
    },

    getLayerKmzUrl(originalKmzFileUrl: string) {
        if (!originalKmzFileUrl.startsWith('http')) {
            originalKmzFileUrl = window.location.origin + originalKmzFileUrl
        }
        return SearchEngineService.getResourceUrl('LAYER_KMZ', {
            url: originalKmzFileUrl,
        })
    },

    getLegendAccessibilityUrl(url: string) {
        return SearchEngineService.getResourceUrl('LEGEND_ACCESSIBILITY', {
            url,
        })
    },

    getSurveyUrl(form: any) {
        return SearchEngineService.getResourceUrl('SURVEY', {
            impression: form.impression,
            did_found_what_looking_for: form.did_found_what_looking_for,
            what_looking_for: form.what_looking_for,
            interest: form.interest,
            classification: form.classification,
            from: form.from,
            organized: form.organized,
            adequately: form.adequately,
            search_criteria: form.search_criteria,
            visualization: form.visualization,
        })
    },

    getBookmarkedFeedUrl(targetIds: string) {
        return SearchEngineService.getResourceUrl('BOOKMARKED_FEED', {
            targetIds,
        })
    },

    getLinkStatusUrl(urlToCheck: string) {
        return SearchEngineService.getResourceUrl('LINK_STATUS', { urlToCheck })
    },

    getMetaDataUrl(targetId: string) {
        return SearchEngineService.getResourceUrl('META_DATA', { targetId })
    },

    getCheckLayerFileUrl(urlToCheck: string, type: string) {
        return SearchEngineService.getResourceUrl('CHECK_LAYER_FILE', {
            url: urlToCheck,
            type,
        })
    },

    getCatalogsAndViewsUrl() {
        return SearchEngineService.getResourceUrl('SEARCH_QUERY_PARAMS', {
            popularSearchId:
                AppVueObj.app.$store.getters[MyWorkspaceGetters.search]
                    .popularSearchId,
        })
    },

    getDhusProxyUrl(dhusResourceUrl: string) {
        return SearchEngineService.getResourceUrl('DHUS_PROXY', {
            url: encodeURI(dhusResourceUrl),
        })
    },

    // targetIds - comma separated values
    getGetRatingsUrl(targetIds: string) {
        return SearchEngineService.getResourceUrl('GET_RATINGS', { targetIds })
    },

    getUpdateRatingUrl(
        targetId: string,
        name: string,
        score: string,
        comment: string
    ) {
        return SearchEngineService.getResourceUrl('UPDATE_RATING', {
            targetId,
            name,
            score,
            comment,
        })
    },

    getGetCommentsUrl(targetId: string) {
        return SearchEngineService.getResourceUrl('GET_COMMENTS', { targetId })
    },

    getDabBaseUrl() {
        let baseUrl = ''
        if (AppVueObj.app.$store.getters[SearchEngineGetters.userDabBaseUrl]) {
            baseUrl =
                AppVueObj.app.$store.getters[SearchEngineGetters.userDabBaseUrl]
        } else if (
            AppVueObj.app.$store.getters[SearchEngineGetters.dabBaseUrlConf]
        ) {
            baseUrl =
                AppVueObj.app.$store.getters[SearchEngineGetters.dabBaseUrlConf]
        } else {
            baseUrl =
                AppVueObj.app.$store.getters[SearchEngineGetters.dabBaseUrl]
        }
        // Make sure trailing slash is removed from the end
        return baseUrl?.replace(/\/+$/, '')
    },

    getKpBaseUrl() {
        return AppVueObj.app.$store.getters[SearchEngineGetters.kpBaseUrl]
    },

    getStatisticAuthUrl() {
        return SearchEngineService.getResourceUrl('STATISTIC_AUTH', {})
    },

    getShareUrl(targetId?: string | null, checkTargetIdUrlParam?: boolean) {
        const params = window.location.search
            .substr(1)
            .split('&')
            .filter((item) => item)
        let targetIdParam = null
        if (checkTargetIdUrlParam) {
            for (const param of params) {
                const paramName = param.split('=')[0]
                const paramValue: any = decodeURIComponent(param.split('=')[1])
                if (paramName === 'targetId') {
                    targetIdParam = paramValue
                    break
                }
            }
        }
        let linkurl
        if (UtilsService.isWidget()) {
            linkurl = BaseUrl() + '?'
        } else {
            linkurl =
                window.location.protocol +
                '//' +
                window.location.host +
                window.location.pathname +
                '?'
        }

        if (!targetId && !targetIdParam) {
            linkurl +=
                AppVueObj.app.$store.getters[GeneralFiltersGetters.shareParams]
            linkurl +=
                AppVueObj.app.$store.getters[FacetedFiltersGetters.shareParams]
            linkurl +=
                AppVueObj.app.$store.getters[GranulaFiltersGetters.shareParams]
            linkurl +=
                AppVueObj.app.$store.getters[IrisFiltersGetters.shareParams]
            // linkurl += AppVueObj.app.$store.getters[MyWorkspaceGetters.shareParams];
            linkurl += AppVueObj.app.$store.getters[MapGetters.shareParams]
            linkurl += AppVueObj.app.$store.getters[SearchGetters.shareParams]
        } else {
            linkurl += AppVueObj.app.$store.getters[MapGetters.shareParams]
            linkurl += `targetId=${encodeURIComponent(
                targetId || targetIdParam
            )}`
            linkurl += `&f:phrase=${encodeURIComponent(
                AppVueObj.app.$store.getters[GeneralFiltersGetters.state].phrase
            )}`
            linkurl += `&f:dataSource=${encodeURIComponent(
                AppVueObj.app.$store.getters[SearchGetters.dataSource]
            )}`
        }

        if (AppVueObj.app.$store.getters[SearchGetters.crRelation]) {
            const relationType =
                AppVueObj.app.$store.getters[SearchGetters.crRelation]
                    .relationType
            const srcEntry =
                AppVueObj.app.$store.getters[SearchGetters.crRelation].srcEntry
            linkurl += `crRelation=${encodeURIComponent(relationType)}`
            if (srcEntry && srcEntry.id && srcEntry.dataSource) {
                linkurl += `&crRelationSrcId=${encodeURIComponent(srcEntry.id)}`
                linkurl += `&crRelationSrcDs=${encodeURIComponent(
                    DataOrigin[srcEntry.dataSource as DataSource]
                )}`
            }
        }

        if (linkurl[linkurl.length - 1] === '?') {
            linkurl = linkurl.substr(0, linkurl.length - 1)
        }

        if (linkurl[linkurl.length - 1] === '&') {
            linkurl = linkurl.substr(0, linkurl.length - 1)
        }
        return linkurl
    },

    // Returns url in format accepted by Liferay's redirect parameter
    getRedirectUrl(targetId: string, action?: string) {
        let linkurl = '/?'
        linkurl += AppVueObj.app.$store.getters[MapGetters.shareParams]
        linkurl += `targetId=${targetId}`
        linkurl += `&f:phrase=${
            AppVueObj.app.$store.getters[GeneralFiltersGetters.state].phrase
        }`
        linkurl += `&f:dataSource=${
            AppVueObj.app.$store.getters[SearchGetters.dataSource]
        }`
        if (action) {
            linkurl += `&trigger=${action}`
        }

        return encodeURIComponent(linkurl)
    },

    stripQuotationMarks(query: string) {
        const fullQuery = query.trim()
        return fullQuery.replace(/"/g, '')
    },

    getUserSettingsUrl() {
        const geossVersion = window.$nuxt.$cookies.get('geoss_version')
        const splashscreenTooltips = window.$nuxt.$cookies.get(
            'splashscreen_tooltips'
        )
        const dhusUsername = window.$nuxt.$cookies.get('dhus_username')
        const dhusPassword = window.$nuxt.$cookies.get('dhus_password')

        return SearchEngineService.getResourceUrl('USER_SETTINGS', {
            geoss_version: geossVersion,
            splashscreen_tooltips: splashscreenTooltips,
            dhus_username: dhusUsername,
            dhus_password: dhusPassword,
        })
    },

    getSiteSettingsUrl() {
        const groupId =
            !UtilsService.isWidget() && typeof Liferay !== 'undefined'
                ? Liferay.ThemeDisplay.getScopeGroupId()
                : SearchEngineService.getMainSiteGroupId()
        return SearchEngineService.getResourceUrl('SITE_SETTINGS', { groupId })
    },

    getSearchSettingsUrl() {
        const savedSearchId = UtilsService.getUrlParam('savedSearchId')
        const popularSearchId = UtilsService.getUrlParam('popularSearchId')
        const groupId =
            !UtilsService.isWidget() && typeof Liferay !== 'undefined'
                ? Liferay.ThemeDisplay.getScopeGroupId()
                : SearchEngineService.getMainSiteGroupId()

        return SearchEngineService.getResourceUrl('SEARCH_SETTINGS', {
            groupId,
            savedSearchId,
            popularSearchId,
        })
    },

    getDabProvidersUrl() {
        return SearchEngineService.getResourceUrl('DATA_PROVIDERS', {})
    },
}

export default SearchEngineService
