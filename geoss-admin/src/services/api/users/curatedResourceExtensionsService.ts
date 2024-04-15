import {
    CreateUserExtension,
    ExtensionData,
    ExtensionTransferOption,
    UserExtensionData,
} from "@/types/models/userExtensions";
import { fetcher } from "@/utils/fetcher";

export const ResourceExtensionsService = {
    baseUrl: `${process.env.NEXT_PUBLIC_API}/curated/rest`,

    createUserExtension: async (data: CreateUserExtension) =>
        fetcher({ url: `${ResourceExtensionsService.baseUrl}/userExtensions`, method: "POST", body: data }),
    getUserExtensions: async (page: number, size = 20, userId: string): Promise<UserExtensionData> =>
        fetcher({
            url: `${ResourceExtensionsService.baseUrl}/userExtensions/check/user/${userId}?page=${page}&size=${size}`,
        }),
    getExtensions: async (page: number, size = 20): Promise<ExtensionData> =>
        fetcher({
            url: `${ResourceExtensionsService.baseUrl}/extensions?page=${page}&size=${size}`,
        }),
    getEntry: async (extensionId: number): Promise<ExtensionTransferOption[]> =>
        fetcher({ url: `${ResourceExtensionsService.baseUrl}/transferOptionExtension/extension/${extensionId}` }),
    updateEntry: async (entryExtensionId: number, data: any) =>
        fetcher({
            url: `${ResourceExtensionsService.baseUrl}/extensions/${entryExtensionId}`,
            method: "PUT",
            body: data,
        }),
    updateExtension: async (entryExtensionId: number, data: any) =>
        fetcher({
            url: `${ResourceExtensionsService.baseUrl}/userExtensions/${entryExtensionId}`,
            method: "PUT",
            body: data,
        }),
    updateTransferOptionsForExtension: async (data: any, entryId: number) =>
        fetcher({
            url: `${ResourceExtensionsService.baseUrl}/transferOptionExtension/extension/${entryId}`,
            method: "PUT",
            body: data,
        }),
};
