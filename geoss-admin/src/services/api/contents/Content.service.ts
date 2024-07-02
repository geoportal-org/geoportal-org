import { QueryParams } from "@/types";
import { IContent, IContentData, IContentList } from "@/types/models";
import { fetcher } from "@/utils/fetcher";

export const ContentService = {
    baseUrl: `${process.env.NEXT_PUBLIC_API}/contents/rest/content`,

    getContentList: async (query?: QueryParams): Promise<IContentList> =>
        fetcher({ url: `${ContentService.baseUrl}/search/findBySiteId`, query }),

    getContent: async (id: number): Promise<IContent> => fetcher({ url: `${ContentService.baseUrl}/${id}` }),

    createContent: async (contentData: IContentData): Promise<IContent> =>
        fetcher({ url: ContentService.baseUrl, method: "POST", body: contentData }),

    updateContent: async (id: number, contentData: IContentData): Promise<IContent> =>
        fetcher({ url: `${ContentService.baseUrl}/${id}`, method: "PUT", body: contentData }),

    deleteContent: async (id: number) => fetcher({ url: `${ContentService.baseUrl}/${id}`, method: "DELETE" }),

    deleteContents: async (query: QueryParams) =>
        fetcher({ url: `${ContentService.baseUrl}/deleteByIdsIn`, method: "DELETE", query }),
};
