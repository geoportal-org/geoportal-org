const RatingService: any = {
    async fetchRating(entryId: string) {
        try {
            let response = await fetch(
                window.$nuxt.$config.curatedUrl +
                    'rating/search/findRatingsByTargetIdsAndDataSource' +
                    '?targetIds=' +
                    entryId +
                    '&dataSource=dab',
                {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
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

    async fetchComments(entryId: string) {
        try {
            let response = await fetch(
                window.$nuxt.$config.curatedUrl +
                    'rating/search/findCommentsByTargetIdAndDataSource' +
                    '?targetId=' +
                    entryId +
                    '&dataSource=dab',
                {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
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

    async rateWithoutComment(
        entryId: string,
        title: string,
        score: number
    ) {
        const body = {
            name: title,
            targetId: entryId,
            score: score || 0,
            dataSource: 'dab',
        }
        try {
            let response = await fetch(
                window.$nuxt.$config.curatedUrl + 'rating/rate/withoutComment',
                {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify(body),
                }
            )
            if (response.status === 200 || response.status === 201) {
                let rJson = await response.json()
                return rJson
            }
        } catch (error) {
            console.log(error)
        }
    },

    async rateWithComment(
        entryId: string,
        title: string,
        token: any,
        score: number,
        comment: string
    ) {
        const body = {
            name: title,
            targetId: entryId,
            score: score || 0,
            comment: comment || '',
            dataSource: 'dab',
        }
        try {
            let response = await fetch(
                window.$nuxt.$config.curatedUrl + 'rating/rate/withComment',
                {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        Authorization: token,
                    },
                    body: JSON.stringify(body),
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

export default RatingService
