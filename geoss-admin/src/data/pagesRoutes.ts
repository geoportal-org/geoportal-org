export const pagesRoutes = {
    addContent: "/contents/add",
    addPage: "/page/add",
    api: "/api-settings",
    catalogs: "/catalogs-settings",
    defaultLayer: "/default-layer-settings",
    fileRepository: "/file-repository",
    menu: "/menu",
    myProfile: "/profile",
    page: "/page",
    signIn: "/",
    tutorialTags: "/tutorial-tags-settings",
    views: "/views-settings",
    web: "/web-settings",
    website: "/contents",
};

export const unauthenicatedRoutes = [pagesRoutes.signIn];

export const authenticatedRoutes = Object.keys(pagesRoutes).map(
    (routeKey) => pagesRoutes[routeKey as keyof typeof pagesRoutes]
);
