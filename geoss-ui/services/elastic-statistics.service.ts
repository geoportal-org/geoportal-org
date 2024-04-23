import { getRandomColorsArray } from '~/utils/randomColorsArray'
import UtilsService from './utils.service'

const ElasticDataService = {
    async fetchElasticData(
        datasetOption: string,
        dateFrom: string,
        dateTo: string,
        interval: string,
        unit: string,
        type: string,
        resultsNumber: number,
        token: string
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
        let endpoint = ''
        let body = {}
        if (
            type ===
            window.$nuxt.$tc('statistics.dataTypeOptions.numberOfSearches')
        ) {
            endpoint = 'getNumberOfSearches'
            body = {
                dsSourcesGroupKey: dsSourceKey,
                period: {
                    min: dateFromTs,
                    max: dateToTs,
                },
                interval: intervalElk,
                results: 0,
            }
        } else if (
            type ===
            window.$nuxt.$tc('statistics.dataTypeOptions.popularResources')
        ) {
            endpoint = 'getMostPopularResources'
            body = {
                period: {
                    min: dateFromTs,
                    max: dateToTs,
                },
                size: 10,
                results: resultsNumber,
            }
        } else if (
            type ===
            window.$nuxt.$tc('statistics.dataTypeOptions.popularKeywords')
        ) {
            endpoint = 'getMostPopularKeywords'
            body = {
                dsSourcesGroupKey: dsSourceKey,
                period: {
                    min: dateFromTs,
                    max: dateToTs,
                },
                results: resultsNumber,
            }
        } else if (
            type ===
            window.$nuxt.$tc('statistics.dataTypeOptions.popularCatalogs')
        ) {
            endpoint = 'getMostPopularCatalogs'
            body = {
                period: {
                    min: dateFromTs,
                    max: dateToTs,
                },
                results: resultsNumber,
            }
        } else if (
            type ===
            window.$nuxt.$tc('statistics.dataTypeOptions.popularOrganizations')
        ) {
            endpoint = 'getMostPopularOrganizations'
            body = {
                period: {
                    min: dateFromTs,
                    max: dateToTs,
                },
                size: 10,
                results: resultsNumber,
            }
        } else {
            endpoint = 'getMostPopularAreas'
            body = {
                dsSourcesGroupKey: dsSourceKey,
                period: {
                    min: dateFromTs,
                    max: dateToTs,
                },
                results: resultsNumber,
            }
        }
        const data = await this.fetchData(endpoint, body, token)
        return this.prepareChartData(data, type)
    },

    async fetchData(endpoint: string, body: any, token: string) {
        try {
            let response = await fetch(window.$nuxt.$config.proxyUrl + 'statistics/' + endpoint, {
                method: 'POST',
                headers: {
                    Authorization: token,
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(body),
            })
            if (response.status === 200) {
                let rJson = await response.json()
                return rJson
            }
        } catch (error) {
            console.log(error)
        }
    },

    prepareChartData(data: any, type: string) {
        const chartType =
            type ===
            window.$nuxt.$tc('statistics.dataTypeOptions.numberOfSearches')
                ? 'line'
                : 'bar'
        if (data) {
            let label = type
            let labels: string[] = []
            let values: unknown[] = []
            let colors: string[] = []
            let options = {}
            switch (chartType) {
                case 'line':
                    if (data.length) {
                        data.forEach((element: any) => {
                            const label =
                                element.key === ''
                                    ? window.$nuxt.$tc(
                                          'statistics.unspecifiedLabel'
                                      )
                                    : element.keyAsString.slice(0, 10)
                            labels.push(label)
                            values.push(element.docCount)
                        })
                    }
                    const lineChartData = {
                        labels: labels,
                        datasets: [
                            {
                                label: label,
                                data: values,
                                fill: false,
                                radius: '5',
                                pointHoverRadius: '5',
                                borderColor: '#f70707',
                            },
                        ],
                    }
                    options = {
                        responsive: true,
                        scales: {
                            x: {
                                title: {
                                    display: true,
                                    text: window.$nuxt.$tc(
                                        'statistics.dateLabel'
                                    ),
                                },
                            },
                            y: {
                                title: {
                                    display: true,
                                    text: label,
                                },
                            },
                        },
                    }
                    return {
                        type: 'line',
                        data: lineChartData,
                        options: options,
                    }
                case 'bar':
                    if (data.length) {
                        data.forEach((element: any) => {
                            const label =
                                element.key === ''
                                    ? window.$nuxt.$tc(
                                          'statistics.unspecifiedLabel'
                                      )
                                    : element.key
                            labels.push(label)
                            values.push(element.docCount.toString())
                        })
                    }
                    colors = getRandomColorsArray(labels.length)
                    const barChartData = {
                        labels: labels,
                        datasets: [
                            {
                                label: label,
                                data: values,
                                backgroundColor: colors,
                                borderColor: colors,
                                borderWidth: '1',
                            },
                        ],
                    }
                    options = {
                        responsive: true,
                        plugins: {
                            tooltip: {
                                callbacks: {
                                    label: function (context: any) {
                                        const value = context.formattedValue
                                        const label = window.$nuxt.$tc(
                                            'statistics.numberOfSearches'
                                        )
                                        const final = label + ': ' + value

                                        return final
                                    },
                                },
                            },
                        },
                        scales: {
                            x: {
                                title: {
                                    display: true,
                                    text: type,
                                },
                            },
                            y: {
                                title: {
                                    display: true,
                                    text: window.$nuxt.$tc(
                                        'statistics.numberOfSearches'
                                    ),
                                },
                            },
                        },
                    }

                    return {
                        type: 'bar',
                        data: barChartData,
                        options: options,
                    }
                default:
                    break
            }
        }
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
            Sentinel: 'sentinelscihudtest',
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
        return unit === window.$nuxt.$tc('statistics.timeUnits.month')
            ? interval + 'M'
            : interval + unit.toString().toLowerCase()[0]
    },
}

export default ElasticDataService
