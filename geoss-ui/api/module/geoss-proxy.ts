const config = {
    apiUrl: '/proxy/rest', //'http://10.254.7.59:8084/rest', // 
    popularUrl: '/popular',
    logSearchUrl: '/log/logSearchResult',
    logElementClickUrl: '/log/logElementClick',
    logResourceErrorUrl: '/log/logResourceError',
    logSignInUrl: '/log/logSignIn',
}

export default {
    url: config.apiUrl,
    popular: config.apiUrl + config.popularUrl,
    logSearch: config.apiUrl + config.logSearchUrl,
    logElementClick: config.apiUrl + config.logElementClickUrl,
    logResourceError: config.apiUrl + config.logResourceErrorUrl,
    logSignIn: config.apiUrl + config.logSignInUrl,
}
