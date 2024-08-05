import VueMatomo from 'vue-matomo'
import Vue from 'vue'
import apiClient from '../api/apiClient'
import geossSettings from '../api/module/geoss-settings'
import { AppVueObj } from '~/data/global'
import { SearchEngineGetters } from '~/store/searchEngine/search-engine-getters'
import { GeneralActions } from '~/store/general/general-actions'

export default ({ app, $config }) => {
    const siteId = (async () => {
        const webSettings = await apiClient.$get(
            `${geossSettings.webSettings}/sites/${
                AppVueObj.app.$store.getters[SearchEngineGetters.siteId]
            }/web-settings`,
            {
                headers: {
                    Authorization: '',
                },
            }
        )
        const matomoSiteId = webSettings._embedded.webSettings.find(
            (element) => element.key === 'siteId'
        ).value
        AppVueObj.app.$store.dispatch(
            GeneralActions.setMatomoSiteId,
            matomoSiteId
        )
        return matomoSiteId
    })()
    Vue.use(VueMatomo, {
        router: app.router,
        host: `${$config.adminUrl}/matomo`,
        siteId: siteId,
        enableLinkTracking: true,
        requireConsent: false,
        trackInitialView: true,
        disableCookies: true,
        requireCookieConsent: false,
    })
}
