const config = {
    apiUrl:  'http://10.254.7.59:8080/rest', //'settings/rest',  //  
    apiSettingsUrl: '/api-settings',
    webSettingsUrl: '/web-settings',
    catalogsUrl: '/catalogs',
    viewsUrl: '/views',
    layersUrl: '/layers',
    tutorialTatsUrl: 'tutorial-tags',
}

export default {
    url: config.apiUrl,
    apiSettings: config.apiUrl + config.apiSettingsUrl,
    webSettings: config.apiUrl + config.webSettingsUrl,
    catalogs: config.apiUrl + config.catalogsUrl,
    views: config.apiUrl + config.viewsUrl,
    layers: config.apiUrl + config.layersUrl,
    tutorialTags: config.apiUrl + config.tutorialTatsUrl,
}
