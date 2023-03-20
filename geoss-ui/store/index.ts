import Vue from 'vue'
import Vuex from 'vuex'

import map from '@/store/map'
import generalFilters from '@/store/general-filters'
import facetedFilters from '@/store/faceted-filters'
import granulaFilters from '@/store/granula-filters'
import irisFilters from '@/store/iris-filters'
import searchEngine from '@/store/search-engine'
import myWorkspace from '@/store/my-workspace'
import search from '@/store/search'
import menu from '@/store/menu'
import popup from '@/store/popup'
import fileDownload from '@/store/file-download'
import bulkDownload from '@/store/bulk-download'
import user from '@/store/user'
import extendedView from '@/store/extended-view'
import general from '@/store/general'

Vue.use(Vuex)

export const store = new Vuex.Store({
    modules: {
        general,
        map,
        generalFilters,
        facetedFilters,
        granulaFilters,
        irisFilters,
        searchEngine,
        myWorkspace,
        search,
        menu,
        popup,
        fileDownload,
        bulkDownload,
        user,
        extendedView,
    },
})
