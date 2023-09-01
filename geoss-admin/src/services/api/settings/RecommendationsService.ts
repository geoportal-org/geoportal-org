import { RecommendationData, RecommendationEntityData, RecommendationList, SavedRecommendationData } from "@/types/models/recommendations";
import { fetcher } from "@/utils/fetcher";

export const RecommendationsService = {
    baseUrl: `${process.env.NEXT_PUBLIC_API}/curated/rest/recommendations`,

    getRecommendations: async (page: number): Promise<RecommendationList> =>
        fetcher({ url: `${RecommendationsService.baseUrl}?page=${page}&size=10` }),
    createRecommendation: async (recommendationData: RecommendationData): Promise<SavedRecommendationData> =>
        fetcher({ url: RecommendationsService.baseUrl, method: "POST", body: recommendationData }),
    getRecommendation: async (id: number): Promise<SavedRecommendationData> =>
        fetcher({ url: `${RecommendationsService.baseUrl}/${id}`}),
    deleteRecommendation: async (id: number) => 
        fetcher({ url: `${RecommendationsService.baseUrl}/${id}`, method: "DELETE"}),
    addEntityForRecommendation: async (id: number, entityData: RecommendationEntityData[]) => 
        fetcher({ url: `${RecommendationsService.baseUrl}/${id}/entities`, method: 'POST', body: entityData}),
    updateEntity: async (recommendationId: number, entityId: number, entityData: RecommendationEntityData) =>
        fetcher({ url: `${RecommendationsService.baseUrl}/${recommendationId}/entities/${entityId}`, method: 'PUT', body: entityData}),
    deleteEntityForRecommendation: async (recommendationId: number, entityId: number) => 
        fetcher({ url: `${RecommendationsService.baseUrl}/${recommendationId}/entities/${entityId}`, method: 'DELETE'}),
    updateKeywordsForRecommendation: async (recommendationId: number, keywords: string[]) =>
        fetcher({ url: `${RecommendationsService.baseUrl}/${recommendationId}/keywords`, method: 'PUT', body: keywords}),
};  
