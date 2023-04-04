import { QueryParams } from "@/types";
import { IPage, IPageData, IPageList } from "@/types/models";
import { fetcher } from "@/utils/fetcher";

export const PageService = {
    baseUrl: `${process.env.NEXT_PUBLIC_API}/contents/rest/page`,

    getPagesList: async (query?: QueryParams): Promise<IPageList> => fetcher({ url: PageService.baseUrl, query }),

    getPage: async (id: number): Promise<IPage> => fetcher({ url: `${PageService.baseUrl}/${id}` }),

    createPage: async (pageData: IPageData): Promise<IPage> =>
        fetcher({ url: PageService.baseUrl, method: "POST", body: pageData }),

    updatePage: async (id: number, pageData: Partial<IPageData>): Promise<IPage> =>
        fetcher({ url: `${PageService.baseUrl}/${id}`, method: "PUT", body: pageData }),

    deletePage: async (id: number) => fetcher({ url: `${PageService.baseUrl}/${id}`, method: "DELETE" }),
};
