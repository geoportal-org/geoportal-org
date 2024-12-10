const config = {
    apiUrl:  window.location.port === '3000' ? 'https://gpp.devel.esaportal.eu/search/rest' : '/search/rest',
    conceptsUrl: '/concepts',
    recommendationsUrl: '/recommendations'
}

export default {
    url: config.apiUrl,
    concepts: config.apiUrl + config.conceptsUrl,
    recommendations: config.apiUrl + config.recommendationsUrl
}
