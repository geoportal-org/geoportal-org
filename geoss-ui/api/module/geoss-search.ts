const config = {
    apiUrl: '/search/rest', // 'http://10.254.7.59:8086/rest', //
    conceptsUrl: '/concepts',
}

export default {
    url: config.apiUrl,
    concepts: config.apiUrl + config.conceptsUrl,
}
