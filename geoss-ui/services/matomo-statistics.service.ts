//to change
import { getRandomColorsArray } from '~/utils/randomColorsArray'

type matomoParams = {
    module: string
    method: string
    idSite: string
    period: string
    date: string
    format: string
    filter_limit: string
    token_auth: string
}

const MatomoDataService = {
    parseParams(params: matomoParams) {
        let parsedParams = ''

        Object.keys(params).forEach((key, index) => {
            parsedParams += `${!index ? '' : '&'}${key}=${
                params[key as keyof matomoParams]
            }`
        })

        return parsedParams
    },

    prepareRequestParams(
        method: string,
        unit: string,
        dateFrom: string,
        dateTo: string,
        resultsNumber: string,
    ) {
        let currentDate = new Date().toJSON().slice(0, 10)
        const start = dateFrom
        const end = dateTo || currentDate
        const dateRange = start + ',' + end

        let params: matomoParams = {
            module: 'API',
            method: '',
            idSite: '1',
            period: MatomoDataService.getPeriod(unit),
            date: dateRange,
            format: 'json',
            filter_limit: resultsNumber,
            token_auth: window.$nuxt.$config.matomoToken,
        }

        let chartType = ''
        switch (method) {
            case window.$nuxt.$tc('statistics.siteTypeOptions.usersCountries'):
                params.method = 'UserCountry.getCountry'
                chartType = 'bar'
                break
            case window.$nuxt.$tc('statistics.siteTypeOptions.popularBrowsers'):
                params.method = 'DevicesDetection.getBrowsers'
                chartType = 'pie'
                break
            case window.$nuxt.$tc('statistics.siteTypeOptions.returningUsers'):
                params.method = 'VisitFrequency.get'
                chartType = 'pie'
                break
            case window.$nuxt.$tc('statistics.siteTypeOptions.bounceRate'):
                params.method = 'VisitFrequency.get'
                chartType = 'line'
                break
            case window.$nuxt.$tc('statistics.siteTypeOptions.numberOfUsers'):
                params.method = 'VisitsSummary.getUniqueVisitors'
                chartType = 'line'
                break
            case window.$nuxt.$tc(
                'statistics.siteTypeOptions.numberOfSessions'
            ):
                params.method = 'VisitsSummary.getVisits'
                chartType = 'line'
                break
            default:
                params.method = ''
        }
        return [params, chartType]
    },

    async prepareChartData(
        method: string,
        unit: string,
        dateFrom: string,
        dateTo: string,
        resultsNumber: string,
    ) {
        const [params, chartType]: any = this.prepareRequestParams(
            method,
            unit,
            dateFrom,
            dateTo,
            resultsNumber,
        )

        const data = await this.fetchMatomoData(
            window.$nuxt.$config.matomoUrl + this.parseParams(params)
        )

        if (data) {
            let label = method
            let labels: string[] = []
            let values: unknown[] = []
            let colors: string[] = []
            let options = {}
            switch (chartType) {
                case 'line':
                    for (const [key, value] of Object.entries(data)) {
                        if (unit.toLowerCase() === 'week') {
                            labels.push(key.slice(11))
                        } else {
                            labels.push(key)
                        }
                        if (
                            method ===
                            window.$nuxt.$tc(
                                'statistics.siteTypeOptions.bounceRate'
                            )
                        ) {
                            //@ts-ignore
                            values.push(parseFloat(value.bounce_rate_new))
                            label = window.$nuxt.$tc(
                                'statistics.bounceRateLabel'
                            )
                        } else {
                            values.push(value)
                        }
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
                        type: chartType,
                        data: lineChartData,
                        options: options,
                    }
                case 'pie':
                    if (
                        method ===
                        window.$nuxt.$tc(
                            'statistics.siteTypeOptions.popularBrowsers'
                        )
                    ) {
                        if (data.length) {
                            data.forEach((element: any) => {
                                labels.push(element.label)
                                values.push(element.nb_actions)
                            })
                        }
                    } else {
                        labels = [
                            window.$nuxt.$tc('statistics.newVisitorsLabel'),
                            window.$nuxt.$tc(
                                'statistics.returningVisitorsLabel'
                            ),
                        ]
                        values = [data.nb_visits_new, data.nb_visits_returning]
                    }

                    colors = getRandomColorsArray(labels.length)

                    const pieChartData = {
                        labels: labels,
                        datasets: [
                            {
                                label: label,
                                data: values,
                                backgroundColor: colors,
                                hoverOffset: 4,
                                spacing: 2,
                                radius: '90%',
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
                                        const sum = context.dataset.data.reduce(
                                            (partialSum: any, a: any) =>
                                                partialSum + a,
                                            0
                                        )
                                        const value = context.formattedValue
                                        const percentageValue = (
                                            (value / sum) *
                                            100
                                        ).toFixed(1)
                                        const label = context.label
                                        const final =
                                            label +
                                            ': ' +
                                            value +
                                            ' [' +
                                            percentageValue +
                                            '%' +
                                            ']'
                                        return final
                                    },
                                },
                            },
                            title: {
                                display: true,
                                text:
                                    method ===
                                    window.$nuxt.$tc(
                                        'statistics.siteTypeOptions.popularBrowsers'
                                    )
                                        ? window.$nuxt.$tc(
                                              'statistics.popularBrowsersTitle'
                                          )
                                        : window.$nuxt.$tc(
                                              'statistics.visitorTypeTitle'
                                          ),
                            },
                        },
                    }

                    return {
                        type: chartType,
                        data: pieChartData,
                        options: options,
                    }
                case 'bar':
                    const mapData: any[][] = []
                    mapData.push([
                        window.$nuxt.$tc('statistics.country'),
                        window.$nuxt.$tc(
                            'statistics.siteTypeOptions.numberOfUsers'
                        ),
                    ])
                    if (data.length) {
                        data.forEach((element: any) => {
                            labels.push(element.label)
                            values.push(element.nb_visits)
                            mapData.push([
                                element.code.toUpperCase(),
                                element.nb_visits,
                            ])
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
                                            'statistics.numberOfVisits'
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
                                    text: window.$nuxt.$tc(
                                        'statistics.country'
                                    ),
                                },
                            },
                            y: {
                                title: {
                                    display: true,
                                    text: window.$nuxt.$tc(
                                        'statistics.numberOfVisits'
                                    ),                                },
                            },
                        },
                    }

                    return {
                        type: chartType,
                        data: barChartData,
                        options: options,
                        isCountries: true,
                        mapData: mapData,
                    }
                default:
                    break
            }
        }
    },

    async fetchMatomoData(url: string) {
        try {
            let response = await fetch(url)
            let result = await response.json()
            return result
        } catch (error) {
            console.log(error)
        }
    },
    getPeriod(period: string) {
        switch (period) {
            case window.$nuxt.$tc('statistics.timeUnits.day'):
                return 'day'
            case window.$nuxt.$tc('statistics.timeUnits.week'):
                return 'week'
            case window.$nuxt.$tc('statistics.timeUnits.month'):
                return 'month'
            case window.$nuxt.$tc('statistics.timeUnits.year'):
                return 'year'
            default:
                return 'range'
        }
    },
}

export default MatomoDataService
