const config = {
    apiUrl: '/contents/rest', // 'http://10.254.7.59:8082/rest',
    menuUrl: '/menu',
    pageUrl: '/page',
    contentUrl: '/content',
}

export default {
    url: config.apiUrl,
    menu: config.apiUrl + config.menuUrl + '?size=9999',
    page: config.apiUrl + config.pageUrl + '?size=9999',
    content: config.apiUrl + config.contentUrl,
}
