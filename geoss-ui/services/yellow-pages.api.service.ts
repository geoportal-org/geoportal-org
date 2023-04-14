import { YellowPagesFiltersGetters } from '~/store/yellowPagesFilters/yellow-pages-filters-getters'
import { makeRequest } from './general.api.service'
import { AppVueObj } from '~/data/global'
import { SearchEngineGetters } from '~/store/searchEngine/search-engine-getters'

let staticOrganizations: any[] = []

const YellowPagesApiService = {
    getProviders() {
        return new Promise((resolve) => {
            if (!staticOrganizations.length) {
                return makeRequest(
                    'get',
                    AppVueObj.app.$store.getters[
                        SearchEngineGetters.dabDataProvidersUrl
                    ],
                    null,
                    false,
                    { timeout: 120000 }
                )
                    .then((data: { organizations: any[] }) => {
                        staticOrganizations = data.organizations
                        for (const organization of staticOrganizations) {
                            const registrationDate = organization.extras.find(
                                (extra: { key: string }) =>
                                    extra.key === 'Registration Date'
                            )
                            if (registrationDate && registrationDate.value) {
                                const date = new Date()
                                date.setFullYear(
                                    parseInt(
                                        registrationDate.value.split('-')[0],
                                        10
                                    )
                                )
                                date.setMonth(
                                    parseInt(
                                        registrationDate.value.split('-')[1],
                                        10
                                    ) - 1
                                )
                                date.setDate(
                                    parseInt(
                                        registrationDate.value.split('-')[2],
                                        10
                                    )
                                )
                                organization.date = date
                            }

                            let principles = []
                            const principlesStr = organization.extras.find(
                                (extra: { key: string }) =>
                                    extra.key ===
                                    'GEO Data Management Principles Label'
                            )
                            if (principlesStr && principlesStr.value) {
                                principles = principlesStr.value.split(',')
                                principles = principles
                                    .map((principle: string) => {
                                        const label = principle.split('-')[0]
                                        if (label) {
                                            return label
                                                .toLowerCase()
                                                .trim()
                                                .replace(/ /g, '_')
                                        } else {
                                            return null
                                        }
                                    })
                                    .filter((principle: any) => principle)
                            }

                            organization.principles = principles

                            let goalsSBA = []
                            const goalsSBAStr = organization.extras.find(
                                (extra: { key: string }) =>
                                    extra.key === 'Relevant SBA'
                            )
                            if (goalsSBAStr && goalsSBAStr.value) {
                                goalsSBA = goalsSBAStr.value.split(',')
                                goalsSBA = goalsSBA
                                    .map((goal: string) => {
                                        let label = goal.split('-')[0]
                                        if (label) {
                                            label = label
                                                .toLowerCase()
                                                .trim()
                                                .replace(/ /g, '_')
                                            if (label.indexOf(':_') !== -1) {
                                                label = label.split(':_')[1]
                                            }
                                            return label
                                        } else {
                                            return null
                                        }
                                    })
                                    .filter((goal: any) => goal)
                            }

                            organization.goalsSBA = goalsSBA

                            let goalsSDG = []
                            const goalsSDGStr = organization.extras.find(
                                (extra: { key: string }) =>
                                    extra.key === 'Relevant SDG'
                            )
                            if (goalsSDGStr && goalsSDGStr.value) {
                                goalsSDG = goalsSDGStr.value.split(',')
                                goalsSDG = goalsSDG
                                    .map((goal: string) => {
                                        let label = goal.split('-')[0]
                                        if (label) {
                                            label = label
                                                .toLowerCase()
                                                .trim()
                                                .replace(/ /g, '_')
                                            if (label.indexOf(':_') !== -1) {
                                                label = label.split(':_')[1]
                                            }
                                            return label
                                        } else {
                                            return null
                                        }
                                    })
                                    .filter((goal: any) => goal)
                            }

                            organization.goalsSDG = goalsSDG
                        }
                    })
                    .then(resolve)
            } else {
                resolve
            }
        }).then(() => {
            const providers = staticOrganizations.filter(
                (provider) =>
                    provider.title
                        .toLowerCase()
                        .indexOf(
                            AppVueObj.app.$store.getters[
                                YellowPagesFiltersGetters.search
                            ].toLowerCase()
                        ) !== -1
            )
            switch (
                AppVueObj.app.$store.getters[YellowPagesFiltersGetters.orderBy]
            ) {
                case 'asc': {
                    providers.sort((a, b) => {
                        if (
                            a.title.toLowerCase().trim() >
                            b.title.toLowerCase().trim()
                        ) {
                            return 1
                        } else if (
                            a.title.toLowerCase().trim() <
                            b.title.toLowerCase().trim()
                        ) {
                            return -1
                        } else {
                            return 0
                        }
                    })
                    break
                }
                case 'desc': {
                    providers.sort((a, b) => {
                        if (
                            a.title.toLowerCase().trim() >
                            b.title.toLowerCase().trim()
                        ) {
                            return -1
                        } else if (
                            a.title.toLowerCase().trim() <
                            b.title.toLowerCase().trim()
                        ) {
                            return 1
                        } else {
                            return 0
                        }
                    })
                    break
                }
                case 'date': {
                    providers.sort((a, b) => {
                        if (!a.date) {
                            return 1
                        } else if (!b.date) {
                            return -1
                        } else if (!a.date && !b.date) {
                            return 0
                        }
                        if (a.date > b.date) {
                            return -1
                        } else if (a.date < b.date) {
                            return 1
                        } else {
                            return 0
                        }
                    })
                    break
                }
            }
            const providersSliced = providers.slice(
                AppVueObj.app.$store.getters[
                    YellowPagesFiltersGetters.pageOffset
                ],
                AppVueObj.app.$store.getters[
                    YellowPagesFiltersGetters.pageOffset
                ] +
                    AppVueObj.app.$store.getters[
                        YellowPagesFiltersGetters.perPage
                    ]
            )

            return { results: providersSliced, resultsTotal: providers.length }
        })
    },
}

export default YellowPagesApiService
