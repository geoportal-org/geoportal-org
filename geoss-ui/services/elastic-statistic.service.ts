const ElasticDataService = {
    async fetchElasticData(
        datasetOption: string,
        dateFrom: string,
        dateTo: string,
        interval: string,
        unit: string,
        typeIndex: string
    ) {
        const dsSourceKey =
            ElasticDataService.getDsSourceKey(datasetOption) || ''
        const dateFromTs =
            ElasticDataService.getTimestampFromDate(dateFrom).toString()
        const dateToTs =
            ElasticDataService.getTimestampFromDate(dateTo).toString()
        const intervalElk = ElasticDataService.createElasticInterval(
            interval,
            unit
        )
        switch (typeIndex) {
            case window.$nuxt.$tc(
                'statistics.dataTypeOptions.numberOfSearches'
            ):
                return await this.fetchNumberOfSearches(
                    dsSourceKey,
                    dateFromTs,
                    dateToTs,
                    intervalElk
                )
        }
    },

    async fetchNumberOfSearches(
        dsSourceKey: string,
        dateFromTs: string,
        dateToTs: string,
        intervalElk: string
    ) {
        try {
            let response = await fetch(
                `https://gpp.devel.esaportal.eu/proxy/rest/statistics/getNumberOfSearches?source=%7B%22dsSourcesGroupKey%22%3A%22${dsSourceKey}%22%2C%22period%22%3A%7B%22min%22%3A${dateFromTs}%2C%22max%22%3A${dateToTs}%7D%2C%22interval%22%3A%22${intervalElk}%22%7D`,
                // 'https://gpp.devel.esaportal.eu/proxy/rest/statistics/getNumberOfSearches',
                {
                    method: 'GET',
                    headers: {
                        Authorization:
                            'Bearer ' +
                            window.$nuxt.$cookies.get('elkAuthToken'),
                        'Content-Type': 'application/json',
                    },
                    // body: JSON.stringify({
                    //     dsSourcesGroupKey: dsSourceKey,
                    //     period: {
                    //       "min": dateFromTs,
                    //       "max": dateToTs
                    //     },
                    //     interval: intervalElk
                    //   })
                }
            )
        } catch (error) {
            console.log(error)
        }
        return true
    },

    getDsSourceKey(datasetOption: string) {
        const options = {
            'All catalogs': '',
            'CORINE Land Cover': 'clcglobalid',
            'European NUTS 2': 'eunuts2id',
            'French Virtual Hub': 'frvhub',
            'German Virtual Hub': 'devhub',
            'Italian Virtual Hub': 'itvhub',
            'Noise Capture Data': 'ncdataid',
            'Polish Virtual Hub': 'plvhub',
            'SOS SensorODP Alkante': 'sosalkantekiwi',
            'Sentinel': 'sentinelscihudtest',
            'Spanish Virtual Hub': 'esvhub',
            'USGS Landsat 8': 'landsat8dbidawstest',
        }

        for (const [key, value] of Object.entries(options)) {
            if (datasetOption === key) {
                return value
            }
        }
    },

    getTimestampFromDate(date: string) {
        return date ? new Date(date).valueOf() : Date.now()
    },

    createElasticInterval(interval: string, unit: string) {
        return unit === 'Month'
            ? interval + 'M'
            : interval + unit.toString().toLowerCase()[0]
    },
}

export default ElasticDataService
