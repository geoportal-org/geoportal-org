import apiClient from './apiClient'
import geossPersonaldata from './module/geoss-personaldata'
import { buildSavedSearchUrl } from '@/utils/urlBuilder'

const UserAPI = {
    addSavedSearch: (searchData: any) => {
        const url = `${geossPersonaldata.savedSearches}`
        const phrase = searchData.name
        const savedSearchUrl = buildSavedSearchUrl(searchData)

        return apiClient.$post(url, {
            name: phrase,
            phrase: phrase,
            url: savedSearchUrl,
        })
    },

    getSavedSearchesByUser: async (userId: string | null = null) => {
        let url: string = ''
        if (userId && userId.length) {
            url = `${geossPersonaldata.savedSearchesByUser}?user=${userId}`
        } else {
            url = `${geossPersonaldata.savedSearchesByCurrentUser}`
        }
        const savedSearchesRaw = await apiClient.$get(url)
        const savedSearches = savedSearchesRaw._embedded.savedSearches
        return savedSearches
    },

    deleteSavedSearch: (savedSearchId: number) => {
        const url = `${geossPersonaldata.savedSearches}/${savedSearchId}`
        return apiClient.$delete(url)
    },

    highlightSavedSearch: (savedSearchId: number, name: string) => {
        const url = `${geossPersonaldata.savedSearches}/${savedSearchId}/highlighted`
        return apiClient.$post(url, {
            name,
        })
    },

    getHighlightedSearches: async (admin: boolean = false) => {
        let url: string = ''
        let highlightedSearchesRaw = []
        if (!admin) {
            url = `${geossPersonaldata.highlightedSearches}/search/enabled`
            highlightedSearchesRaw = await apiClient.$get(url, {
                headers: {
                    Authorization: '',
                },
            })
        } else {
            url = `${geossPersonaldata.highlightedSearches}`
            highlightedSearchesRaw = await apiClient.$get(url)
        }
        const highlightedSearches =
            highlightedSearchesRaw._embedded.highlightedSearches
        return highlightedSearches
    },

    updateHighlightedSearch: (highlightedSearchId: number, data: any) => {
        const url = `${geossPersonaldata.highlightedSearches}/${highlightedSearchId}`
        return apiClient.$patch(url, data)
    },

    deleteHighlightedSearch: (highlightedSearchId: number) => {
        const url = `${geossPersonaldata.highlightedSearches}/${highlightedSearchId}`
        return apiClient.$delete(url)
    },
}

export default UserAPI
