const config = {
    apiUrl: 'http://10.254.7.59:8086/rest', // '/search/rest', //'http://10.254.7.59:8086/rest', //
    conceptsUrl: '/concepts',
    recommendationsUrl: '/recommendations'
}

export default {
    url: config.apiUrl,
    concepts: config.apiUrl + config.conceptsUrl,
    recommendations: config.apiUrl + config.recommendationsUrl
}
