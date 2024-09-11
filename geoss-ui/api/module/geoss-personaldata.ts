const config = {
    apiUrl: window.location.port === '3000' ? 'http://10.254.7.59:8083/rest' : '/personaldata/rest',
    savedSearchesUrl: '/saved-searches',
    highlightedSeearchesUrl: '/highlighted-searches',
    surveys: '/surveys'
}

export default {
    url: config.apiUrl,
    savedSearches: config.apiUrl + config.savedSearchesUrl,
    savedSearchesByUser:
        config.apiUrl + config.savedSearchesUrl + '/search/byUser',
    savedSearchesByCurrentUser:
        config.apiUrl + config.savedSearchesUrl + '/search/current',
    highlightedSearches: config.apiUrl + config.highlightedSeearchesUrl,
    surveys: config.apiUrl + config.surveys,
}
