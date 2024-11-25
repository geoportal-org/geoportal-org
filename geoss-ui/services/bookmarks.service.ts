const BookmarksService: any = {
    async fetchBookmarkedResults(
        page: number,
        size = 10
    ) {
        // @ts-ignore
        const token = window.$nuxt.$auth.getToken('keycloak')
        // @ts-ignore
        const userId = window.$nuxt.$auth.$state.user.sub
        try {
            let response = await fetch(
                window.$nuxt.$config.curatedUrl +
                    'bookmarked/search/findBookmarksByUserId' +
                    '?userId=' +
                    userId +
                    '&page=' +
                    page +
                    '&size=' +
                    size,
                {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                        Authorization: token ? token : '',
                    },
                }
            )
            if (response.status === 200) {
                let rJson = await response.json()
                return rJson
            }
        } catch (error) {
            console.log(error)
        }
    },

    async addBookmark(
        title: string,
        id: string,
        currMap: any,
        dataSource: string
    ) {
        // @ts-ignore
        const token = window.$nuxt.$auth.getToken('keycloak')
        // @ts-ignore
        const userId = window.$nuxt.$auth.$state.user.sub
        const body = {
            name: title,
            targetId: id,
            userId: userId || '',
            currMap: currMap || '',
            dataSource: dataSource ? dataSource : 'dab',
        }
        try {
            let response = await fetch(
                window.$nuxt.$config.curatedUrl + 'bookmarked/add',
                {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        Authorization: token ? token : '',
                    },
                    body: JSON.stringify(body),
                }
            )
            console.log(response)
            if (response.status === 200 || response.status === 201) {
                return true
            }
        } catch (e: any) {
            console.log(e)
        }
    },

    async removeBookmark(targetId: string, dataSource: string) {
        // @ts-ignore
        const token = window.$nuxt.$auth.getToken('keycloak')
        try {
            let response = await fetch(
                window.$nuxt.$config.curatedUrl +
                    'bookmarked/delete/byTargetIdAndDataSource?targetId=' +
                    targetId +
                    '&dataSource=' +
                    dataSource,
                {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/json',
                        Authorization: token ? token : '',
                    },
                }
            )
            if (response.status === 202) {
                return true
            }
        } catch (e: any) {
            console.log(e)
        }
    },

    async getBookmark() {
        // @ts-ignore
        const token = window.$nuxt.$auth.getToken('keycloak')
        const id = 1
        try {
            let response = await fetch(
                window.$nuxt.$config.curatedUrl + 'bookmarked/' + id,
                {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                        Authorization: token ? token : '',
                    },
                }
            )
            if (response.status === 200) {
                let rJson = await response.json()
                return rJson
            }
        } catch (error) {
            console.log(error)
        }
    },
}

export default BookmarksService
