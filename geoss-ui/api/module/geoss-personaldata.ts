const config = {
    apiUrl: window.location.port === '3000' ? 'https://gpp.devel.esaportal.eu/personaldata/rest' : '/personaldata/rest',
    savedRunsUrl: '/saved-runs',
    savedSearchesUrl: '/saved-searches',
    highlightedSeearchesUrl: '/highlighted-searches',
    surveys: '/surveys',
    feedbacks: '/feedbacks'
}

export default {
    url: config.apiUrl,
    savedRuns: config.apiUrl + config.savedRunsUrl,
    savedSearches: config.apiUrl + config.savedSearchesUrl,
    savedSearchesByUser:
        config.apiUrl + config.savedSearchesUrl + '/search/byUser',
    savedSearchesByCurrentUser:
        config.apiUrl + config.savedSearchesUrl + '/search/current',
    highlightedSearches: config.apiUrl + config.highlightedSeearchesUrl,
    surveys: config.apiUrl + config.surveys,
    feedbacks: config.apiUrl + config.feedbacks,
}
