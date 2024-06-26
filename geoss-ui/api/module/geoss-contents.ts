const config = {
    apiUrl: '/contents/rest', //  'http://10.254.7.59:8082/rest', //
    menuUrl: '/menu/search/findByLevelIdAndSiteId',
    pageUrl: '/page',
    contentUrl: '/content',
    documentUrl: '/document',
    siteUrl: '/site',
}

export default {
    url: config.apiUrl,
    menu: config.apiUrl + config.menuUrl + '?page=0&size=9999',
    page: config.apiUrl + config.pageUrl + '?size=9999',
    content: config.apiUrl + config.contentUrl,
    document: config.apiUrl + config.documentUrl,
    site: config.apiUrl + config.siteUrl,
    siteByUrl: config.apiUrl + config.siteUrl + '/search/findByUrl',
}
