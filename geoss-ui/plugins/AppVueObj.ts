//@ts-nocheck
// import '@babel/polyfill'
// import '@/assets/loadshp/loadshp'

import Vue from 'vue'
import PortalVue from 'portal-vue'
import Vuebar from 'vuebar'
//import SocialSharing from 'vue-social-sharing'

import VueAnalytics from 'vue-analytics'
import LineClamp from 'vue-line-clamp'
// import 'vue-draggable-resizable/dist/VueDraggableResizable.css'
// import 'element-closest-polyfill'
// import 'events-polyfill/src/constructors/Event.js'

// import App from '@/pages/index.vue'
// import { store } from '@/store/index'

import ClickOutside from '@/directives/click-outside'
import ImagePreview from '@/directives/image-preview'
import HtmlToText from '@/directives/html-to-text'

import { AppVueObj } from '~/data/global'
import { GeneralApiService } from '@/services/general.api.service'
import { GeneralGetters } from '@/store/general/general-getters'

Vue.config.devtools = true

// plugins
Vue.use(LineClamp)
Vue.use(PortalVue)
Vue.use(Vuebar)
//Vue.use(SocialSharing)
Vue.use(VueAnalytics, {
    id: () =>
        GeneralApiService.getSiteSettings().then(
            (response: { analytics: { googleAnalyticsId: any } }) => {
                return response ? response.analytics.googleAnalyticsId : null
            }
        ),
    checkDuplicatedScript: true
})

// directives
Vue.directive('click-outside', ClickOutside)
Vue.directive('image-preview', ImagePreview)
Vue.directive('html-to-text', HtmlToText)

const cookie = 'cookie'
const invalidImages = 'invalidImages'
const staticPath = 'staticPath'

Vue.mixin({
    data() {
        return {
            invalidImages: []
        }
    },
    methods: {
        staticPath() {
            return AppVueObj.app.$store.getters[GeneralGetters.staticPath]
        },
        getImage(imagePath: string) {
            if (
                !imagePath ||
                typeof imagePath !== 'string' ||
                (this as any)[invalidImages].indexOf(imagePath) !== -1
            ) {
                return `/svg/geoss-gray.svg`
            } else if (imagePath.startsWith('http:')) {
                return imagePath.replace('http:', 'https:')
            }
            return imagePath
        },
        imageLoadError(imagePath) {
            ;(this as any)[invalidImages].push(imagePath)
        }
    }
})
