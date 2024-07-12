import Vue from 'vue'
import Vuex from 'vuex'

import map from '@/store/map'
import generalFilters from '~/store/generalFilters'
import facetedFilters from '~/store/facetedFilters'
import granulaFilters from '~/store/granulaFilters'
import irisFilters from '~/store/irisFilters'
import wcFilters from '~/store/wcFilters' 
import searchEngine from '~/store/searchEngine'
import myWorkspace from '~/store/myWorkspace'
import search from '@/store/search'
import menu from '@/store/menu'
import popup from '@/store/popup'
import fileDownload from '~/store/fileDownload'
import bulkDownload from '~/store/bulkDownload'
import user from '@/store/user'
import extendedView from '~/store/extendedView'
import general from '@/store/general'
import { AppVueObj } from '~/data/global'

Vue.use(Vuex)

export const store = new Vuex.Store({
    modules: {
        general,
        map,
        generalFilters,
        facetedFilters,
        granulaFilters,
        irisFilters,
        wcFilters,
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

AppVueObj.app = new Vue({
    store,
})

export const strict = false
