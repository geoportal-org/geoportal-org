const state = {
    keyword: null,
    keywordOptions: [],
    format: null,
    formatOptions: [],
    source: null,
    sourceOptions: [],
    protocol: null,
    protocolOptions: [],
    organisation: null,
    organisationOptions: [],
    score: null,
    scoreOptions: [],
    facetedFiltersAvailable: false,
    containerVisible: false,
}

const initialState = JSON.parse(JSON.stringify(state))

const paramsMap: { [key: string]: string } = {
    keyword: 'kwd',
    format: 'frmt',
    source: 'sources',
    protocol: 'prot',
    organisation: 'organisationName',
    score: 'sscScore',
}

const getters = {
    state: (state: any) => {
        return state
    },
    keyword: (state: any) => {
        return state.keyword
    },
    keywordOptions: (state: any) => {
        return state.keywordOptions
    },
    format: (state: any) => {
        return state.format
    },
    formatOptions: (state: any) => {
        return state.formatOptions
    },
    source: (state: any) => {
        return state.source
    },
    sourceOptions: (state: any) => {
        return state.sourceOptions
    },
    protocol: (state: any) => {
        return state.protocol
    },
    protocolOptions: (state: any) => {
        return state.protocolOptions
    },
    organisation: (state: any) => {
        return state.organisation
    },
    organisationOptions: (state: any) => {
        return state.organisationOptions
    },
    score: (state: any) => {
        return state.score
    },
    scoreOptions: (state: any) => {
        return state.scoreOptions
    },
    facetedFiltersAvailable: (state: any) => {
        return state.facetedFiltersAvailable
    },
    containerVisible: (state: any) => {
        return state.containerVisible
    },
    stateMapped: (state: any) => {
        const params: { [key: string]: string } = {}
        for (const param of [
            'keyword',
            'format',
            'source',
            'protocol',
            'organisation',
            'score',
        ]) {
            if (state[param]) {
                params[paramsMap[param]] = state[param]
            }
        }
        return params
    },
    saveParams: (state: any) => {
        const params: { [key: string]: string } = {}
        for (const param of [
            'keyword',
            'format',
            'source',
            'protocol',
            'organisation',
            'score',
        ]) {
            if (state[param]) {
                params[param] = state[param]
            }
        }
        return params
    },
    shareParams: (state: any) => {
        let params = ''
        const excludeParams = [
            'keywordOptions',
            'formatOptions',
            'sourceOptions',
            'protocolOptions',
            'organisationOptions',
            'scoreOptions',
            'facetedFiltersAvailable',
            'containerVisible',
        ]
        for (const prop of Object.keys(initialState)) {
            if (
                excludeParams.indexOf(prop) === -1 &&
                JSON.stringify(state[prop]) !==
                    JSON.stringify(initialState[prop])
            ) {
                if (params) {
                    params += '&'
                }
                params += `f:${prop}=${encodeURIComponent(state[prop])}`
            }
        }
        if (params) {
            params += '&'
        }
        return params
    },
}

const mutations = {
    setStateProp(state: any, data: { prop: any; value: any }) {
        state[data.prop] = data.value
    },
    resetState(state: any) {
        for (const prop of Object.keys(initialState)) {
            state[prop] = JSON.parse(JSON.stringify(initialState[prop]))
        }
    },
}

const actions = {
    setKeyword(context: any, value: string) {
        context.commit('setStateProp', { prop: 'keyword', value })
    },
    setKeywordOptions(context: any, value: []) {
        context.commit('setStateProp', { prop: 'keywordOptions', value })
    },
    setFormat(context: any, value: string) {
        context.commit('setStateProp', { prop: 'format', value })
    },
    setFormatOptions(context: any, value: []) {
        context.commit('setStateProp', { prop: 'formatOptions', value })
    },
    setSource(context: any, value: string) {
        context.commit('setStateProp', { prop: 'source', value })
    },
    setSourceOptions(context: any, value: []) {
        context.commit('setStateProp', { prop: 'sourceOptions', value })
    },
    setProtocol(context: any, value: string) {
        context.commit('setStateProp', { prop: 'protocol', value })
    },
    setProtocolOptions(context: any, value: []) {
        context.commit('setStateProp', { prop: 'protocolOptions', value })
    },
    setOrganisation(context: any, value: string) {
        context.commit('setStateProp', { prop: 'organisation', value })
    },
    setOrganisationOptions(context: any, value: []) {
        context.commit('setStateProp', { prop: 'organisationOptions', value })
    },
    setScore(context: any, value: string) {
        context.commit('setStateProp', { prop: 'score', value })
    },
    setScoreOptions(context: any, value: []) {
        context.commit('setStateProp', { prop: 'scoreOptions', value })
    },
    setFacetedFiltersAvailable(context: any, value: boolean) {
        context.commit('setStateProp', {
            prop: 'facetedFiltersAvailable',
            value,
        })
        if (!value) {
            context.commit('resetState')
        }
    },
    setContainerVisible(context: any, value: string) {
        context.commit('setStateProp', { prop: 'containerVisible', value })
    },
    toggleContainerVisible({ commit, getters }: any) {
        commit('setStateProp', {
            prop: 'containerVisible',
            value: !getters.containerVisible,
        })
    },
    reset(context: any) {
        context.commit('resetState')
    },
}

export const facetedFilters = {
    namespaced: true,
    state,
    getters,
    mutations,
    actions,
}
