const state = () => ({
    wcFiltersAvailable: false,
})

const getters = {
    wcFiltersAvailable: (state: any) => {
        console.log(state)
        return state.wcFiltersAvailable
    },
}

const mutations = {
    setStateProp(state: any, data: { prop: any; value: any }) {
        state[data.prop] = data.value
    },
}
const actions = {
    setWcFiltersAvailable(context: any, value: boolean) {
        context.commit('setStateProp', { prop: 'wcFiltersAvailable', value })
    },
}

export default {
    namespaced: true,
    state,
    getters,
    actions,
    mutations,
}
