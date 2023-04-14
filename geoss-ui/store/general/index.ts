import { AppVueObj } from '~/data/global'

const state = () => ({
    storeInitialized: false,
    langLocale: '',
    staticPath: `${process.env.NUXT_ENV_IMAGE_PATH}`,
    baseUrl: `${process.env.NUXT_ENV_BASE_URL}`,
    imagePreview: null,
    isWidget: false,
    isEntryExtensionEnabled: true,
    isExtendedViewEnabled: true,
    isBulkDownloadEnabled: true,
    widgetAccessKey: null,
})

const getters = {
    storeInitialized: (state: any) => {
        return state.storeInitialized
    },
    langLocale: (state: any) => {
        return state.langLocale
    },
    staticPath: (state: any) => {
        return state.staticPath
    },
    baseUrl: (state: any) => {
        return state.baseUrl
    },
    imagePreview: (state: any) => {
        return state.imagePreview
    },
    isWidget: (state: any) => {
        return state.isWidget
    },
    isEntryExtensionEnabled: (state: any) => {
        return state.isEntryExtensionEnabled
    },
    isExtendedViewEnabled: (state: any) => {
        return state.isExtendedViewEnabled
    },
    isBulkDownloadEnabled: (state: any) => {
        return state.isBulkDownloadEnabled
    },
    widgetAccessKey: (state: any) => {
        return state.widgetAccessKey
    },
}

const mutations = {
    setStateProp(state: any, data: { prop: any; value: any }) {
        state[data.prop] = data.value
    },
}

const actions = {
    setStoreInitialized(context: any, value: boolean) {
        return context.commit('setStateProp', {
            prop: 'storeInitialized',
            value,
        })
    },
    setLangLocale: (context: any, value: string) => {
        // AppVueObj.app.$i18n.locale = value
        context.commit('setStateProp', { prop: 'langLocale', value })
    },
    setStaticPath: (context: any, value: string) => {
        context.commit('setStateProp', { prop: 'staticPath', value })
    },
    setBaseUrl: (context: any, value: string) => {
        context.commit('setStateProp', { prop: 'baseUrl', value })
    },
    setImagePreview: (context: any, value: HTMLImageElement) => {
        context.commit('setStateProp', { prop: 'imagePreview', value })
    },
    setIsWidget({ commit }: any, value: boolean) {
        commit('setStateProp', { prop: 'isWidget', value })
    },
    setIsEntryExtensionEnabled({ commit }: any, value: boolean) {
        commit('setStateProp', { prop: 'isEntryExtensionEnabled', value })
    },
    setIsExtendedViewEnabled({ commit }: any, value: boolean) {
        commit('setStateProp', { prop: 'isExtendedViewEnabled', value })
    },
    setIsBulkDownloadEnabled({ commit }: any, value: boolean) {
        commit('setStateProp', { prop: 'isBulkDownloadEnabled', value })
    },
    setWidgetAccessKey({ commit }: any, value: string) {
        commit('setStateProp', { prop: 'widgetAccessKey', value })
    },
}

export default {
    namespaced: true,
    state,
    getters,
    actions,
    mutations,
}
