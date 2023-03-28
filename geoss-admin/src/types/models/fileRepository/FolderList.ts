import { IFolder, IPagination, IResponseLinks } from "@/types/models";

export interface IFolderList {
    _embedded: {
        folder: IFolder[];
    };
    page: IPagination;
    _links: IResponseLinks;
}
