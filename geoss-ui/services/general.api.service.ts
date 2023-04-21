import axios from 'axios'
import xmljs from 'xml-js'
import axiosCancel from 'axios-cancel'
import { Liferay, BaseUrl } from '~/data/global'
import SpinnerService from './spinner.service'
import UtilsService from './utils.service'
import {
    cancellableRequests,
    CancelCurrentrequest,
} from './geoss-search.api.service'
import SearchEngineService from './search-engine.service'
import webSettingsAPI from '@/api/webSettings'

axiosCancel(axios)

const removeJsonTextAttribute = (
    value: any,
    parentElement: { [x: string]: any }
) => {
    try {
        const keyNo = Object.keys(parentElement._parent).length
        const keyName = Object.keys(parentElement._parent)[keyNo - 1]
        if (parentElement._parent[keyName][0]) {
            parentElement._parent[keyName].splice(-1, 1)
            parentElement._parent[keyName].push(UtilsService.nativeType(value))
        } else {
            parentElement._parent[keyName] = UtilsService.nativeType(value)
        }
    } catch (e) {
        return e
    }
}

export const parseXMLToJSON = (data: string) => {
    return xmljs.xml2json(data, {
        compact: true,
        trim: true,
        ignoreDeclaration: true,
        ignoreInstruction: true,
        ignoreAttributes: false,
        ignoreComment: true,
        ignoreCdata: true,
        ignoreDoctype: true,
        textFn: removeJsonTextAttribute,
    } as any)
}

export const makeRequest = (
    type: 'post' | 'get',
    url: string,
    data?: any,
    hideSpinner?: boolean,
    config?: any,
    showCancelSpinner?: boolean
) => {
    let longRequestInfo: string | number | NodeJS.Timeout | undefined
    if (!hideSpinner) {
        SpinnerService.showSpinner(null, showCancelSpinner)
        longRequestInfo = setTimeout(() => {
            SpinnerService.setLongRequestInfo(true)
        }, 10000)
    }

    let request = null
    const finalUrl = url.indexOf('http') === 0 ? url : `${BaseUrl()}${url}`
    config = config || {}
    config.timeout = config.timeout || 30000
    request = data
        ? // @ts-ignore
          axios[type](finalUrl, data, config)
        : axios[type](finalUrl, config)
    return (
        request
            .then((response: { data: any }) => {
                return response.data
            })
            .catch((err: any) => {
                return Promise.reject(err)
            })
            // @ts-ignore
            .finally(() => {
                if (!hideSpinner) {
                    SpinnerService.hideSpinner()
                    clearTimeout(longRequestInfo)
                    SpinnerService.setLongRequestInfo(false)
                }
            })
    )
}

export const GeneralApiService = {
    cancelCurrentrequest(id?: CancelCurrentrequest) {
        if (id) {
            ;(axios as any).cancel(cancellableRequests[id])
        } else {
            for (const id of Object.keys(
                cancellableRequests
            ) as Array<CancelCurrentrequest>) {
                ;(axios as any).cancel(cancellableRequests[id])
            }
        }
    },

    getUserSettings() {
        return makeRequest(
            'get',
            SearchEngineService.getUserSettingsUrl(),
            null,
            true
        )
            .then((data: { status: number }) => {
                if (!data || data.status === 500) {
                    return null
                }
                return data
            })
            .catch(() => {
                return Promise.resolve(null)
            })
    },

    getSiteSettings() {
        return webSettingsAPI.getSiteSettings()

        // return makeRequest(
        //     'get',
        //     SearchEngineService.getSiteSettingsUrl(),
        //     null,
        //     true
        // )
        //     .then((data: { status: number }) => {
        //         if (!data || data.status === 500) {
        //             return null
        //         }
        //         return data
        //     })
        //     .catch(() => {
        //         return Promise.resolve(null)
        //     })
    },

    getSearchSettings() {
        // return webSettingsAPI.getSearchSettings();

        return makeRequest(
            'get',
            SearchEngineService.getSearchSettingsUrl(),
            null,
            true
        )
            .then((data: { status: number }) => {
                if (!data || data.status === 500) {
                    return null
                }
                return data
            })
            .catch(() => {
                return Promise.resolve(null)
            })
    },

    getMenuItems(locale: string) {
        if (locale === 'es_ES' || locale === 'zh_CN') {
            locale = locale.substring(0, 2)
        }

        const languageCode = locale || 'en_US'
        const authParam =
            !UtilsService.isWidget() &&
            typeof Liferay !== 'undefined' &&
            Liferay.ThemeDisplay.isSignedIn()
                ? `?p_auth=${Liferay.authToken}`
                : ''
        const defaultGroupId = SearchEngineService.getMainSiteGroupId()
        const groupId =
            !UtilsService.isWidget() &&
            typeof Liferay !== 'undefined' &&
            Liferay.ThemeDisplay.getScopeGroupId()
                ? Liferay.ThemeDisplay.getScopeGroupId()
                : defaultGroupId
        const url = `${BaseUrl()}/api/jsonws/geoss-service-portlet.menuitem/get-menu-items/group-id/${groupId}/language-id/${languageCode}${authParam}`
        return makeRequest('get', url, null, true).then((data: any) => {
            for (const item of data) {
                item.imgURL = item.imgURL.substr(
                    item.imgURL.lastIndexOf('/') + 1
                )
            }
            return data
        })
    },

    getTutorialTags() {
        const authParam =
            !UtilsService.isWidget() &&
            typeof Liferay !== 'undefined' &&
            Liferay.ThemeDisplay.isSignedIn()
                ? `?p_auth=${Liferay.authToken}`
                : ''
        const url = `${BaseUrl()}/api/jsonws/geoss-service-portlet.tutorialconfig/get-tutorial-configuration${authParam}`
        return makeRequest('get', url, null, true)
            .then((data: any) => {
                return data
            })
            .catch(() => {
                return Promise.resolve([])
            })
    },

    shortenLink(link: string) {
        return makeRequest(
            'get',
            `https://api-ssl.bitly.com/v3/shorten?access_token=${
                process.env.NUXT_ENV_BITLY_ACCESS_TOKEN
            }&longUrl=${encodeURIComponent(link)}`,
            null,
            true
        ).then(({ data }: { data: any }) => {
            return data.url || link
        })
    },
}
