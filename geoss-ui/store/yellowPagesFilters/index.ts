import YellowPagesApiService from '@/services/yellow-pages.api.service'
import { SearchEngineGetters } from '@/store/searchEngine/search-engine-getters'
import { AppVueObj } from '@/data/global'

const state = () => ({
    search: '',
    orderBy: 'asc',
    perPage: 5,
    pageOffset: 0,
    results: [],
    resultsTotal: 0,
    currentResults: []
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
    },
    currentResults: (state: any) => {
        let finalResult = state.currentResults
        if (state.search !== '') {
            const filteredResults = state.currentResults.filter(
                (element: any) =>
                    element.name
                        .toLowerCase()
                        .includes(state.search.toLowerCase()) ||
                    element.data.shortDescription
                        .toLowerCase()
                        .includes(state.search.toLowerCase())
            )
            finalResult = filteredResults
        } else {
            finalResult = state.currentResults
        }

        switch (
           state.orderBy
        ) {
            case 'asc': {
                finalResult.sort((a: any, b: any) => {
                    if (
                        a.name.toLowerCase().trim() >
                        b.name.toLowerCase().trim()
                    ) {
                        return 1
                    } else if (
                        a.name.toLowerCase().trim() <
                        b.name.toLowerCase().trim()
                    ) {
                        return -1
                    } else {
                        return 0
                    }
                })
                break
            }
            case 'desc': {
                finalResult.sort((a: any, b: any) => {
                    if (
                        a.name.toLowerCase().trim() >
                        b.name.toLowerCase().trim()
                    ) {
                        return -1
                    } else if (
                        a.name.toLowerCase().trim() <
                        b.name.toLowerCase().trim()
                    ) {
                        return 1
                    } else {
                        return 0
                    }
                })
                break
            }
            case 'date': {
                finalResult.sort((a: any, b:any) => {
                    if (!a.createDate) {
                        return 1
                    } else if (!b.createDate) {
                        return -1
                    } else if (!a.createDate && !b.createDate) {
                        return 0
                    }
                    if (a.createDate > b.createDate) {
                        return -1
                    } else if (a.createDate < b.createDate) {
                        return 1
                    } else {
                        return 0
                    }
                })
                break
            }
        }

        return finalResult
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
    async getResults({ commit }: any) {
        let result = []
        try {
            const providersUrl =
                AppVueObj.app.$store.getters[
                    SearchEngineGetters.dabDataProvidersUrl
                ]

            const providersLogin =
                AppVueObj.app.$store.getters[
                    SearchEngineGetters.dabDataProvidersUsername
                ]
            const providersPass =
                AppVueObj.app.$store.getters[
                    SearchEngineGetters.dabDataProvidersPassword
                ]

            const headers = new Headers()
            const authString =
                'Basic ' + btoa(`${providersLogin}:${providersPass}`)
            headers.append('Authorization', authString)

            const providersResponse = await fetch(
                `${providersUrl}?page=0&size=9999`,
                {
                    method: 'GET',
                    headers: headers
                }
            )
            const json = await providersResponse.json()
            if (
                Number(providersResponse.status) < 300 ||
                Number(providersResponse.status) >= 200
            ) {
                result = json._embedded.dataProviderResultModels
            }
        } catch (e: any) {
            console.error(e)
        }

        return result
    },
    setCurrentResults({ commit }: any, value: number) {
        commit('setStateProp', { prop: 'currentResults', value })
    }
}

export default {
    namespaced: true,
    state,
    getters,
    actions,
    mutations
}
