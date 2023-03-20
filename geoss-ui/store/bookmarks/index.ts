import to from '@/utils/to'
import GeossSearchApiService from '@/services/geoss-search.api.service'
import { bookmarksTestIds } from '@/data/bookmarks-test'
import { DataSources } from '@/interfaces/DataSources'

const state = {
    perPage: 10,
    pageOffset: 0,
    results: [],
    resultsLoading: true,
    resultsTotal: 0,
    checkAll: false,
    checkedResults: [],
    resultsOrigin: {},
    checkedOrigins: [],
    mode: 'bookmarks',
}

const getters = {
    perPage: (state: any) => {
        return state.perPage
    },
    pageOffset: (state: any) => {
        return state.pageOffset
    },
    results: (state: any) => {
        return state.results
    },
    resultsLoading: (state: any) => {
        return state.resultsLoading
    },
    resultsTotal: (state: any) => {
        return state.resultsTotal
    },
    checkAll: (state: any) => {
        return state.checkAll
    },
    checkedResults: (state: any) => {
        return state.checkedResults
    },
    resultsOrigin: (state: any) => {
        return state.resultsOrigin
    },
    checkedOrigins: (state: any) => {
        return state.checkedOrigins
    },
    mode: (state: any) => {
        return state.mode
    },
}

const mutations = {
    setStateProp(state: any, data: { prop: any; value: any }) {
        state[data.prop] = data.value
    },
    updateResultRating(
        state: any,
        { id, rating }: { id: string; rating: any }
    ) {
        const results = state.results
        if (results) {
            const result = results.find(
                (item: { id: string }) => item.id === id
            )
            if (result) {
                result.rating = rating
            }
        }
    },
}

const actions = {
    setPerPage({ commit }: any, value: number) {
        commit('setStateProp', { prop: 'perPage', value })
    },
    setPageOffset({ commit }: any, value: number) {
        commit('setStateProp', { prop: 'pageOffset', value })
    },
    setCheckedOrigins({ commit }: any, value: any) {
        commit('setStateProp', { prop: 'checkedOrigins', value })
    },
    setCheckedResults({ commit }: any, value: any) {
        commit('setStateProp', { prop: 'checkedResults', value })
    },
    setResults({ commit }: any, value: any) {
        commit('setStateProp', { prop: 'results', value })
    },
    setCheckAll({ state, commit }: any, value: number) {
        if (value) {
            const checkedResults = []
            for (const result of state.results) {
                checkedResults.push(result.id.toString())
            }
            commit('setStateProp', {
                prop: 'checkedResults',
                value: checkedResults,
            })
        } else {
            commit('setStateProp', { prop: 'checkedResults', value: [] })
        }
        commit('setStateProp', { prop: 'checkAll', value })
    },
    checkboxChange({ state, commit }: any, value: string) {
        let checkedResults = state.checkedResults
        if (checkedResults.includes(value)) {
            checkedResults = checkedResults.filter(
                (item: string) => item !== value
            )
            commit('setStateProp', { prop: 'checkAll', value: false })
        } else {
            checkedResults.push(value)
            if (checkedResults.length === state.results.length) {
                commit('setStateProp', { prop: 'checkAll', value: true })
            }
        }
        commit('setStateProp', {
            prop: 'checkedResults',
            value: checkedResults,
        })
    },
    async getResults({ state, commit }: any) {
        let data = [] as any

        if (location.pathname.includes('geo-likes')) {
            commit('setStateProp', { prop: 'mode', value: 'geo-likes' })
        } else {
            commit('setStateProp', { prop: 'mode', value: 'bookmarks' })
        }

        if (state.mode === 'geo-likes') {
            ;[, data] = await to(
                GeossSearchApiService.getGeoLikes(
                    state.pageOffset,
                    state.pageOffset + state.perPage
                )
            )
        } else {
            ;[, data] = await to(
                GeossSearchApiService.getBookmarks(
                    state.pageOffset,
                    state.pageOffset + state.perPage
                )
            )
        }

        // test data
        // data = bookmarksTestIds;
        //

        if (data) {
            const dataTypes = data.items
                .map((item: { dataSource: any }) => item.dataSource)
                .filter(
                    (value: any, index: any, self: string | any[]) =>
                        self.indexOf(value) === index
                )
            const resultsOrigin: { [key: string]: any } = {
                byType: {},
                byId: {},
                byName: {}, // dab occasionaly changes ID so we should try to compare names
            }
            let bookmarks: Array<any> | string[] = []
            for (const dataType of dataTypes) {
                const idsOfAType = []
                for (const item of data.items) {
                    if (item.dataSource === dataType) {
                        idsOfAType.push(item.targetId)
                    }
                }
                resultsOrigin.byType[dataType] = idsOfAType
                const [, results] = await to(
                    GeossSearchApiService.getTargetById(
                        idsOfAType.join(','),
                        dataType
                    )
                )
                if (results && results.entry) {
                    bookmarks = bookmarks.concat(results.entry)
                }
            }
            let cleanResults = bookmarks
            for (const item of data.items) {
                resultsOrigin.byId[
                    GeossSearchApiService.safeString(item.targetId)
                ] = {
                    dataOrigin: item.dataSource,
                    dataSource: actions.extractDataSource(
                        item,
                        cleanResults.find(
                            (result) => result.id === item.targetId + ''
                        )
                    ),
                    currMap: item.currMap || 'addsat',
                }
                if (item.name) {
                    resultsOrigin.byName[
                        GeossSearchApiService.safeString(item.name)
                    ] = {
                        dataOrigin: item.dataSource,
                        dataSource: actions.extractDataSource(
                            item,
                            cleanResults.find(
                                (result) => result.title === item.name
                            )
                        ),
                        currMap: item.currMap || 'addsat',
                    }
                }
            }

            const resultsOriginIds = Object.keys(resultsOrigin.byId).map(
                (key) => key
            )
            const resultsOriginNames = Object.keys(resultsOrigin.byName).map(
                (key) => key
            )
            const resultsIds = Object.keys(bookmarks).map((key: any) =>
                GeossSearchApiService.safeString(bookmarks[key].id)
            )
            // find all id's that changed in DAB
            let idsToRemove = resultsIds.filter(
                (id) => !resultsOriginIds.includes(id)
            )

            // if id was changed, check if we can compare it by name
            for (const item of bookmarks) {
                const safeId = GeossSearchApiService.safeString(item.id)
                const safeName = GeossSearchApiService.safeString(
                    item.title || item.metadata.title
                )
                if (
                    resultsOriginIds.includes(safeId) ||
                    resultsOriginNames.includes(safeName)
                ) {
                    idsToRemove = idsToRemove.filter((id) => id !== safeId)
                }
            }

            // clear entries that id's changed and can't be compared by name
            if (idsToRemove.length) {
                cleanResults = bookmarks.filter(
                    (item) =>
                        !idsToRemove.includes(
                            GeossSearchApiService.safeString(item.id)
                        )
                )
                console.warn(`Entries ${idsToRemove} were ommited.`)
            }

            // clear duplicated entries
            const duplicates = new Set()
            cleanResults = cleanResults.filter((item) => {
                const duplicate = duplicates.has(item.id)
                duplicates.add(item.id)
                return !duplicate
            })

            commit('setStateProp', { prop: 'results', value: cleanResults })
            commit('setStateProp', {
                prop: 'resultsOrigin',
                value: resultsOrigin,
            })
            commit('setStateProp', { prop: 'checkedResults', value: [] })
            commit('setStateProp', { prop: 'checkAll', value: false })

            if (!state.pageOffset) {
                commit('setStateProp', {
                    prop: 'resultsTotal',
                    value: data.totalCount,
                })
            }
            commit('setStateProp', { prop: 'resultsLoading', value: false })
        } else {
            commit('setStateProp', { prop: 'resultsLoading', value: false })
        }
    },
    setMode({ commit }: any, value: any) {
        commit('setStateProp', { prop: 'mode', value })
    },
    extractDataSource(item: any, result: any) {
        let resourceType = item.dataSource || 'dab'
        if (result) {
            const resultCategories = result.category

            if (resultCategories) {
                if (Array.isArray(resultCategories)) {
                    for (const i in resultCategories) {
                        if (
                            resultCategories[i]._attributes &&
                            resultCategories[i]._attributes.term === 'geoss_cr'
                        ) {
                            resourceType = resultCategories[i]._attributes.label
                        }
                    }
                } else {
                    if (
                        resultCategories._attributes &&
                        resultCategories._attributes.term === 'geoss_cr'
                    ) {
                        resourceType = resultCategories._attributes.label
                    }
                }
            }
            if (resourceType !== DataSources.DAB) {
                switch (resourceType) {
                    case 'data_resource':
                        if (item.dataSource === 'amerigeoss_ckan') {
                            resourceType = DataSources.AMERIGEOSS
                        } else if (item.dataSource === 'nextgeoss') {
                            resourceType = DataSources.NEXTGEOSS
                        } else {
                            resourceType = DataSources.DATA
                        }
                        break
                    case 'information_resource':
                        if (item.dataSource === 'zenodo') {
                            resourceType = DataSources.ZENODO
                        } else if (item.dataSource === 'wikipedia') {
                            resourceType = DataSources.WIKIPEDIA
                        } else {
                            resourceType = DataSources.INFORMATION
                        }
                        break
                    case 'service_resource':
                        resourceType = DataSources.SERVICES
                        break
                }
            }
        }
        return resourceType
    },
    updateResultRating({ commit }: any, data: { id: string; rating: any }) {
        commit('updateResultRating', data)
    },
}

export default {
    namespaced: true,
    state,
    getters,
    actions,
    mutations,
}
