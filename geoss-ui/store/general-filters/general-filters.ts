import SearchEngineService from '@/services/search-engine.service'
import date from '@/filters/date'
import { SearchGetters } from '../search/search-getters'

declare const google: any

const state = {
    sources: [] as string[],
    viewId: '',
    locationType:
        typeof google !== 'undefined' ? 'geolocation' : 'continent_country',
    locationTypeOptions: ['geolocation', 'continent_country', 'coordinates'],
    selectedAreaCoordinates: {
        W: null,
        S: null,
        E: null,
        N: null,
    },
    googlePlacesInput: '',
    boundingBoxRelation: 'OVERLAPS',
    searchFields: 'title,keywords,abstract',
    additionalSearchFields: '',
    phrase: '',
    termFrequency:
        'keyword,format,protocol,providerID,organisationName,sscScore',
    resultsPerPage: 12,
    dateFrom: '',
    dateTo: '',
    datePeriod: '',
    geossDataCore: false,
    containerVisible: false,
    requestId: null,
    inChangeProcess: false,
    workflowMapDraw: false,
    googlePlacesApiError: null,
}

const initialState = JSON.parse(JSON.stringify(state))
let lastTriggerredGeneralFiltersState = initialState

const concatCoordinates = (coordinates: {
    W: number
    S: number
    E: number
    N: number
}) => {
    const { W, S, E, N } = coordinates
    if (W && S && E && N) {
        return `${W},${S},${E},${N}`
    }
    return ',,,'
}

const getters = {
    state: (state: any) => {
        return state
    },
    lastTriggerredGeneralFiltersState: () => {
        return lastTriggerredGeneralFiltersState
    },
    stateMapped: (
        state: any,
        getters: any,
        rootState: any,
        rootGetters: any
    ) => {
        state.requestId = Math.random().toString(36).substring(2)

        const params: any = {
            searchFields: state.searchFields,
            reqID: state.requestId,
            si: rootGetters[SearchGetters.startIndex],
            ct: state.resultsPerPage,
            tf: state.termFrequency,
            rel: state.boundingBoxRelation,
            viewid: state.viewId,
        }

        if (state.dateFrom) {
            params.ts = date(state.dateFrom, 'YYYY-MM-DDThh:mm:ssZ')
        }

        if (state.dateTo) {
            params.te = date(state.dateTo, 'YYYY-MM-DDThh:mm:ssZ')
        }

        if (state.phrase) {
            params.st = SearchEngineService.stripQuotationMarks(state.phrase)
        }

        if (concatCoordinates(state.selectedAreaCoordinates) !== ',,,') {
            params.bbox = concatCoordinates(state.selectedAreaCoordinates)
        }

        if (state.viewid) {
            params.viewid = state.viewid
        }

        if (state.sources && state.sources.length) {
            params.sources = state.sources.join(',')
        } else {
            delete params.sources
        }

        if (state.geossDataCore) {
            params.gdc = true
        }

        return params
    },
    containerVisible: (state: any) => {
        return state.containerVisible
    },
    isNonEmpty: (state: any) => {
        const excludeProps = [
            'locationType',
            'containerVisible',
            'resultsPerPage',
            'requestId',
            'inChangeProcess',
        ]

        for (const prop of Object.keys(lastTriggerredGeneralFiltersState)) {
            if (
                excludeProps.indexOf(prop) === -1 &&
                JSON.stringify(state[prop]) !==
                    JSON.stringify(lastTriggerredGeneralFiltersState[prop])
            ) {
                return true
            }
        }
        return false
    },
    shareParams: (state: any) => {
        let params = ''
        const excludeParams = [
            'containerVisible',
            'requestId',
            'inChangeProcess',
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

                if (prop === 'selectedAreaCoordinates') {
                    params += `f:${prop}=${state[prop].S.toFixed(13)},${state[
                        prop
                    ].W.toFixed(13)},${state[prop].N.toFixed(13)},${state[
                        prop
                    ].E.toFixed(13)}`
                } else {
                    params += `f:${prop}=${encodeURIComponent(state[prop])}`
                }
            }
        }
        if (params) {
            params += '&'
        }
        return params
    },
    inChangeProcess(state: any) {
        return state.inChangeProcess
    },
    values(state: any) {
        return {
            sources: state.sources,
            viewId: state.viewId,
            locationType: state.locationType,
            selectedAreaCoordinates: state.selectedAreaCoordinates,
            boundingBoxRelation: state.boundingBoxRelation,
            phrase: state.phrase,
            dateFrom: state.dateFrom,
            dateTo: state.dateTo,
            datePeriod: state.datePeriod,
            geossDataCore: state.geossDataCore,
            googlePlacesInput: state.googlePlacesInput,
        }
    },
    googlePlacesInput(state: any) {
        return state.googlePlacesInput
    },
    selectedAreaCoordinates(state: any) {
        return state.selectedAreaCoordinates
    },
    workflowMapDraw(state: any) {
        return state.workflowMapDraw
    },
    additionalSearchFields(state: any) {
        return state.additionalSearchFields
    },
    googlePlacesApiError(state: any) {
        return state.googlePlacesApiError
    },
    locationTypeOptions(state: any) {
        return state.locationTypeOptions
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
    setLastTriggerredGeneralFiltersState(state: any) {
        lastTriggerredGeneralFiltersState = JSON.parse(JSON.stringify(state))
    },
}

const actions = {
    setSources(context: any, value: string[]) {
        context.commit('setStateProp', { prop: 'sources', value })
    },
    setViewId(context: any, value: string) {
        context.commit('setStateProp', { prop: 'viewId', value })
    },
    setLocationType(context: any, value: string) {
        context.commit('setStateProp', { prop: 'locationType', value })
    },
    setLocationTypeOptions(context: any, value: string) {
        context.commit('setStateProp', { prop: 'locationTypeOptions', value })
    },
    setGooglePlacesInput(context: any, value: string) {
        context.commit('setStateProp', { prop: 'googlePlacesInput', value })
    },
    setSelectedAreaCoordinates(
        context: any,
        value: { W: number; S: number; E: number; N: number } | null
    ) {
        const { W, S, E, N } = context.getters.selectedAreaCoordinates
        if (
            value === null &&
            (W !== null || S !== null || E !== null || N !== null)
        ) {
            context.commit('setStateProp', {
                prop: 'selectedAreaCoordinates',
                value: { W: null, S: null, E: null, N: null },
            })
        } else if (
            value &&
            (W !== value.W || S !== value.S || E !== value.E || N !== value.N)
        ) {
            context.commit('setStateProp', {
                prop: 'selectedAreaCoordinates',
                value,
            })
        }
    },
    setBoundingBoxRelation(context: any, value: string) {
        context.commit('setStateProp', { prop: 'boundingBoxRelation', value })
    },
    setSearchFields(context: any, value: string) {
        context.commit('setStateProp', { prop: 'searchFields', value })
    },
    setAdditionalSearchFields(context: any, value: string) {
        context.commit('setStateProp', {
            prop: 'additionalSearchFields',
            value,
        })
    },
    setPhrase(context: any, value: string) {
        context.commit('setStateProp', { prop: 'phrase', value })
    },
    setTermFrequency(context: any, value: string) {
        context.commit('setStateProp', { prop: 'termFrequency', value })
    },
    setResultsPerPage(context: any, value: number) {
        context.commit('setStateProp', { prop: 'resultsPerPage', value })
    },
    setRequestId(context: any, value: string) {
        context.commit('setStateProp', { prop: 'requestId', value })
    },
    setDateFrom(context: any, value: string) {
        context.commit('setStateProp', { prop: 'dateFrom', value })
    },
    setDateTo(context: any, value: string) {
        context.commit('setStateProp', { prop: 'dateTo', value })
    },
    setDatePeriod(context: any, value: string) {
        context.commit('setStateProp', { prop: 'datePeriod', value })
    },
    setContainerVisible(context: any, value: boolean) {
        context.commit('setStateProp', { prop: 'containerVisible', value })
    },
    setGeossDataCore(context: any, value: boolean) {
        context.commit('setStateProp', { prop: 'geossDataCore', value })
    },
    reset(context: any) {
        context.commit('resetState')
        context.commit('setLastTriggerredGeneralFiltersState')
    },
    setLastTriggerredState(context: any) {
        context.commit('setLastTriggerredGeneralFiltersState')
    },
    setInChangeProcess(context: any, value: boolean) {
        context.commit('setStateProp', { prop: 'inChangeProcess', value })
    },
    parseValues(context: any, data: any) {
        if (data && typeof data === 'object') {
            for (const prop in data) {
                if (typeof context.state[prop] !== 'undefined') {
                    context.commit('setStateProp', { prop, value: data[prop] })
                }
            }
        }
    },
    setWorkflowMapDraw(context: any, value: boolean) {
        context.commit('setStateProp', { prop: 'workflowMapDraw', value })
    },
    setGooglePlacesApiError(context: any, value: string) {
        if (value !== 'OVER_QUERY_LIMIT') {
            sessionStorage.removeItem('googlePlacesApiError')
            context.commit('setStateProp', {
                prop: 'googlePlacesApiError',
                value: null,
            })
        } else {
            sessionStorage.setItem('googlePlacesApiError', value)
            context.commit('setStateProp', {
                prop: 'googlePlacesApiError',
                value,
            })

            if (Array.isArray(context.state.locationTypeOptions)) {
                const options = context.state.locationTypeOptions.filter(
                    (e: string) => e !== 'geolocation'
                )
                context.commit('setStateProp', {
                    prop: 'locationTypeOptions',
                    value: options,
                })
            }

            if (context.state.locationType === 'geolocation') {
                context.commit('setStateProp', {
                    prop: 'locationType',
                    value: 'continent_country',
                })
            }
        }
    },
}

export const generalFilters = {
    namespaced: true,
    state,
    getters,
    actions,
    mutations,
}
