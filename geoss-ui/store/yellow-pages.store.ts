import Vue from 'vue'
import Vuex from 'vuex'

import { menu } from '@/store/menu/menu'
import { popup } from '@/store/popup/popup'
import { user } from '@/store/user/user'
import { yellowPagesFilters } from '@/store/yellow-pages-filters/yellow-pages-filters'
import { general } from '@/store/general/general'
import { searchEngine } from './search-engine/search-engine'

Vue.use(Vuex)

export const store = new Vuex.Store({
    modules: {
        general,
        menu,
        popup,
        user,
        yellowPagesFilters,
        searchEngine,
    },
})
