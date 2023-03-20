const state = {
    id: null,
    isSignedIn: false,
    authToken: null,
    groupId: null,
    bookmarks: [] as string[],
    savedRuns: [],
}

const getters = {
    id: (state: any) => {
        return state.id
    },
    isSignedIn: (state: any) => {
        return state.isSignedIn
    },
    authToken: (state: any) => {
        return state.authToken
    },
    groupId: (state: any) => {
        return state.groupId
    },
    bookmarks: (state: any) => {
        return state.bookmarks
    },
    savedRuns: (state: any) => {
        return state.savedRuns
    },
}

const mutations = {
    setStateProp(state: any, data: { prop: any; value: any }) {
        state[data.prop] = data.value
    },
    addBookmark(state: any, data: any) {
        state.bookmarks.push(data)
    },
    removeBookmark(state: any, targetId: string) {
        state.bookmarks = state.bookmarks.filter(
            (item: { targetId: string }) => targetId !== item.targetId
        )
    },
}

const actions = {
    setId({ commit }: any, value: string) {
        commit('setStateProp', { prop: 'id', value })
    },
    setIsSignedIn({ commit }: any, value: boolean) {
        commit('setStateProp', { prop: 'isSignedIn', value })
    },
    setAuthToken({ commit }: any, value: string) {
        commit('setStateProp', { prop: 'authToken', value })
    },
    setGroupId({ commit }: any, value: string) {
        commit('setStateProp', { prop: 'groupId', value })
    },
    setBookmarks({ commit }: any, value: string[]) {
        commit('setStateProp', { prop: 'bookmarks', value })
    },
    addBookmark({ commit }: any, data: any) {
        commit('addBookmark', data)
    },
    removeBookmark({ commit }: any, targetId: string) {
        commit('removeBookmark', targetId)
    },
}

export default {
    namespaced: true,
    state,
    getters,
    actions,
    mutations,
}
