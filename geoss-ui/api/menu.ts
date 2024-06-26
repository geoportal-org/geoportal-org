import apiClient from './apiClient'
import geossContents from './module/geoss-contents'

interface MenuElement {
    createdBy: string
    createdDate: string
    imageSource: string
    imageTitle: string
    levelId: number
    modifiedBy: string
    modifiedDate: string
    parentMenuId: number
    priority: number
    title: string
    url: string
    _links: {
        menu: {
            href: string
        }
        self: {
            href: string
        }
    }
}

interface MenuResponse {
    page: {
        number: number
        size: number
        totalElements: number
        totalPages: number
    }
    _embedded: {
        [key: string]: Array<MenuElement>
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
}

interface Level2Route {
    link: string
    title: string | any
}

interface Level1Route {
    imgURL: string
    link: string | null
    links: Array<Level2Route>
    title: string | any
}

interface Routes extends Array<Level1Route> {}

type Language = 'en' | 'es' | 'fr' | 'pl' | 'ru' | 'zh'

const getId = (element: MenuElement) => {
    return element._links.self.href.split('/').pop()
}

const buildMenu = (menu: Array<MenuElement>) => {
    const locale: Language = window.$nuxt.$i18n.getLocaleCookie() as Language
    const routes: Routes = []
    const linksLevel1 = menu.filter((e) => e.levelId === 0)
    const linksLevel2 = menu.filter((e) => e.levelId === 1)

    linksLevel1.sort((a, b) => a.priority - b.priority)
    linksLevel2.sort((a, b) => a.priority - b.priority)

    for (const link of linksLevel1) {
        const routeLevel1: Level1Route = {
            imgURL: link.imageSource,
            title: link.title[locale as keyof Language],
            link: link.url || null,
            links: [],
        }
        const id = getId(link)
        const children = linksLevel2.filter((e) => e.parentMenuId + '' === id)
        if (children.length) {
            for (const child of children) {
                const routeLevel2: Level2Route = {
                    link: child.url,
                    title: child.title[locale as keyof Language],
                }
                routeLevel1.links.push(routeLevel2)
            }
        }
        routes.push(routeLevel1)
    }

    return routes
}

export default {
    getMenu: async (siteId: number = 0) => {
        const levels = [0, 1];
        const promises = [];

        for (const level of levels) {
            promises.push(
                new Promise((resolve) => {
                    apiClient.$get(geossContents.menu + '&siteId=' + siteId + '&levelId=' + level, {
                        headers: {
                            Authorization: '',
                        },
                    }).then((menuRaw: MenuResponse) => {
                        resolve(menuRaw._embedded.menu)
                    })
                })
            )
        }

        return Promise.all(promises).then((menuLevels) => {
            const menuFlat: any = menuLevels.flat();
            return buildMenu(menuFlat)
        })
    },
}
