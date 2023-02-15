import Vue from 'vue'
import { GeneralGetters } from '@/store/general/general-getters'

export const BaseUrl = () => {
    if (AppVueObj.app && AppVueObj.app.$store.getters[GeneralGetters.baseUrl]) {
        return AppVueObj.app.$store.getters[GeneralGetters.baseUrl]
    } else {
        return `${process.env.VUE_APP_BASE_URL}`
    }
}

export const StaticPath = () => {
    if (
        AppVueObj.app &&
        AppVueObj.app.$store.getters[GeneralGetters.staticPath]
    ) {
        return AppVueObj.app.$store.getters[GeneralGetters.staticPath]
    } else {
        return `${process.env.VUE_APP_IMAGE_PATH}`
    }
}

interface Window {
    [key: string]: any
}
declare const window: Window

const LiferayStr: string = 'Liferay'
const jQStr: string = '$'
const cookiesStr: string = 'Cookies'

export const Liferay = window[LiferayStr]
export const $ = window[jQStr]
export const Cookies = window[cookiesStr]

interface AppVueObj {
    [key: string]: any
}
export const AppVueObj: AppVueObj = {
    app: null,
    storeStateBackup: null,
}
