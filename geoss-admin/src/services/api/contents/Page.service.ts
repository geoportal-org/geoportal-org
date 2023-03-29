import { QueryParams } from "@/types";
import { IPage, IPageData, IPageList } from "@/types/models";
import { fetcher } from "@/utils/fetcher";

export const PageService = {
    //baseUrl: "http://10.254.7.59:8082/rest/page",
    baseUrl: "https://gpp-admin.devel.esaportal.eu/contents/rest/page",
    baseRoute: "/api/backend/pages",

    //to be removed - just for test
    getPagesListClientSide: async (query?: QueryParams): Promise<IPageList> =>
        fetcher({ url: "https://gpp-admin.devel.esaportal.eu/contents/rest/page", query }),

    getPagesList: async (query?: QueryParams): Promise<IPageList> => fetcher({ url: PageService.baseUrl, query }),

    getPage: async (id: number): Promise<IPage> => fetcher({ url: `${PageService.baseUrl}/${id}` }),

    createPage: async (pageData: IPageData): Promise<IPage> =>
        fetcher({ url: PageService.baseUrl, method: "POST", body: pageData }),

    updatePage: async (id: number, pageData: Partial<IPageData>): Promise<IPage> =>
        fetcher({ url: `${PageService.baseUrl}/${id}`, method: "PUT", body: pageData }),

    deletePage: async (id: number) => fetcher({ url: `${PageService.baseUrl}/${id}`, method: "DELETE" }),

    getPagesRoute: async (query?: QueryParams): Promise<IPageList> => fetcher({ url: PageService.baseRoute, query }),

    getPageRoute: async (id: number): Promise<IPage> => fetcher({ url: `${PageService.baseRoute}/${id}` }),

    createPageRoute: async (pageData: IPageData): Promise<IPage> =>
        fetcher({ url: PageService.baseRoute, method: "POST", body: pageData }),

    updatePageRoute: async (id: number, pageData: Partial<IPageData>): Promise<IPage> =>
        fetcher({ url: `${PageService.baseRoute}/${id}`, method: "PUT", body: pageData }),

    deletePageRoute: async (id: number) => fetcher({ url: `${PageService.baseRoute}/${id}`, method: "DELETE" }),
};
