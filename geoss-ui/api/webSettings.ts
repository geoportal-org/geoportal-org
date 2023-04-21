import apiClient from './apiClient'
import geossSettings from './module/geoss-settings'

interface WebSetting {
    id: number
    set: string
    key: string
    value: string
    created_by: string
    created_on: string
    modified_by: string
    modified_on: string
    _links: {
        self: {
            href: string
        }
        webSetting: {
            href: string
        }
    }
}

interface WebSettings {
    _embedded: {
        [key: string]: Array<WebSetting>
    }
    _links: {
        profile: {
            href: string
        }
        search: {
            href: string
        }
        self: {
            href: string
        }
    }
    page: {
        number: number
        size: number
        totalElements: number
        totalPages: number
    }
}

interface SiteSettings {
    site: {
        name: string
        defaultDataSource: string
        url: string
        logoUrl: string
    }
}

interface SearchEngine {
    groupId: number
    latitude: number
    mapConf: boolean
    contextPath: string
    name: string
    dabDataProvidersUrl: string
    mapZoom: number
    defaultEngine: boolean
    searchEngineId: string
    dabBaseUrl: string
    longitude: number
}

interface SearchEngineConfig {
    searchEngineConfigId: number
    name: string
    value: string
    searchEngineId: number
    group: string
}

interface SearchSettings {
    localization: any
    searchEngine: Array<SearchEngine>
    searchEngineConfigs: Array<SearchEngineConfig>
    searchFilterParams: any
    linkSharing: {
        geossSearchPlid: number
        searchEngineLayers: any
    }
    popularSearch: {
        groupId: number
        aoiRelation: string
        showAoi: boolean
        datePeriod: string
        cloudCoverageTo: number
        showDate: boolean
        enabled: boolean
        searchEngineId: number
        aoiGeolocation: string
        aoiW3W: string
        id: number
        currMap: string
        cloudCoverageFrom: number
        defaultSearch: boolean
        queryOptions: string
        query: string
        aoiBoundingBox: string
        dateFrom: string
        showClaudCoverage: boolean
        showFullAndOpenDataset: boolean
        folder: string
        fullAndOpenDataset: boolean
        dateTo: string
        name: string
        aoiOption: string
    }
}

interface WebSettingsData {
    [key: string]: string
}

const parseWebSettings = (data: WebSettings): WebSettingsData => {
    const webSettingsData: WebSettingsData = {}
    for (const setting of data._embedded.webSettings) {
        const key: string = `${setting.set}_${setting.key}`
        webSettingsData[key] = setting.value
    }
    return webSettingsData
}

const parseSiteSettings = (data: WebSettingsData): SiteSettings => {
    return {
        site: {
            name: data.logo_title,
            defaultDataSource: data.source_GEOSS,
            url: '/',
            logoUrl: data.logo_source,
        },
    }
}

// const parseSearchSettings = (data: WebSettingsData): SearchSettings => {
//     return {
//         searchEngine: [],
//         searchEngineConfigs: [],
//     }
// }

export default {
    getSiteSettings: async () => {
        const webSettings: WebSettings = await apiClient.$get(
            geossSettings.webSettings
        )
        const webSettingsData: WebSettingsData = parseWebSettings(webSettings)
        return parseSiteSettings(webSettingsData)
    },
    getSearchSettings: async () => {
        return {}
    },
}
