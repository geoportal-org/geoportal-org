import apiClient from './apiClient'
import geossContents from './module/geoss-contents'

interface Page {
    title: string | any
    description: string
    contentId: number
    slug: string
    published: boolean
    createdBy: string
    createdDate: string
    modifiedBy: string
    modifiedDate: string
    _links: {
        page: {
            href: string
        }
        self: {
            href: string
        }
    }
}

interface Pages {
    _embedded: {
        [key: string]: Array<Page>
    }
    _links: {
        profile: {
            href: string
        }
        search: {
            href: string
        }
        self: {
            href: string
        }
    }
    page: {
        number: number
        size: number
        totalElements: number
        totalPages: number
    }
}

interface Content {
    title: string | any
    data: string | any
    published: boolean
    createdBy: string
    createdDate: string
    modifiedBy: string
    modifiedDate: string
    _links: {
        self: {
            href: string
        }
        content: {
            href: string
        }
    }
}

const getSiteId = (site: Site) => {
    return site._links.self.href.split('/').pop() * 1
}

type Language = 'en' | 'es' | 'fr' | 'pl' | 'ru' | 'zh'

interface Site {
    [key: string]: any
}
interface SiteData {
    name: string
    url: string
    logoUrl: string
    siteId?: number
}

const parseSiteData = (data: Site): SiteData => {
    return {
        name: data.name,
        url: data.url,
        logoUrl: `/contents/rest/document/${data.logoDocumentId}/content`,
        siteId: getSiteId(data)
    }
}

const getPages = (siteId: number) => {
    return apiClient.$get(geossContents.page + '&siteId=' + siteId, {
        headers: {
            Authorization: '',
        },
    })
}

const prepareFileToUpload = (inputFile: any): FormData => {
    const formData = new FormData()
    const file = inputFile as File
    const fileInfo = getNewFileData(inputFile)
    formData.set('files', file, file.name)
    formData.set('model', JSON.stringify(fileInfo))
    return formData
}

const getNewFileData = (inputFile: any) => {
    const file = inputFile as File
    const name = file.name
    const extension = name.substring(name.lastIndexOf('.') + 1, name.length)
    return {
        title: name,
        fileName: name,
        extension,
        path: 0,
        folderId: 0,
        siteId: 0,
    }
}

const ContentAPI = {
    generatePage: async (slug: string, locale: Language, siteId: number = 0) => {
        const receivedPage = await ContentAPI.getPage(slug, locale, siteId)
        const generatedPage = receivedPage || { title: 'Missing page' }
        const generatedContent = receivedPage
            ? await ContentAPI.getContent(receivedPage.contentId as any, locale)
            : {
                  data: 'No page found for slug: ' + slug,
              }

        return { generatedPage, generatedContent }
    },

    getPage: async (slug: string, locale: string = 'en', siteId: number) => {
        const pages: Pages = await getPages(siteId)
        const page: Page = pages._embedded.page.filter(
            (page: { slug: string }) => page.slug === slug
        )[0]

        if (!page) {
            return false
        }

        const title = page.title[locale as keyof Language]
        page.title = title

        return page
    },

    getContent: async (contentId: string, locale: string = 'en') => {
        const content: Content = await apiClient.$get(
            geossContents.content + '/' + contentId,
            {
                headers: {
                    Authorization: '',
                },
            }
        )

        const title = content.title[locale as keyof Language]
        const data = content.data[locale as keyof Language]

        content.title = title
        content.data = data

        return content
    },

    addContent: async (inputFile: any, token: any = null) => {
        const fileData = prepareFileToUpload(inputFile)
        try {
            const resp = await apiClient.$post(
                geossContents.document,
                fileData,
                {
                    headers: {
                        'Content-Type': 'application/json',
                        Authorization: token ? token : '',
                    },
                }
            )
            return resp._links.document.href.split('/').slice(-1).pop()
        } catch (e: any) {
            console.warn(e)
            return e.response.data
        }
    },

    getSiteByUrl: async (siteUrl: string = 'global') => {
        try {
            const site: Site = await apiClient.$get(
                geossContents.siteByUrl + '?url=' + siteUrl,
                {
                    headers: {
                        Authorization: '',
                    },
                }
            )

            const siteData: SiteData = parseSiteData(site)
            return siteData
        } catch (e: any) {
            console.warn(e)
            return {}
        }
    },

    getSiteById: async (siteId: number = 0) => {
        try {
            const site: Site = await apiClient.$get(
                geossContents.site + '/' + siteId,
                {
                    headers: {
                        Authorization: '',
                    },
                }
            )

            const siteData: SiteData = parseSiteData(site)
            return siteData
        } catch (e: any) {
            console.warn(e)
            return {}
        }
    },

    updateSite: async (siteData: SiteData, token: any = null) => {
        if (!siteData) return

        const updatedSiteData: SiteData = {...{}, ...siteData}
        delete updatedSiteData.siteId

        try {
            const resp = await apiClient.$post(
                geossContents.site + '/' + siteData.siteId,
                updatedSiteData,
                {
                    headers: {
                        'Content-Type': 'application/json',
                        Authorization: token ? token : '',
                    },
                }
            )
            return resp._links.document.href.split('/').slice(-1).pop()
        } catch (e: any) {
            console.warn(e)
            return e.response.data
        }
    }
}

export default ContentAPI
