import { AppVueObj } from '~/data/global'
import { OidcProvider, OpenEO } from '@openeo/js-client'
import { UserActions } from '~/store/user/user-actions'

const OpenEOService = {
    async authenticateOpenEO() {
        OidcProvider.uiMethod = 'popup'
        try {
            //@ts-ignore
            const con = await OpenEO.connect(
                'https://openeo.dataspace.copernicus.eu/openeo/1.2'
            )
            let res = await fetch(
                'https://openeo.dataspace.copernicus.eu/openeo/1.2/credentials/oidc',
                {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }
            )
            const credentials = await res.json()
            //@ts-ignore
            const provider = new OidcProvider(con, credentials.providers[0])
            //@ts-ignore
            provider.setClientId('sh-7ca5cca7-045f-4adb-a24e-c20bc3dde8f7')
            OidcProvider.redirectUrl = window.$nuxt.$config.openEORedirect || ''
            //@ts-ignore
            await provider.login({}, true)
            //@ts-ignore
            let token = provider.getToken()
            AppVueObj.app.$store.dispatch(
                UserActions.setOpenEOTokenExpireDate,
                //@ts-ignore
                provider.user.expires_at
            )
            AppVueObj.app.$store.dispatch(UserActions.setOpenEOToken, token)
            return token
        } catch (e: any) {
            console.log(e)
        }
    },

    async getOpenEOJobs(token: string) {
        const list = await fetch(
            'https://openeo.dataspace.copernicus.eu/openeo/1.2/jobs',
            {
                method: 'GET',
                headers: {
                    Authorization: `Bearer ${token}`
                }
            }
        )
        const jsonGet = await list.json()

        const listWithResults = await Promise.all(
            jsonGet.jobs.map(async (element: any) => {
                if (element.status === 'finished') {
                    const res = await fetch(
                        `https://openeo.dataspace.copernicus.eu/openeo/1.2/jobs/${element.id}/results`,
                        {
                            method: 'GET',
                            headers: {
                                Authorization: `Bearer ${token}`
                            }
                        }
                    )
                    const json = await res.json()
                    return { ...element, ...json }
                } else {
                    return element
                }
            })
        )

        console.log(listWithResults)

        const parsedJobs = this.parseOpenEOJobs(listWithResults)
        return parsedJobs
    },

    parseOpenEOJobs(data: any) {
        if (data) {
            return data.map((job: any) => {
                const outputs: any[] = []
                if (job.assets) {
                    for (const [key] of Object.entries(job.assets)) {
                        const value = job.assets[key]
                        if (value.title.includes('.tif')) {
                            outputs.push({
                                aoi: null,
                                createFolder: true,
                                description: value.title,
                                id: value.title,
                                name: value.title,
                                outputType: 'individual',
                                value: value.href,
                                valueArray: null,
                                valueSchema: 'url',
                                valueType: 'value'
                            })
                        }
                    }
                }
                return {
                    createdOn: job.created,
                    createdBy: '',
                    id: job.id,
                    messageList: [],
                    modifiedBy: '',
                    modifiedOn: job.updated,
                    name: job.title || 'Calculation not finished',
                    outputs: outputs,
                    path: '',
                    result: job.status.toUpperCase(),
                    runId: job.id,
                    showLogs: false,
                    showOutputs: false,
                    status: job.status.toUpperCase(),
                    user: '',
                    workflowId: job.id
                }
            })
        } else {
            return []
        }
    },

    async createOpenEOJob(url: string, token: string, body: any) {
        const res = await fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(body)
        })

        return res
    },

    async runOpenEOJob(id: string, token: string) {
        await fetch(
            `https://openeo.dataspace.copernicus.eu/openeo/1.2/jobs/${id}/results`,
            {
                method: 'POST',
                headers: {
                    Authorization: `Bearer ${token}`
                }
            }
        )
    },

    async getJobResults(id: string, token: string) {
        await fetch(
            `https://openeo.dataspace.copernicus.eu/openeo/1.2/jobs/${id}/results`,
            {
                method: 'GET',
                headers: {
                    Authorization: `Bearer ${token}`
                }
            }
        )
    },

    async getJobLogs(id: string, token: string) {
        await fetch(
            `https://openeo.dataspace.copernicus.eu/openeo/1.2/jobs/${id}/logs`,
            {
                method: 'GET',
                headers: {
                    Authorization: `Bearer ${token}`
                }
            }
        )
    }
}

export default OpenEOService
