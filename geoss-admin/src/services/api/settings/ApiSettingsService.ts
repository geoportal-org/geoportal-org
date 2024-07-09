import { QueryParams } from "@/types";
import { IApiSetting, IApiSettingData, IApiSettingsList } from "@/types/models";
import { fetcher } from "@/utils/fetcher";

export const ApiSettingsService = {
    baseUrl: `${process.env.NEXT_PUBLIC_API}/settings/rest/api-settings`,

    getApiSettings: async (siteId: number, query?: QueryParams): Promise<IApiSettingsList> =>
        fetcher({ url: `${process.env.NEXT_PUBLIC_API}/settings/rest/sites/${siteId}/api-settings`, query }),

    getApiSetting: async (id: number): Promise<IApiSetting> => fetcher({ url: `${ApiSettingsService.baseUrl}/${id}` }),

    createApiSetting: async (apiSettingData: IApiSettingData): Promise<IApiSetting> =>
        fetcher({ url: ApiSettingsService.baseUrl, method: "POST", body: apiSettingData }),

    updateApiSetting: async (id: number, apiSettingData: IApiSettingData): Promise<IApiSetting> =>
        fetcher({ url: `${ApiSettingsService.baseUrl}/${id}`, method: "PUT", body: apiSettingData }),

    deleteApiSetting: async (id: number) => fetcher({ url: `${ApiSettingsService.baseUrl}/${id}`, method: "DELETE" }),
};
