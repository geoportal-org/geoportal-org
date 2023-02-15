import Vue from 'vue'
import Vuex from 'vuex'

import { feedback } from '@/store/feedback/feedback'
import { myWorkspace } from '@/store/my-workspace/my-workspace'
import { menu } from '@/store/menu/menu'
import { popup } from '@/store/popup/popup'
import { user } from '@/store/user/user'
import { general } from '@/store/general/general'
import { searchEngine } from '@/store/search-engine/search-engine'
import { map } from '@/store/map/map'

Vue.use(Vuex)

export const store = new Vuex.Store({
    modules: {
        general,
        myWorkspace,
        menu,
        popup,
        user,
        feedback,
        searchEngine,
        map,
    },
})
