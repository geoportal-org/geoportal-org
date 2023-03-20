export interface MenuLink {
    imgURL: string
    link: string
    title: string
    links: MenuSublink[]
    visible: boolean
}

interface MenuSublink {
    link: string
    title: string
}

export interface MenuLinksWrapper {
    imgURL: string
    title: string
    links: MenuSublink[]
    visible: boolean
    link: string
}
