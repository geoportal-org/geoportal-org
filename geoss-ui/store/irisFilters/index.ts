const state = () => ({
    magnitudeType: null,
    magnitudeTypeOptions: [
        { id: 'ML', text: 'Richter magnitude' },
        { id: 'Ms', text: 'Surface wave magnitude' },
        { id: 'mb', text: 'Body wave magnitude' },
        { id: 'Mw', text: 'Moment magnitude' },
        { id: 'all', text: 'ALL' },
    ],
    sort: null,
    sortOptions: [
        { id: 'time', text: 'Time descending' },
        { id: 'time-asc', text: 'Time ascending' },
        { id: 'magnitude', text: 'Magnitude descending' },
        { id: 'magnitude-asc', text: 'Magnitude ascending' },
    ],
    minMagnitude: null,
    maxMagnitude: null,
    minDepth: null,
    maxDepth: null,
    dateFrom: '',
    dateTo: '',
    containerVisible: false,
    irisFiltersAvailable: false,
})

const initialState = JSON.parse(JSON.stringify(state()))

const paramsMap: { [key: string]: string } = {
    magnitudeType: 'magt',
    sort: 'evtOrd',
    minMagnitude: 'minmag',
    maxMagnitude: 'maxmag',
    minDepth: 'mindepth',
    maxDepth: 'maxdepth',
    dateFrom: 'ts',
    dateTo: 'te',
}

const saveParamsMap: { [key: string]: string } = {
    magnitudeType: 'magt',
    sort: 'evtOrd',
    minMagnitude: 'minMag',
    maxMagnitude: 'maxMag',
    minDepth: 'minDep',
    maxDepth: 'maxDep',
    dateFrom: 'dateFromIris',
    dateTo: 'dateToIris',
}

const getters: { [key: string]: any } = {
    stateMapped: (state: any) => {
        const params: { [key: string]: string } = {}
        for (const param of Object.keys(paramsMap)) {
            if (state[param] !== null && state[param] !== '') {
                params[paramsMap[param]] = state[param]
            }
        }
        return params
    },
    saveParams: (state: any) => {
        const params: { [key: string]: string } = {}
        for (const param of Object.keys(saveParamsMap)) {
            if (state[param]) {
                params[saveParamsMap[param]] = state[param]
            }
        }
        return params
    },
    shareParams: (state: any) => {
        let params = ''
        const excludeParams = [
            'magnitudeTypeOptions',
            'sortOptions',
            'containerVisible',
            'irisFiltersAvailable',
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

for (const key of Object.keys(initialState)) {
    getters[key] = (state: any) => {
        return state[key]
    }
}

const mutations: { [key: string]: any } = {
    setStateProp(state: any, data: { prop: any; value: any }) {
        state[data.prop] = data.value
    },
    resetState(state: any) {
        for (const prop of Object.keys(initialState)) {
            state[prop] = JSON.parse(JSON.stringify(initialState[prop]))
        }
    },
}

const actions: { [key: string]: any } = {
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

for (const key of Object.keys(initialState)) {
    const keyCapitalized: string = key.charAt(0).toUpperCase() + key.substr(1)
    actions[`set${keyCapitalized}`] = ({ commit }: any, value: any) => {
        commit('setStateProp', { prop: key, value })
    }
}

export default {
    namespaced: true,
    state,
    getters,
    mutations,
    actions,
}
