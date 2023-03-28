import { QueryParams } from "@/types";
import { IContent, IContentData, IContentList } from "@/types/models";
import { fetcher } from "@/utils/fetcher";

export const ContentService = {
    baseUrl: "http://10.254.7.59:8082/rest/content",
    baseRoute: "/api/backend/contents",

    // to be removed - just for test
    getContentsClientSide: async (query?: QueryParams): Promise<IContentList> =>
        fetcher({ url: "https://gpp.devel.esaportal.eu/contents/rest/content", query }),

    getContentList: async (query?: QueryParams): Promise<IContentList> =>
        fetcher({ url: ContentService.baseUrl, query }),

    getContent: async (id: number): Promise<IContent> => fetcher({ url: `${ContentService.baseUrl}/${id}` }),

    createContent: async (contentData: IContentData): Promise<IContent> =>
        fetcher({ url: ContentService.baseUrl, method: "POST", body: contentData }),

    updateContent: async (id: number, contentData: Partial<IContentData>): Promise<IContent> =>
        fetcher({ url: `${ContentService.baseUrl}/${id}`, method: "PUT", body: contentData }),

    deleteContent: async (id: number) => fetcher({ url: `${ContentService.baseUrl}/${id}`, method: "DELETE" }),

    getContentsRoute: async (query?: QueryParams): Promise<IContentList> =>
        fetcher({ url: ContentService.baseRoute, query }),

    getContentRoute: async (id: number): Promise<IContent> => fetcher({ url: `${ContentService.baseRoute}/${id}` }),

    createContentRoute: async (contentData: IContentData): Promise<IContent> =>
        fetcher({ url: ContentService.baseRoute, method: "POST", body: contentData }),

    updateContentRoute: async (id: number, contentData: Partial<IContentData>): Promise<IContent> =>
        fetcher({ url: `${ContentService.baseRoute}/${id}`, method: "PUT", body: contentData }),

    deleteContentRoute: async (id: number) => fetcher({ url: `${ContentService.baseRoute}/${id}`, method: "DELETE" }),
};
