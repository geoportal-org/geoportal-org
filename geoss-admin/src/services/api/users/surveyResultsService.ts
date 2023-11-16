import { SurveysData } from "@/types/models/surveys";
import { fetcher } from "@/utils/fetcher";

export const SurveysService = {
    baseUrl: `${process.env.NEXT_PUBLIC_API}/personaldata/rest/surveys`,

    getSurveys: async (page: number, size = 20): Promise<SurveysData> =>
        fetcher({ url: `${SurveysService.baseUrl}?page=${page}&size=${size}` }),
    searchSurveys: async (page: number, size = 20, startDate: string, endDate: string): Promise<SurveysData> => {
        let url = "";
        if (startDate === "") {
            url = `search?q=modifiedOn%3C%27${endDate}%27&page=${page}&size=${size}`;
        } else if (endDate === "") {
            url = `search?q=modifiedOn%3E%27${startDate}%27&page=${page}&size=${size}`;
        } else {
            url = `search?q=modifiedOn%3E%27${startDate}%27%26modifiedOn%3C%27${endDate}%27&page=${page}&size=${size}`;
        }

        return fetcher({ url: `${SurveysService.baseUrl}/${url}` });
    },
};
