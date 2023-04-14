let client: any

export function setClient(newclient: any) {
    client = newclient
}

// Request helpers
const reqMethods = [
    '$request',
    '$delete',
    '$get',
    '$head',
    '$options', // url, config
    '$post',
    '$put',
    '$patch', // url, data, config
]

let service: any = {}

reqMethods.forEach((method) => {
    service[method] = function () {
        if (!client) throw new Error('apiClient not installed')
        return client[method].apply(null, arguments)
    }
})

export default service
