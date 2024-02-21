import { PagesInfo } from "./page";

export interface RecommendationList {
    _embedded: {
        recommendationModels: SavedRecommendationData[];
    };
    _links: {
        recommendations: {
            href: string;
            templated: boolean;
        };
        self: {
            href: string;
        };
    };
    page: PagesInfo
}

export interface RecommendationData {
    entities: [
        {
            dataSourceCode: string;
            entityCode: string;
            name: string;
            orderWeight: number;
        }
    ];
    keywords: string[];
}

export interface SavedRecommendationData {
    id: number;
    entities: [
        {
            id: number;
            dataSourceCode: string;
            entityCode: string;
            name: string;
            orderWeight: number;
        }
    ];
    keywords: string[];
    _links: {
        recommendations: {
            href: string;
        };
    };
}

export interface RecommendationEntityData {
    dataSourceCode: string;
    entityCode: string;
    name: string;
    orderWeight: number;
}
