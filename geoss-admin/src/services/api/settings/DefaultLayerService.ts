import { QueryParams } from "@/types";
import { ILayer, ILayerData, ILayerList } from "@/types/models";
import { fetcher } from "@/utils/fetcher";

export const DefaultLayerService = {
    baseUrl: `${process.env.NEXT_PUBLIC_API}/settings/rest/layers`,

    getLayersList: async (siteId: number, query?: QueryParams): Promise<ILayerList> =>
        fetcher({ url: `${process.env.NEXT_PUBLIC_API}/settings/rest/sites/${siteId}/layers`, query }),

    getLayer: async (id: number): Promise<ILayer> => fetcher({ url: `${DefaultLayerService.baseUrl}/${id}` }),

    createLayer: async (layerData: ILayerData): Promise<ILayer> =>
        fetcher({ url: DefaultLayerService.baseUrl, method: "POST", body: layerData }),

    updateLayer: async (id: number, layerData: ILayerData): Promise<ILayer> =>
        fetcher({ url: `${DefaultLayerService.baseUrl}/${id}`, method: "PUT", body: layerData }),

    deleteLayer: async (id: number) => fetcher({ url: `${DefaultLayerService.baseUrl}/${id}`, method: "DELETE" }),
};
