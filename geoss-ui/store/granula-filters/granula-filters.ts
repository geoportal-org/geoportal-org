import { SearchGetters } from '../search/search-getters'
import { ParentRef } from '@/interfaces/ParentRef'
import { DataSources } from '@/interfaces/DataSources'

const state = {
    productType: null,
    productTypeOptions: [],
    sensorPolarisation: null,
    sensorPolarisationOptions: [],
    sensorMode: null,
    sensorModeOptions: [],
    sensorSwath: null,
    sensorSwathOptions: [],
    instrument: null,
    instrumentOptions: [],
    productLevel: null,
    productLevelOptions: [],
    timeliness: null,
    timelinessOptions: [],
    cloudCoverage: [0, 70] as number[],
    cloudCoverageAvailable: false,
    relativeOrbit: '',
    relativeOrbitAvailable: false,
    row: null,
    rowAvailable: false,
    path: null,
    pathAvailable: false,
    fromDate: '',
    toDate: '',
    granulaFiltersAvailable: false,
    containerVisible: false,
    availableQueryable: [
        'prodType',
        'sensorOpMode',
        'sensorSwath',
        'sarPolCh',
        's3InstrumentIdx',
        's3ProductLevel',
        's3Timeliness',
    ],
    availableContinuous: ['cloudcp'],
    availableInteger: ['relOrbit', 'path', 'row'],
}

const initialState = JSON.parse(JSON.stringify(state))

const paramsMap: { [key: string]: any } = {
    productType: 'prodType',
    sensorPolarisation: 'sarPolCh',
    sensorMode: 'sensorOpMode',
    sensorSwath: 'sensorSwath',
    instrument: 's3InstrumentIdx',
    productLevel: 's3ProductLevel',
    timeliness: 's3Timeliness',
    cloudCoverage: 'cloudcp',
    relativeOrbit: 'relOrbit',
    row: 'path',
    path: 'row',
    fromDate: 'ts',
    toDate: 'te',
}

const saveParamsMap: { [key: string]: any } = {
    productType: 'prodType',
    sensorPolarisation: 'sarPolCh',
    sensorMode: 'sensorOpMode',
    sensorSwath: 'sensorSwath',
    instrument: 's3InstrumentIdx',
    productLevel: 's3ProductLevel',
    timeliness: 's3Timeliness',
    cloudCoverage: 'cloudcp',
    relativeOrbit: 'relOrbit',
    row: 'path',
    path: 'row',
    fromDate: 'dateFromGranula',
    toDate: 'dateToGranula',
}

const getters: { [key: string]: any } = {
    stateMapped: (state: any) => {
        const params: { [key: string]: any } = {}
        for (const param of Object.keys(paramsMap)) {
            const available = state[param + 'Available']
            if (
                state[param] &&
                (typeof available === 'undefined' || available)
            ) {
                if (state[param].constructor === Array) {
                    params[paramsMap[param]] = JSON.stringify(state[param])
                } else {
                    params[paramsMap[param]] = state[param]
                }
            }
        }
        return params
    },
    saveParams: (state: any) => {
        const params: { [key: string]: any } = {}
        for (const param of Object.keys(saveParamsMap)) {
            const available = state[param + 'Available']
            if (
                state[param] &&
                (typeof available === 'undefined' || available)
            ) {
                if (state[param].constructor === Array) {
                    params[saveParamsMap[param]] = JSON.stringify(state[param])
                } else {
                    params[saveParamsMap[param]] = state[param]
                }
            }
        }
        return params
    },
    shareParams: (state: any) => {
        let params = ''
        const excludeParams = [
            'productTypeOptions',
            'sensorPolarisationOptions',
            'sensorModeOptions',
            'sensorSwathOptions',
            'instrumentOptions',
            'productLevelOptions',
            'timelinessOptions',
            'availableQueryable',
            'availableContinuous',
            'availableInteger',
            'cloudCoverageAvailable',
            'relativeOrbitAvailable',
            'granulaFiltersAvailable',
            'rowAvailable',
            'pathAvailable',
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

let actions: { [key: string]: any } = {}

for (const key of Object.keys(initialState)) {
    const keyCapitalized = key.charAt(0).toUpperCase() + key.substr(1)
    actions[`set${keyCapitalized}`] = ({ commit }: any, value: any) => {
        commit('setStateProp', { prop: key, value })
    }
}

actions = {
    ...actions,

    toggleContainerVisible({ commit, getters }: any) {
        commit('setStateProp', {
            prop: 'containerVisible',
            value: !getters.containerVisible,
        })
    },
    reset(context: any) {
        context.commit('resetState')
    },
    setGranulaFiltersAvailable(context: any, value: boolean) {
        context.commit('setStateProp', {
            prop: 'granulaFiltersAvailable',
            value,
        })
        if (!value) {
            context.commit('resetState')
        }
    },
    checkParamsAvailability({ dispatch, rootGetters, getters }: any) {
        // Temporary VLAB Sentinel2-only Support
        if (
            rootGetters[SearchGetters.workflow] &&
            rootGetters[SearchGetters.workflowInputId]
        ) {
            dispatch('setCloudCoverageAvailable', true)
            dispatch('setRelativeOrbitAvailable', true)
            dispatch('setPathAvailable', true)
            dispatch('setRowAvailable', true)
            return
        }

        let queryable = []
        const dabparentRef = rootGetters[SearchGetters.parentRefs].find(
            (parentRef: ParentRef) => parentRef.dataSource === DataSources.DAB
        )
        if (dabparentRef) {
            if (
                dabparentRef &&
                dabparentRef.entry.satelliteCollectionQueryable
            ) {
                queryable =
                    dabparentRef.entry.satelliteCollectionQueryable.split(',')
            }
        }

        for (const item of getters.availableContinuous) {
            switch (item) {
                case 'cloudcp': {
                    dispatch(
                        'setCloudCoverageAvailable',
                        queryable.indexOf(item) !== -1
                    )
                    if (!this.cloudCoverageAvailable) {
                        this.cloudCoverage = [0, 70]
                    }
                    break
                }
            }
        }

        for (const item of getters.availableInteger) {
            switch (item) {
                case 'relOrbit': {
                    dispatch(
                        'setRelativeOrbitAvailable',
                        queryable.indexOf(item) !== -1
                    )
                    break
                }
                case 'path': {
                    dispatch('setPathAvailable', queryable.indexOf(item) !== -1)
                    break
                }
                case 'row': {
                    dispatch('setRowAvailable', queryable.indexOf(item) !== -1)
                    break
                }
            }
        }
    },
}

export const granulaFilters = {
    namespaced: true,
    state,
    getters,
    mutations,
    actions,
}
