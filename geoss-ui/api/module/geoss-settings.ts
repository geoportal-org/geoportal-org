const config = {
    apiUrl: window.location.port === '3000' ? 'https://gpp.devel.esaportal.eu/settings/rest' : '/settings/rest',
    apiSettingsUrl: '/api-settings',
    webSettingsUrl: '/web-settings',
    catalogsUrl: '/catalogs',
    viewsUrl: '/views',
    layersUrl: '/layers',
    tutorialTatsUrl: '/tags',
}

export default {
    url: config.apiUrl,
    apiSettings: config.apiUrl,
    webSettings: config.apiUrl,
    catalogs: config.apiUrl + config.catalogsUrl + '?page=0&size=9999',
    views: config.apiUrl + config.viewsUrl,
    layers: config.apiUrl + config.layersUrl,
    tutorialTags: config.apiUrl + config.tutorialTatsUrl + '?page=0&size=9999',
}
