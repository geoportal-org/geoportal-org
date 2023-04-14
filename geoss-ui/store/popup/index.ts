const state = () => ({
    queue: [],
    promises: [],
})

const getters = {
    queue: (state: any) => {
        return state.queue
    },
    promises: (state: any) => {
        return state.promises
    },
}

const mutations = {
    addQueueItem(
        state: any,
        data: {
            contentId: string
            title: string
            errorInfo?: any
            generalInfo?: any
        }
    ) {
        state.queue = state.queue.concat([data])

        let resolvePromise = null
        const promise = new Promise((resolve) => {
            resolvePromise = resolve
        })
        state.promises = state.promises.concat([
            { resolvePromise, promise, contentId: data.contentId },
        ])
    },
    removeQueueItem(state: any, contentId: string) {
        const popupIndex = state.queue.findIndex(
            (item: { contentId: string }) => item.contentId === contentId
        )
        if (popupIndex !== -1) {
            state.queue.splice(popupIndex, 1)
            const promiseObj = state.promises.find(
                (item: { contentId: string }) => item.contentId === contentId
            )
            state.promises.splice(popupIndex, 1)
            promiseObj.resolvePromise()
        }
    },
    removeQueueItemWithResponse(
        state: any,
        data: { contentId: string; response: any }
    ) {
        const popupIndex = state.queue.findIndex(
            (item: { contentId: string }) => item.contentId === data.contentId
        )
        if (popupIndex !== -1) {
            state.queue.splice(popupIndex, 1)
            const promiseObj = state.promises.find(
                (item: { contentId: string }) =>
                    item.contentId === data.contentId
            )
            state.promises.splice(popupIndex, 1)
            promiseObj.resolvePromise(data.response)
        }
    },
}

const actions = {
    openPopup(
        { commit, getters }: any,
        value: {
            contentId: string
            title: string
            errorInfo?: any
            generalInfo?: any
            data?: any
            noCloseOutside?: boolean
        }
    ) {
        // this timeout is necessary because popup has on click outside
        // handler which will immediately close the popup after it is
        // added to the queue
        return new Promise((resolve) => {
            setTimeout(() => {
                commit('addQueueItem', value)
                const promiseObj = getters.promises.find(
                    (item: { contentId: string }) =>
                        item.contentId === value.contentId
                )
                return promiseObj.promise.then(resolve)
            }, 1)
        })
    },
    closePopup({ commit }: any, contentId: string) {
        commit('removeQueueItem', contentId)
    },
    closePopupWithResponse(
        { commit }: any,
        data: { contentId: string; response: any }
    ) {
        commit('removeQueueItemWithResponse', data)
    },
}

export default {
    namespaced: true,
    state,
    getters,
    actions,
    mutations,
}
