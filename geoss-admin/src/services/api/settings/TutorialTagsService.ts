import { QueryParams } from "@/types";
import { ITutorialTag, ITutorialTagData, ITutorialTagList, ITutorialTagTranslated } from "@/types/models";
import { fetcher } from "@/utils/fetcher";

export const TutorialTagsService = {
    baseUrl: `${process.env.NEXT_PUBLIC_API}/settings/rest/tags`,

    getTagList: async (query?: QueryParams): Promise<ITutorialTagList> =>
        fetcher({ url: TutorialTagsService.baseUrl, query }),

    getTag: async (id: number): Promise<ITutorialTag> => fetcher({ url: `${TutorialTagsService.baseUrl}/${id}` }),

    createTag: async (tagData: ITutorialTagData): Promise<ITutorialTagTranslated> =>
        fetcher({ url: TutorialTagsService.baseUrl, method: "POST", body: tagData }),

    updateTag: async (id: number, tagData: ITutorialTagData): Promise<ITutorialTagTranslated> =>
        fetcher({ url: `${TutorialTagsService.baseUrl}/${id}`, method: "PUT", body: tagData }),

    deleteTag: async (id: number) => fetcher({ url: `${TutorialTagsService.baseUrl}/${id}`, method: "DELETE" }),
};
