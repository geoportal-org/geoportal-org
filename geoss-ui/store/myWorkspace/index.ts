const state = () => ({
    settings: {
        dhusUsername: null,
    },
    search: {
        popularSearchId: 901,
        savedSearchId: 0,
        defaultSearch: true,
    },
})

const initialState = JSON.parse(JSON.stringify(state()))

const getters = {
    search: (state: any) => {
        return state.search
    },
    settings: (state: any) => {
        return state.settings
    },
    shareParams: (state: any) => {
        let params = ''
        const excludedProps = ['defaultSearch']
        for (const prop of Object.keys(initialState.search)) {
            if (
                excludedProps.indexOf(prop) === -1 &&
                JSON.stringify(state.search[prop]) !==
                    JSON.stringify(initialState[prop])
            ) {
                if (params) {
                    params += '&'
                }
                params += `my-workspace:${prop}=${encodeURIComponent(
                    state[prop]
                )}`
            }
        }
        if (params) {
            params += '&'
        }
        return params
    },
}

const mutations = {
    setSearchStateProp(state: any, data: { prop: any; value: any }) {
        state.search[data.prop] = data.value
    },
    setSettingsStateProp(state: any, data: { prop: any; value: any }) {
        state.settings[data.prop] = data.value
    },
}

const actions = {
    setPopularSearchId(context: any, value: number) {
        context.commit('setSearchStateProp', { prop: 'popularSearchId', value })
    },
    setDhusUsername(context: any, value: string) {
        context.commit('setSettingsStateProp', { prop: 'dhusUsername', value })
    },
}

export default {
    namespaced: true,
    state,
    getters,
    actions,
    mutations,
}
