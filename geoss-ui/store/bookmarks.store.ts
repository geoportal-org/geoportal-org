import Vue from 'vue'
import Vuex from 'vuex'

import { map } from '@/store/map/map'
import { myWorkspace } from '@/store/my-workspace/my-workspace'
import { menu } from '@/store/menu/menu'
import { popup } from '@/store/popup/popup'
import { user } from '@/store/user/user'
import { general } from '@/store/general/general'
import { bookmarks } from '@/store/bookmarks/bookmarks'
import { searchEngine } from '@/store/search-engine/search-engine'
import { fileDownload } from '@/store/file-download/file-download'
import { bulkDownload } from '@/store/bulk-download/bulk-download'
import { search } from '@/store/search/search'

Vue.use(Vuex)

export const store = new Vuex.Store({
    modules: {
        general,
        map,
        myWorkspace,
        menu,
        popup,
        user,
        bookmarks,
        searchEngine,
        fileDownload,
        bulkDownload,
        search,
    },
})
