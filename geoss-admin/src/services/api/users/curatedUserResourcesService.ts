import { CreateUserResource, EntriesData, TransferOption, UserResourcesData } from "@/types/models/userResources";
import { fetcher } from "@/utils/fetcher";

export const UserResourcesService = {
    baseUrl: `${process.env.NEXT_PUBLIC_API}/curated/rest`,

    getTypes: async (): Promise<any> => fetcher({ url: `${UserResourcesService.baseUrl}/type` }),
    getAccessPolicies: async (): Promise<any> => fetcher({ url: `${UserResourcesService.baseUrl}/accessPolicy` }),
    getOrganisations: async (): Promise<any> => fetcher({ url: `${UserResourcesService.baseUrl}/organisation` }),
    getSources: async (): Promise<any> => fetcher({ url: `${UserResourcesService.baseUrl}/source` }),
    getEndpoints: async (): Promise<any> => fetcher({ url: `${UserResourcesService.baseUrl}/endpoint` }),
    getProtocols: async (): Promise<any> => fetcher({ url: `${UserResourcesService.baseUrl}/protocol` }),
    getUrlTypes: async (): Promise<any> => fetcher({ url: `${UserResourcesService.baseUrl}/endpoint/urlTypes` }),
    getResources: async (page: number, size = 20, userId: string): Promise<UserResourcesData> =>
        fetcher({ url: `${UserResourcesService.baseUrl}/userResources/check/user/${userId}?page=${page}&size=${size}` }),
    createResource: async (data: CreateUserResource) =>
        fetcher({ url: `${UserResourcesService.baseUrl}/userResources`, method: "POST", body: data }),
    getEntries: async (page: number, size = 20): Promise<EntriesData> =>
        fetcher({ url: `${UserResourcesService.baseUrl}/resources?page=${page}&size=${size}` }),
    getEntry: async (entryId: number): Promise<TransferOption[]> =>
        fetcher({ url: `${UserResourcesService.baseUrl}/transferOption/entry/${entryId}` }),
    updateEntry: async (data: any, entryId: number) =>
        fetcher({ url: `${UserResourcesService.baseUrl}/resources/${entryId}`, method: "PUT", body: data }),
};
