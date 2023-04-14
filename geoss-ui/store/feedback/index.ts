const state = () => ({
    formType: 'simple',
    showSimpleForm: false,
    showEnhancedForm: false,
    questionnaireSubmitted: false,
})

const getters = {
    formType: (state: any) => {
        return state.formType
    },
    showSimpleForm: (state: any) => {
        return state.showSimpleForm
    },
    showEnhancedForm: (state: any) => {
        return state.showEnhancedForm
    },
    questionnaireSubmitted: (state: any) => {
        return state.questionnaireSubmitted
    },
}

const mutations = {
    setStateProp(state: any, data: { prop: any; value: any }) {
        state[data.prop] = data.value
    },
}

const actions = {
    setFormType({ commit }: any, value: string) {
        commit('setStateProp', { prop: 'formType', value })
    },
    setShowSimpleForm({ commit }: any, value: boolean) {
        commit('setStateProp', { prop: 'showSimpleForm', value })
    },
    setShowEnhancedForm({ commit }: any, value: boolean) {
        commit('setStateProp', { prop: 'showEnhancedForm', value })
    },
    setQuestionnaireSubmitted({ commit }: any, value: boolean) {
        commit('setStateProp', { prop: 'questionnaireSubmitted', value })
    },
}

export default {
    namespaced: true,
    state,
    getters,
    actions,
    mutations,
}
