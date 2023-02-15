import Vue from 'vue'
import Vuex from 'vuex'

import { map } from '@/store/map/map'
import { generalFilters } from '@/store/general-filters/general-filters'
import { facetedFilters } from '@/store/faceted-filters/faceted-filters'
import { granulaFilters } from '@/store/granula-filters/granula-filters'
import { irisFilters } from '@/store/iris-filters/iris-filters'
import { searchEngine } from '@/store/search-engine/search-engine'
import { myWorkspace } from '@/store/my-workspace/my-workspace'
import { search } from '@/store/search/search'
import { menu } from '@/store/menu/menu'
import { popup } from '@/store/popup/popup'
import { fileDownload } from '@/store/file-download/file-download'
import { bulkDownload } from '@/store/bulk-download/bulk-download'
import { user } from '@/store/user/user'
import { extendedView } from '@/store/extended-view/extended-view'
import { general } from '@/store/general/general'

Vue.use(Vuex)

export default new Vuex.Store({
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
