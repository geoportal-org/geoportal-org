const config = {
    apiUrl: 'settings/rest', // 'http://10.254.7.59:8080/rest', //
    apiSettingsUrl: '/api-settings',
    webSettingsUrl: '/web-settings',
    catalogsUrl: '/catalogs',
    viewsUrl: '/views',
    layersUrl: '/layers',
    tutorialTatsUrl: 'tutorial-tags',
}

export default {
    url: config.apiUrl,
    apiSettings: config.apiUrl,
    webSettings: config.apiUrl,
    catalogs: config.apiUrl + config.catalogsUrl,
    views: config.apiUrl + config.viewsUrl,
    layers: config.apiUrl + config.layersUrl,
    tutorialTags: config.apiUrl + config.tutorialTatsUrl,
}
