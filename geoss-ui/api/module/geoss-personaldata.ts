const config = {
    apiUrl: 'http://10.254.7.59:8083/rest', // , '/personaldata/rest', //'http://10.254.7.59:8083/rest', // ,
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
