import { BulkDownloadLink } from '@/interfaces/BulkDownloadLink'

const state = () => ({
    links: [] as BulkDownloadLink[],
    openTrigger: false as boolean,
})

const updateSessionStorage = () => {
    sessionStorage.setItem('bulkDownload', JSON.stringify(state().links))
}

const getters = {
    links: (state: any) => {
        return state.links
    },
    openTrigger: (state: any) => {
        return state.openTrigger
    },
}

const mutations = {
    addLink(state: any, link: BulkDownloadLink) {
        const alreadyExist =
            state.links.findIndex(
                (element: { url: string }) => element.url === link.url
            ) > -1
        if (!alreadyExist) {
            state.links.push(link)
            updateSessionStorage()
        }
    },
    removeLink(state: any, url: number) {
        state.links = state.links.filter(
            (link: { url: number }) => link.url !== url
        )
        updateSessionStorage()
    },
    removeAllLinks(state: any) {
        state.links = []
        updateSessionStorage()
    },
    openTrigger(state: any, value: boolean) {
        state.openTrigger = value
    },
    assignFileId(state: any, { url, fileId }: { url: string; fileId: string }) {
        const fileIndex = state.links.findIndex(
            (element: { url: any }) => element.url === url
        )
        if (fileIndex > -1) {
            state.links[fileIndex].assignedFileId = fileId
        }
        updateSessionStorage()
    },
}

const actions = {
    addLink({ commit }: any, link: BulkDownloadLink) {
        commit('addLink', link)
    },
    removeLink({ commit }: any, url: number) {
        commit('removeLink', url)
    },
    removeAllLinks({ commit }: any) {
        commit('removeAllLinks')
    },
    openTrigger({ commit }: any, value: boolean) {
        commit('openTrigger', value)
    },
    assignFileId({ commit }: any, fileId: number) {
        commit('assignFileId', fileId)
    },
}

export default {
    namespaced: true,
    state,
    getters,
    mutations,
    actions,
}
