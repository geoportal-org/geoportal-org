import { makeRequest } from '@/services/general.api.service'
// import { Liferay } from '@/data/global'
import DashboardDisplay from '@/components/Search/Results/Dashboard/DashboardDisplay.vue'
import { PopupActions } from '@/store/popup/popup-actions'
import { AppVueObj } from '@/data/global'
import to from '@/utils/to'
import NotificationService from '@/services/notification.service'
import apiClient from '~/api/apiClient'

const DashboardService = {
    async getAllDashboards() {
        const url = `${window.$nuxt.$config.curatedUrl}userDashboards?page=0&size=9999`
        const list = await apiClient.$get(url)
        return list
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

    async createDashboard(dashboardData = null, userId = '') {
        if (!dashboardData) {
            return
        }
        const { title, summary, outputs }: any = dashboardData
        const code =
            title.toLocaleLowerCase().replace(/ /g, '_') +
            '_' +
            new Date().toISOString()

        const transferOptions = []
        for (const output of outputs) {
            const transferOption = {
                name: output.name,
                description: output.description,
                title: output.id,
                protocol: {
                    value: 'download'
                },
                endpoint: {
                    url: output.value,
                    urlType: output.valueSchema
                }
            }
            transferOptions.push(transferOption)
        }

        const body = {
            userId: userId,
            entryName: title,
            taskType: 'create',
            entry: {
                title: title,
                summary: summary,
                logo: '',
                coverage: '[-180,90],[180,-90]',
                type: 'information_resource',
                dashboardContents: {
                    content: JSON.stringify(dashboardData).replace(/\"/g, "'")
                },
                accessPolicy: {
                    name: '',
                    code: ''
                },
                keywords: 'dashboard',
                tags: 'dashboard',
                code: code,
                organisation: {
                    title: '',
                    email: '',
                    contact: '',
                    contactEmail: ''
                },
                source: {
                    term: '',
                    code: ''
                },
                dataSource: 'geoss_cr',
                displayDataSource: 'geoss_cr',
                definitionType: 1,
                userId: userId,
                transferOptions: transferOptions
            }
        }
        const url = `${window.$nuxt.$config.curatedUrl}userDashboards`
        const userResourceUrl = `${window.$nuxt.$config.curatedUrl}userResources`
        try {
            await apiClient.$post(userResourceUrl, body, {
                headers: {
                    'Content-Type': 'application/json',
                    'accept': '*/*'
                }
            })
            await apiClient.$post(url, body, {
                headers: {
                    'Content-Type': 'application/json',
                    'accept': '*/*'
                }
            })
            return 'ok'
        } catch (e: any) {
            console.error(e)
            return 'fail'
        }
    },

    async showDashboard(dashboardContent: any = null, title: string) {
        let data = null
        try {
            if (dashboardContent) {
                data = JSON.parse(dashboardContent.replace(/\'/g, '"'))
            }
            if (data) {
                const props = {
                    data
                }
                AppVueObj.app.$store.dispatch(PopupActions.openPopup, {
                    contentId: 'dashboard-display',
                    title: title,
                    component: DashboardDisplay,
                    props
                })
            }
        } catch (error) {
            console.error(error)
        }
    }
}

export default DashboardService
