import { UserResourcesData } from "@/types/models/userResources";
import { fetcher } from "@/utils/fetcher";

export const WorkflowService = {
    baseUrl: `${process.env.NEXT_PUBLIC_API}/curated/rest/workflow`,

    approveUserResource: async (userResourceId: number) =>
        fetcher({
            url: `${WorkflowService.baseUrl}/resource/approve/${userResourceId}`,
            method: "POST",
        }),
    deleteUserResource: async (userResourceId: number) =>
        fetcher({
            url: `${WorkflowService.baseUrl}/resource/delete/${userResourceId}`,
            method: "POST",
        }),
    denyUserResource: async (userResourceId: number) =>
        fetcher({
            url: `${WorkflowService.baseUrl}/resource/deny/${userResourceId}`,
            method: "POST",
        }),
    pendingUserResource: async (userResourceId: number) =>
        fetcher({
            url: `${WorkflowService.baseUrl}/resource/pending/${userResourceId}`,
            method: "POST",
        }),
    deleteEntry: async (entryId: number) =>
        fetcher({ url: `${WorkflowService.baseUrl}/entry/delete/${entryId}`, method: "DELETE" }),
};
