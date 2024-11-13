import { makeRequest } from '@/services/general.api.service'
// import { Liferay } from '@/data/global'
import DashboardDisplay from '@/components/Search/Results/Dashboard/DashboardDisplay.vue'
import { PopupActions } from '@/store/popup/popup-actions'
import { AppVueObj } from '@/data/global'
import to from '@/utils/to'
import NotificationService from '@/services/notification.service'

const DashboardService = {
    getAllDashboards() {
        const userId = '' // Liferay.ThemeDisplay.getUserId() || '20433'
        const companyId = '' // Liferay.ThemeDisplay.getCompanyId() || '20154'
        const cur = 0
        const delta = 1000
        const url = `/community/guest/geoss-resources?p_p_id=geossresources_WAR_geossportlet&p_p_lifecycle=2&p_p_resource_id=GET_USER_DASHBOARDS&_geossresources_WAR_geossportlet_userId=${userId}&_geossresources_WAR_geossportlet_cur=${cur}&_geossresources_WAR_geossportlet_delta=${delta}&_geossresources_WAR_geossportlet_companyId=${companyId}`
        return makeRequest('get', url, null, true)
            .then((data: any) => {
                if (!data || data.status === 500) {
                    return null
                }
                return data.dashboardContents.filter(
                    (e: any) => e.dashboardContent && e.dashboardContent !== ''
                )
            })
            .catch((error: any) => {
                console.warn(error)
            })
    },

    getDashboard(workflowWrapperId = 122) {
        const url = `/community/guest/geoss-resources?p_p_id=geossresources_WAR_geossportlet&p_p_lifecycle=2&p_p_resource_id=GET_DASHBOARD&_geossresources_WAR_geossportlet_workflowWrapperId=${workflowWrapperId}`
        return makeRequest('get', url, null, true)
            .then((data: any) => {
                if (!data || data.status === 500) {
                    return null
                }
                return data
            })
            .catch((error: any) => {
                console.warn(error)
            })
    },

    createDashboard(dashboardData = null) {
        if (!dashboardData) {
            return
        }
        const userId = '' // Liferay.ThemeDisplay.getUserId() || '20433'
        const groupId = '' // Liferay.ThemeDisplay.getScopeGroupId() || '20181'
        const companyId = '' // Liferay.ThemeDisplay.getCompanyId() || '20154'
        const config = { headers: { 'content-type': 'application/json' } }
        const { title, summary, outputs }: any = dashboardData
        const code =
            title.toLocaleLowerCase().replace(/ /g, '_') +
            '_' +
            new Date().toISOString()

        const transferOptionsDtos = []
        for (const output of outputs) {
            const transferOption = {
                id: 0,
                name: output.name,
                description: output.description,
                title: output.id,
                entryId: 0,
                endpointDto: {
                    id: 0,
                    isCustom: 1,
                    url: output.value.url,
                    urlType: output.valueSchema
                },
                protocolDto: {
                    isCustom: 0,
                    id: 1,
                    value: 'download'
                }
            }
            if (output.valueSchema === 'wms') {
                transferOption.protocolDto = {
                    isCustom: 1,
                    value: output.value.protocol,
                    id: 0
                }
            }
            if (output.valueSchema === 'url') {
                transferOption.endpointDto.url = output.value
            }
            transferOptionsDtos.push(transferOption)
        }

        const entryDto = {
            entryDto: {
                id: 0,
                title,
                summary,
                logo: '',
                coverage: '[-180,90],[180,-90]',
                keywords: 'dashboard',
                tags: 'dashboard',
                code,
                scoreWeight: 1.0,
                workflowInstanceId: 0,
                definitionTypeId: 0,
                typeDto: {
                    id: 3,
                    name: 'Information',
                    code: 'information_resource'
                },
                dataSourceDto: {
                    id: 1,
                    name: 'GEOSS Curated',
                    code: 'geoss_cr'
                },
                dashboardContentsDto: {
                    id: 0,
                    content: JSON.stringify(dashboardData).replace(/\"/g, "'")
                },
                accessPolicyDto: {
                    id: 1,
                    isCustom: 0
                },
                organisationDto: {
                    id: 6,
                    isCustom: 0
                },
                sourceDto: {
                    id: 1,
                    isCustom: 0
                },
                transferOptionsDtos
            }
        }

        const url = `/community/guest/geoss-resources?p_p_id=geossresources_WAR_geossportlet&p_p_lifecycle=2&p_p_resource_id=CREATE_DASHBOARD&_geossresources_WAR_geossportlet_userId=${userId}&_geossresources_WAR_geossportlet_groupId=${groupId}&_geossresources_WAR_geossportlet_companyId=${companyId}`
        return makeRequest('post', url, entryDto, false, config)
            .then((response: any) => {
                const data = JSON.stringify(response)
                if (data && data.length) {
                    NotificationService.show(
                        `${AppVueObj.app.$tc('popupTitles.dashboards')}`,
                        `${AppVueObj.app.$tc(
                            'notifications.dashboardSavedSuccessfully'
                        )}`,
                        10000,
                        'dashboard-save-success',
                        9999,
                        'info'
                    )
                } else {
                    NotificationService.show(
                        `${AppVueObj.app.$tc('popupTitles.dashboards')}`,
                        `${AppVueObj.app.$tc(
                            'notifications.errorDuringDashboardSaving'
                        )}`,
                        10000,
                        'dashboard-save-error',
                        9999,
                        'error'
                    )
                }
            })
            .catch((error: any) => {
                console.warn(error)
                NotificationService.show(
                    `${AppVueObj.app.$tc('popupTitles.dashboards')}`,
                    `${AppVueObj.app.$tc(
                        'notifications.errorDuringDashboardSaving'
                    )}`,
                    10000,
                    'dashboard-save-error',
                    9999,
                    'error'
                )
            })
    },

    async showDashboard(dashboardContent: any = null, workflowWrapperId = 0) {
        let data = null
        try {
            if (dashboardContent) {
                data = JSON.parse(dashboardContent.replace(/\'/g, '"'))
            }
            if (workflowWrapperId) {
                const [, dashboard] = await to(
                    DashboardService.getDashboard(workflowWrapperId)
                )
                if (
                    dashboard &&
                    dashboard.dashboardContent &&
                    dashboard.dashboardContent !== ''
                ) {
                    data = JSON.parse(
                        dashboard.dashboardContent.replace(/\'/g, '"')
                    )
                }
            }
            if (data) {
                const props = {
                    data
                }
                AppVueObj.app.$store.dispatch(PopupActions.openPopup, {
                    contentId: 'dashboard-display',
                    title: AppVueObj.app.$tc('popupTitles.dashboards'),
                    component: DashboardDisplay,
                    props
                })
                NotificationService.show(
                    `${AppVueObj.app.$tc('popupTitles.dashboards')}`,
                    `${AppVueObj.app.$tc(
                        'notifications.dashboardLoadedSuccessfully'
                    )}`,
                    10000,
                    'dashboard-load-success',
                    9999,
                    'info'
                )
            }
        } catch (error) {
            NotificationService.show(
                `${AppVueObj.app.$tc('popupTitles.dashboards')}`,
                `${AppVueObj.app.$tc(
                    'notifications.errorDuringDashboardLoading'
                )}`,
                10000,
                'dashboard-load-error',
                9999,
                'error'
            )
            console.warn(error)
        }
    }
}

export default DashboardService
