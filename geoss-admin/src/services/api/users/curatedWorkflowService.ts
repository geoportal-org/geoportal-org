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
            method: "DELETE",
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
    approveUserExtension: async (userExtensionId: number) =>
        fetcher({
            url: `${WorkflowService.baseUrl}/extension/approve/${userExtensionId}`,
            method: "POST",
        }),
    deleteUserExtension: async (userExtensionId: number) =>
        fetcher({
            url: `${WorkflowService.baseUrl}/extension/delete/${userExtensionId}`,
            method: "DELETE",
        }),
    denyUserExtension: async (userExtensionId: number) =>
        fetcher({
            url: `${WorkflowService.baseUrl}/extension/deny/${userExtensionId}`,
            method: "POST",
        }),
    pendingUserExtension: async (userExtensionId: number) =>
        fetcher({
            url: `${WorkflowService.baseUrl}/extension/pending/${userExtensionId}`,
            method: "POST",
        }),
    deleteEntry: async (entryId: number) =>
        fetcher({ url: `${WorkflowService.baseUrl}/entry/delete/${entryId}`, method: "DELETE" }),
    deleteExtensionEntry: async (entryId: number) =>
        fetcher({ url: `${WorkflowService.baseUrl}/entryExtension/delete/${entryId}`, method: "DELETE" }),
};
