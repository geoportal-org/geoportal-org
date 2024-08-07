const state = () => ({
    menuOpened: false,
    menuItems: [],
    menuItemsURL: '',
    langContainerActive: false
})

const getters = {
    opened: (state: any) => {
        return state.menuOpened
    },
    items: (state: any) => {
        return state.menuItems
    },
    itemsURL: (state: any) => {
        return state.menuItemsURL
    },
    langContainerActive: (state: any) => {
        return state.langContainerActive
    }
}

const mutations = {
    setStateProp(state: any, data: { prop: any; value: any }) {
        state[data.prop] = data.value
    }
}

const actions = {
    setOpened: (context: any, value: boolean) => {
        context.commit('setStateProp', { prop: 'menuOpened', value })
    },
    toggleOpened: (context: any) => {
        context.commit('setStateProp', {
            prop: 'menuOpened',
            value: !context.state.menuOpened
        })
    },
    setItems: (context: any, value: []) => {
        context.commit('setStateProp', { prop: 'menuItems', value })
    },
    setItemsURL: (context: any, value: string) => {
        context.commit('setStateProp', { prop: 'menuItemsURL', value })
    },
    setLangContainerActive: (context: any, value: boolean) => {
        context.commit('setStateProp', { prop: 'langContainerActive', value })
    }
}

export default {
    namespaced: true,
    state,
    getters,
    actions,
    mutations
}
