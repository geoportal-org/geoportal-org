import { DownloadFile, DownloadFileStatus } from '@/interfaces/DownloadFile'
import apiService from '@/services/geoss-search.api.service'

const state = () => ({
    files: [] as DownloadFile[],
    openTrigger: false as boolean
})

const updateSessionStorage = () => {
    sessionStorage.setItem('fileDownload', JSON.stringify(state().files))
}

const getters = {
    files: (state: any) => {
        return state.files
    },
    openTrigger: (state: any) => {
        return state.openTrigger
    }
}

const mutations = {
    addFile(state: any, file: DownloadFile) {
        state.files.push(file)
        updateSessionStorage()
    },
    removeFile(state: any, id: number) {
        const file: DownloadFile = state.files.find(
            (file: DownloadFile) => file.id === id
        )
        clearTimeout(file.progressData.timeout)
        file.progressData.timeout = 'cancel'
        state.files = state.files.filter((file: DownloadFile) => file.id !== id)
        updateSessionStorage()
    },
    removeAllFiles(state: any) {
        for (const file of state.files) {
            clearTimeout(file.progressData.timeout)
            file.progressData.timeout = 'cancel'
        }
        state.files = []
        updateSessionStorage()
    },
    changeFileStatus(
        state: any,
        {
            id,
            status,
            statusObject
        }: { id: number; status: DownloadFileStatus; statusObject: any }
    ) {
        const file: DownloadFile = state.files.find(
            (file: DownloadFile) => file.id === id
        )
        if (file) {
            file.status = status
            file.statusObject = statusObject
        }
        state.files = state.files.splice(0)
        updateSessionStorage()
    },
    changeFileUrl(
        state: any,
        { id, url, download }: { id: number; url: string; download: string }
    ) {
        const file: DownloadFile = state.files.find(
            (file: DownloadFile) => file.id === id
        )
        if (file) {
            file.url = url
            file.download = download
        }
        updateSessionStorage()
    },
    openTrigger(state: any, value: boolean) {
        state.openTrigger = value
    }
}

const actions = {
    addFile({ commit }: any, file: DownloadFile) {
        if (
            file.status !== DownloadFileStatus.ready &&
            file.status !== DownloadFileStatus.failed
        ) {
            const progressData = apiService.checkAsyncDownloadStatus(file)
            file.progressData = progressData
            progressData.promise.then((data: any) => {
                if (data === 'error') {
                    commit('changeFileStatus', {
                        id: file.id,
                        status: DownloadFileStatus.failed
                    })
                } else if (data && data.status === 'error') {
                    sessionStorage.setItem(
                        'fileId_' + file.id,
                        JSON.stringify(file)
                    )
                    commit('changeFileStatus', {
                        id: file.id,
                        status: DownloadFileStatus.failed,
                        statusObject: data.response
                    })
                } else {
                    commit('changeFileUrl', {
                        id: file.id,
                        url: data.url || data,
                        download: data.download
                    })
                    commit('changeFileStatus', {
                        id: file.id,
                        status: DownloadFileStatus.ready
                    })
                }
            })
        }
        commit('addFile', file)
    },
    removeFile({ commit }: any, id: number) {
        commit('removeFile', id)
    },
    removeAllFiles({ commit }: any) {
        commit('removeAllFiles')
    },
    openTrigger({ commit }: any, value: boolean) {
        commit('openTrigger', value)
    }
}

export default {
    namespaced: true,
    state,
    getters,
    mutations,
    actions
}
