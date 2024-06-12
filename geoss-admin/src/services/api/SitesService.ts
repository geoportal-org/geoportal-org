import { CreateSiteBody } from "@/types/models/sites";
import { fetcher } from "@/utils/fetcher";

export const SitesService = {
    baseUrl: `${process.env.NEXT_PUBLIC_API}/contents/rest/site`,

    getSites: async (): Promise<any> => fetcher({ url: `${SitesService.baseUrl}?page=0&size=40` }),
    getSitesWithoutGlobal: async (): Promise<any> =>
        fetcher({ url: `${SitesService.baseUrl}/search/findAllExceptIdZero?page=0&size=40` }),
    createSite: async (data: FormData): Promise<any> =>
        fetcher({ url: SitesService.baseUrl, method: "POST", body: data }),
    deleteSite: async (id: string): Promise<any> => fetcher({ url: `${SitesService.baseUrl}/${id}`, method: "DELETE" }),
    updateSite: async (data: CreateSiteBody, id: string): Promise<any> =>
        fetcher({ url: `${SitesService.baseUrl}/${id}`, method: "PUT", body: data }),
};
