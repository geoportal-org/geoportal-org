export const pagesRoutes = {
    addContent: "/website-contents/add",
    addPage: "/page/add",
    api: "/api-settings",
    catalogs: "/catalogs-settings",
    defaultLayer: "/default-layer-settings",
    fileRepository: "/file-repository",
    menu: "/menu",
    myProfile: "/profile",
    page: "/page",
    recommendations: "/recommendations",
    signIn: "/",
    survey: "/survey",
    tutorialTags: "/tutorial-tags-settings",
    views: "/views-settings",
    web: "/web-settings",
    website: "/website-contents",
};

export const unauthenicatedRoutes = [pagesRoutes.signIn];

export const authenticatedRoutes = Object.keys(pagesRoutes).map(
    (routeKey) => pagesRoutes[routeKey as keyof typeof pagesRoutes]
);
