const state = () => ({
    dabBaseUrl: null,
    dabBaseUrlConf: null,
    kpBaseUrl: null,
    siteName: '',
    siteLogo: '',
    siteUrl: '/',
    siteId: 0,
    tourUrl: '/',
    dabDataProvidersUrl: null,
    w3wKey: null,
    userDabBaseUrl: '',
    internalOpenSearchUrl: '',
    defaultSourceName: null,
})

const getters = {
    dabBaseUrl: (state: any) => {
        return state.dabBaseUrl
    },
    dabBaseUrlConf: (state: any) => {
        return state.dabBaseUrlConf
    },
    kpBaseUrl: (state: any) => {
        return state.kpBaseUrl
    },
    siteName: (state: any) => {
        return state.siteName
    },
    siteLogo: (state: any) => {
        return state.siteLogo
    },
    siteUrl: (state: any) => {
        return state.siteUrl
    },
    siteId: (state: any) => {
        return state.siteId
    },
    tourUrl: (state: any) => {
        return state.tourUrl
    },
    dabDataProvidersUrl: (state: any) => {
        return state.dabDataProvidersUrl
    },
    w3wKey: (state: any) => {
        return state.w3wKey
    },
    userDabBaseUrl: (state: any) => {
        return state.userDabBaseUrl
    },
    internalOpenSearchUrl: (state: any) => {
        return state.internalOpenSearchUrl
    },
    defaultSourceName: (state: any) => {
        return state.defaultSourceName
    },
}

const mutations = {
    setStateProp(state: any, data: { prop: any; value: any }) {
        state[data.prop] = data.value
    },
}

const actions = {
    setDabBaseUrl: (context: any, value: string) => {
        context.commit('setStateProp', { prop: 'dabBaseUrl', value })
    },
    setDabBaseUrlConf: (context: any, value: string) => {
        context.commit('setStateProp', { prop: 'dabBaseUrlConf', value })
    },
    setKpBaseUrl: (context: any, value: string) => {
        context.commit('setStateProp', { prop: 'kpBaseUrl', value })
    },
    setSiteName: (context: any, value: string) => {
        context.commit('setStateProp', { prop: 'siteName', value })
    },
    setSiteLogo: (context: any, value: string) => {
        context.commit('setStateProp', { prop: 'siteLogo', value })
    },
    setSiteUrl: (context: any, value: string) => {
        context.commit('setStateProp', { prop: 'siteUrl', value })
    },
    setSiteId: (context: any, value: number) => {
        context.commit('setStateProp', { prop: 'siteId', value })
    },
    setTourUrl: (context: any, value: string) => {
        context.commit('setStateProp', { prop: 'tourUrl', value })
    },
    setDabDataProvidersUrl: (context: any, value: string) => {
        context.commit('setStateProp', { prop: 'dabDataProvidersUrl', value })
    },
    setW3wKey: (context: any, value: string) => {
        context.commit('setStateProp', { prop: 'w3wKey', value })
    },
    setUserDabBaseUrl: (context: any, value: string) => {
        context.commit('setStateProp', { prop: 'userDabBaseUrl', value })
    },
    setInternalOpenSearchUrl: (context: any, value: string) => {
        context.commit('setStateProp', { prop: 'internalOpenSearchUrl', value })
    },
    setDefaultSourceName: (context: any, value: string) => {
        context.commit('setStateProp', { prop: 'defaultSourceName', value })
    },
}

export default {
    namespaced: true,
    state,
    getters,
    actions,
    mutations,
}
