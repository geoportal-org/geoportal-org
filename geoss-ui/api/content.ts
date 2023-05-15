import apiClient from './apiClient'
import geossContents from './module/geoss-contents'

interface Page {
    title: string
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
    title: string
    data: string
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

const getPages = () => {
    return apiClient.$get(geossContents.page)
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
    }
}

export default {
    getPage: async (slug: string) => {
        const pages: Pages = await getPages()
        const page: Page = pages._embedded.page.filter(
            (page: { slug: string }) => page.slug === slug
        )[0]
        return page
    },

    getContent: async (contentId: string) => {
        const content: Content = await apiClient.$get(
            geossContents.content + '/' + contentId
        )
        return content
    },

    addContent: async (inputFile: any) => {
        const fileData = prepareFileToUpload(inputFile)
        try {
            const resp = await apiClient.$post(geossContents.document, fileData)
            return resp._links.document.href.split('/').slice(-1).pop()
        } catch (e) {
            console.warn(e)
            return e.response.data
        }
    },
}
