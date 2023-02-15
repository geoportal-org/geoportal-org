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

export const Liferay = {
    authToken: 'authToken',
    ThemeDisplay: {
        getScopeGroupId: () => {
            return 0
        },
        getUserId: () => {
            return 1
        },
        getLayoutURL: () => {
            return ''
        },
        isSignedIn: () => {
            return false
        },
    },
}

interface AppVueObj {
    [key: string]: any
}
export const AppVueObj: AppVueObj = {
    app: null,
    storeStateBackup: null,
}
