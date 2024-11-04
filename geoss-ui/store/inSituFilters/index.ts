const state = () => ({
    inSituFiltersAvailable: false,
    cropTypes: [],
    cropTypesOptions: [],
    quantityTypes: [],
    quantityTypesOptions: [],
    landCoverTypes: [],
    landCoverTypesOptions: [],
    irrigationTypes: [],
    irrigationTypesOptions: [],
    cropConfidence: [0, 100],
    landCoverConfidence: [0, 100],
    irrigationConfidence: [0, 100]
})

const initialState = JSON.parse(JSON.stringify(state()))

const paramsMap: { [key: string]: any } = {
    cropTypes: 'cropTypes',
    quantityTypes: 'quantityTypes',
    landCoverTypes: 'landCoverTypes',
    irrigationTypes: 'irrigationTypes',
    cropConfidence: 'cropConfidence',
    landCoverConfidence: 'landCoverConfidence',
    irrigationConfidence: 'irrigationConfidence'
}

const getters: { [key: string]: any } = {
    inSituFiltersAvailable: (state: any) => {
        return state.inSituFiltersAvailable
    },
    stateMapped: (state: any) => {
        const params: { [key: string]: any } = {}
        for (const param of Object.keys(paramsMap)) {
            const available = state[param + 'Available']
            if (
                state[param] &&
                (typeof available === 'undefined' || available)
            ) {
                if (state[param].constructor === Array) {
                    if (state[param].length === 0) {
                        params[paramsMap[param]] = ''
                    } else {
                        params[paramsMap[param]] = JSON.stringify(
                            state[param]
                        ).replace(/[\\\[\]\"\'"]/g, '')
                    }
                } else {
                    params[paramsMap[param]] = state[param]
                }
            }
        }
        return params
    },
    saveParams: (state: any) => {
        const params: { [key: string]: string } = {}
        for (const param of [
            'cropTypes',
            'quantityTypes',
            'landCoverTypes',
            'irrigationTypes'
        ]) {
            if (state[param]) {
                params[param] = state[param]
            }
        }
        return params
    }
}

for (const key of Object.keys(initialState)) {
    getters[key] = (state: any) => {
        return state[key]
    }
}

const mutations = {
    setStateProp(state: any, data: { prop: any; value: any }) {
        state[data.prop] = data.value
    },
    resetState(state: any) {
        for (const prop of Object.keys(initialState)) {
            state[prop] = JSON.parse(JSON.stringify(initialState[prop]))
        }
    }
}

let actions: { [key: string]: any } = {}

for (const key of Object.keys(initialState)) {
    const keyCapitalized = key.charAt(0).toUpperCase() + key.substr(1)
    actions[`set${keyCapitalized}`] = ({ commit }: any, value: any) => {
        commit('setStateProp', { prop: key, value })
    }
}

actions = {
    ...actions,

    setInSituFiltersAvailable(context: any, value: boolean) {
        context.commit('setStateProp', {
            prop: 'inSituFiltersAvailable',
            value
        })
    },
    reset(context: any) {
        context.commit('resetState')
    }
}

export default {
    namespaced: true,
    state,
    getters,
    actions,
    mutations
}
