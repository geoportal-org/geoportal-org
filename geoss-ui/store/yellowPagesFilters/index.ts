import YellowPagesApiService from '@/services/yellow-pages.api.service'
import { SearchEngineGetters } from '@/store/searchEngine/search-engine-getters'
import { AppVueObj } from '@/data/global'

const state = () => ({
    search: '',
    orderBy: 'asc',
    perPage: 5,
    pageOffset: 0,
    results: [],
    resultsTotal: 0
})

const getters = {
    search: (state: any) => {
        return state.search
    },
    orderBy: (state: any) => {
        return state.orderBy
    },
    perPage: (state: any) => {
        return state.perPage
    },
    pageOffset: (state: any) => {
        return state.pageOffset
    },
    results: (state: any) => {
        return state.results
    },
    resultsTotal: (state: any) => {
        return state.resultsTotal
    }
}

const mutations = {
    setStateProp(state: any, data: { prop: any; value: any }) {
        state[data.prop] = data.value
    }
}

const actions = {
    setSearch({ commit }: any, value: string) {
        commit('setStateProp', { prop: 'search', value })
    },
    setOrderBy({ commit }: any, value: string) {
        commit('setStateProp', { prop: 'orderBy', value })
    },
    setPerPage({ commit }: any, value: number) {
        commit('setStateProp', { prop: 'perPage', value })
    },
    setPageOffset({ commit }: any, value: number) {
        commit('setStateProp', { prop: 'pageOffset', value })
    },
    getResults({ commit }: any) {
        return YellowPagesApiService.getProviders(
            AppVueObj.app.$store.getters[
                SearchEngineGetters.dabDataProvidersUrl
            ]
        ).then(({ results, resultsTotal }) => {
            commit('setStateProp', { prop: 'results', value: results })
            commit('setStateProp', {
                prop: 'resultsTotal',
                value: resultsTotal
            })
        })
    }
}

export default {
    namespaced: true,
    state,
    getters,
    actions,
    mutations
}
