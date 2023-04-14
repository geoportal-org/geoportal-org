const state = () => ({
    isExtendedViewActive: false,
    result: null,
})

const getters = {
    isExtendedViewActive: (state: any) => {
        return state.isExtendedViewActive
    },
    result: (state: any) => {
        return state.result
    },
}

const mutations = {
    setStateProp(state: any, data: { prop: any; value: any }) {
        state[data.prop] = data.value
    },
}

const actions = {
    setIsExtendedViewActive(context: any, value: boolean) {
        context.commit('setStateProp', { prop: 'isExtendedViewActive', value })
    },
    setResult(context: any, value: any) {
        context.commit('setStateProp', { prop: 'result', value })
    },
}

export default {
    namespaced: true,
    state,
    getters,
    mutations,
    actions,
}
