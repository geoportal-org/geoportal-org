export const pagesRoutes = {
    addContent: "/website-contents/add",
    addPage: "/page/add",
    api: "/api-settings",
    catalogs: "/catalogs-settings",
    console: `${process.env.NEXT_PUBLIC_IDP_DOMAIN}/admin/geoss/console`,
    defaultLayer: "/default-layer-settings",
    entryResources: "/entry-resources",
    fileRepository: "/file-repository",
    menu: "/menu",
    myProfile: `${process.env.NEXT_PUBLIC_IDP_DOMAIN}/realms/geoss/account`,
    page: "/page",
    recommendations: "/recommendations",
    resourceExtensions: "/resource-extensions",
    signIn: "/",
    sites: '/sites',
    survey: "/survey",
    tutorialTags: "/tutorial-tags-settings",
    views: "/views-settings",
    web: "/web-settings",
    website: "/website-contents",
    workers: '/workers'
};

export const unauthenicatedRoutes = [pagesRoutes.signIn];

export const authenticatedRoutes = Object.keys(pagesRoutes).map(
    (routeKey) => pagesRoutes[routeKey as keyof typeof pagesRoutes]
);
