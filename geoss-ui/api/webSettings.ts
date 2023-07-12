import apiClient from './apiClient'
import geossSettings from './module/geoss-settings'
import { parseXMLToJSON } from '@/services/general.api.service'
import SpinnerService from '@/services/spinner.service'
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

interface WebSettingsData {
    [key: string]: string
}

interface SiteSettings {
    name: string
    defaultDataSource: string
    url: string
    logoUrl: string
    mapZoom: number
    latitude: number
    longitude: number
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
    localization?: any
    searchEngine: Array<SearchEngine>
    searchEngineConfigs: Array<SearchEngineConfig>
    searchFilterParams?: any
    linkSharing?: {
        geossSearchPlid: number
        searchEngineLayers: any
    }
    popularSearch?: {
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

export interface IWebSettingData {
    set: string
    key: string
    value: string
}

const parseWebSettings = (data: WebSettings): WebSettingsData => {
    const webSettingsData: WebSettingsData = {}
    for (const setting of data._embedded.webSettings) {
        const key: string = `${setting.set}_${setting.key}`
        webSettingsData[key] = setting.value
    }
    return webSettingsData
}

const parseApiSettings = (data: any): any => {
    const apiSettingsData: any = {}
    for (const setting of data._embedded.apiSettings) {
        const key: string = `${setting.key}`
        apiSettingsData[key] = setting.value
    }
    return apiSettingsData
}

const parseSiteSettings = (data: WebSettingsData): SiteSettings => {
    return {
        name: data.logo_title,
        defaultDataSource: data.source_GEOSS,
        url: '/',
        logoUrl: data.logo_source,
        mapZoom: Number(data.map_zoom),
        latitude: Number(data.map_latitude),
        longitude: Number(data.map_longitude),
    }
}

const parseCatalogsResponse = (data: string): any => {
    const catalogsObject = JSON.parse(parseXMLToJSON(data))
    const catalogsArray = catalogsObject.feed.entry
    const catalogs = []

    for (const cat of catalogsArray) {
        catalogs.push({
            defaultOption: false,
            subOptions: [],
            label: cat.title,
            title: cat.title,
            value: cat.id,
        })
    }
    return catalogs
}

export default {
    getSiteSettingsRaw: async () => {
        const webSettings: WebSettings = await apiClient.$get(
            geossSettings.webSettings,
            {
                headers: {
                    Authorization: '',
                }
            }
        )
        return webSettings._embedded.webSettings
    },
    getSiteSettings: async () => {
        const webSettings: WebSettings = await apiClient.$get(
            geossSettings.webSettings,
            {
                headers: {
                    Authorization: '',
                }
            }
        )
        const webSettingsData: WebSettingsData = parseWebSettings(webSettings)
        return parseSiteSettings(webSettingsData)
    },
    getSearchSettings: async () => {
        const apiSettings: WebSettings = await apiClient.$get(
            geossSettings.apiSettings,
            {
                headers: {
                    Authorization: '',
                }
            }
        )
        const apiSettingsData: any = parseApiSettings(apiSettings)
        return apiSettingsData
    },
    getCatalogs: async (catalogsUrl: string) => {
        if (process.browser) {
            const catalogsResponse: any = await apiClient.$get(catalogsUrl, {
                headers: {
                    Authorization: '',
                }
            })
            return parseCatalogsResponse(catalogsResponse)
        }
        return []
    },
    getViews: async () => {
        const views: any = await apiClient.$get(geossSettings.views, {
            headers: {
                Authorization: '',
            }
        })
        return views._embedded.views
    },
    getDataProviders: async (dataProvidersUrl: string) => {
        if (process.browser) {
            SpinnerService.showSpinner()
            const dataProvidersResponse: any = await apiClient.$get(
                dataProvidersUrl,
                {
                    headers: {
                        Authorization: '',
                    }
                }
            )
            SpinnerService.hideSpinner()
            return dataProvidersResponse
        }
    },
    setSiteSetting: async (id: number, webSettingData: IWebSettingData, token: any = null) => {
        let method = '$post'
        let url = `${geossSettings.webSettings}`
        if (id) {
            method = '$put'
            url += `/${id}`
        }
        return apiClient[method](url, JSON.stringify(webSettingData), {
            headers: {
                'Content-Type': 'application/json',
                Authorization: token ? token : '',
            },
        })
    },
    setView: async (view: any, token: any = null) => {
        return apiClient.$post(`${geossSettings.views}`, JSON.stringify(view), {
            headers: {
                'Content-Type': 'application/json',
                Authorization: token ? token : '',
            },
        })
    },
    updateView: async (id: number, view: any, token: any = null) => {
        return apiClient.$patch(
            `${geossSettings.views}/${id}`,
            JSON.stringify(view),
            {
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: token ? token : '',
                },
            }
        )
    },
}
