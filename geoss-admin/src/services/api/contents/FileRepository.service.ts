import { QueryParams } from "@/types";
import { IDocumentList, IFolder, IFolderData, IFolderList } from "@/types/models";
import { fetcher } from "@/utils/fetcher";

export const FileRepositoryService = {
    baseFolderUrl: "http://10.254.7.59:8082/rest/folder",
    baseDocumentUrl: "http://10.254.7.59:8082/rest/document",
    baseFolderRoute: "/api/backend/folder",
    baseDocumentRoute: "/api/backend/document",

    getFoldersList: async (query?: QueryParams): Promise<IFolderList> =>
        fetcher({ url: FileRepositoryService.baseFolderUrl, query }),

    getDocumentsList: async (query?: QueryParams): Promise<IDocumentList> =>
        fetcher({ url: FileRepositoryService.baseDocumentUrl, query }),

    createFolder: async (folderData: IFolderData): Promise<IFolder> =>
        fetcher({ url: FileRepositoryService.baseFolderUrl, method: "POST", body: folderData }),

    getFolder: async (id: number): Promise<IFolder> => fetcher({ url: `${FileRepositoryService.baseFolderUrl}/${id}` }),

    deleteFolder: async (id: number) =>
        fetcher({ url: `${FileRepositoryService.baseFolderUrl}/${id}`, method: "DELETE" }),

    updateFolderTitle: async (id: number, folderData: Pick<IFolderData, "title">): Promise<IFolder> =>
        fetcher({ url: `${FileRepositoryService.baseFolderUrl}/${id}`, body: folderData, method: "PATCH" }),

    getFoldersRoute: async (query?: QueryParams): Promise<IFolderList> =>
        fetcher({ url: FileRepositoryService.baseFolderRoute, query }),

    getDocumentsRoute: async (query?: QueryParams): Promise<IDocumentList> =>
        fetcher({ url: FileRepositoryService.baseDocumentRoute, query }),

    createFolderRoute: async (folderData: IFolderData): Promise<IFolder> =>
        fetcher({ url: FileRepositoryService.baseFolderRoute, method: "POST", body: folderData }),

    getFolderRoute: async (id: number): Promise<IFolder> =>
        fetcher({ url: `${FileRepositoryService.baseFolderRoute}/${id}` }),

    deleteFolderRoute: async (id: number) =>
        fetcher({ url: `${FileRepositoryService.baseFolderRoute}/${id}`, method: "DELETE" }),

    updateFolderTitleRoute: async (id: number, folderData: Pick<IFolderData, "title">): Promise<IFolder> =>
        fetcher({ url: `${FileRepositoryService.baseFolderRoute}/${id}`, body: folderData, method: "PATCH" }),
};
