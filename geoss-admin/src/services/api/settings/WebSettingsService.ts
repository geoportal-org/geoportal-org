import { QueryParams } from "@/types";
import { IWebSetting, IWebSettingData, IWebSettingsList } from "@/types/models";
import { fetcher } from "@/utils/fetcher";

export const WebSettingsService = {
    baseUrl: `${process.env.NEXT_PUBLIC_API}/settings/rest/web-settings`,

    getWebSettings: async (siteId: number, query?: QueryParams): Promise<IWebSettingsList> =>
        fetcher({ url: `${process.env.NEXT_PUBLIC_API}/settings/rest/sites/${siteId}/web-settings`, query }),

    getDefaultSourceNameOptions: async (set = "source", key = "defaultSourceName"): Promise<any> =>
        fetcher({ url: `${WebSettingsService.baseUrl}/sets/${set}/keys/${key}/values`, method: "OPTIONS"}),

    getWebSetting: async (id: number): Promise<IWebSetting> => fetcher({ url: `${WebSettingsService.baseUrl}/${id}` }),

    createWebSetting: async (webSettingData: IWebSettingData): Promise<IWebSetting> =>
        fetcher({ url: WebSettingsService.baseUrl, method: "POST", body: webSettingData }),

    updateWebSetting: async (id: number, webSettingData: IWebSettingData): Promise<IWebSetting> =>
        fetcher({ url: `${WebSettingsService.baseUrl}/${id}`, method: "PUT", body: webSettingData }),

    deleteWebSetting: async (id: number) => fetcher({ url: `${WebSettingsService.baseUrl}/${id}`, method: "DELETE" }),
};
