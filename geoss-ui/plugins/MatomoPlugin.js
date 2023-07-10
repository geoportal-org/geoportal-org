import VueMatomo from 'vue-matomo'
import Vue from 'vue'

export default ({ app }) => {
    Vue.use(VueMatomo, {
        router: app.router,
        host: 'https://gpp-admin.devel.esaportal.eu/matomo/',
        siteId: 2,
        enableLinkTracking: true,
        requireConsent: false,
        trackInitialView: true,
        disableCookies: true,
        requireCookieConsent: false,
    })
}
