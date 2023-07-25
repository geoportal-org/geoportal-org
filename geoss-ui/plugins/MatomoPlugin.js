import VueMatomo from 'vue-matomo'
import Vue from 'vue'

export default ({ app, $config }) => {
    Vue.use(VueMatomo, {
        router: app.router,
        host: `${$config.adminUrl}/matomo`,
        siteId: $config.matomoSiteId,
        enableLinkTracking: true,
        requireConsent: false,
        trackInitialView: true,
        disableCookies: true,
        requireCookieConsent: false,
    })
}
