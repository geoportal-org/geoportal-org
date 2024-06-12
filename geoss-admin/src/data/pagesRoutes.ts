export const pagesRoutes = {
    addContent: "/website-contents/add",
    addPage: "/page/add",
    api: "/api-settings",
    catalogs: "/catalogs-settings",
    defaultLayer: "/default-layer-settings",
    entryResources: "/entry-resources",
    fileRepository: "/file-repository",
    menu: "/menu",
    myProfile: "/profile",
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
