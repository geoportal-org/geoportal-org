import { QueryParams } from "@/types";
import { IDocumentList, IFolder, IFolderData, IFolderList } from "@/types/models";
import { fetcher } from "@/utils/fetcher";

export const FileRepositoryService = {
    baseFolderUrl: `${process.env.NEXT_PUBLIC_API}/contents/rest/folder`,
    baseDocumentUrl: `${process.env.NEXT_PUBLIC_API}/contents/rest/document`,

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
};
