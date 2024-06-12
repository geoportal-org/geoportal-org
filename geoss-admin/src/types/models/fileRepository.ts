import { ILink, IPagination, IResponseLinks } from "@/types/models";

export interface IDocument {
    title: string;
    fileName: string;
    extension: string;
    path: string;
    folderId: number;
    createdBy: string;
    createdDate: string;
    modifiedBy: string;
    modifiedDate: string;
    _links: {
        self: ILink;
        document: ILink;
    };
}

export interface IDocumentPatch {
    title: string;
    fileName: string;
    extension: string;
    path: string;
    folderId: number;
}

export interface IDocumentList {
    _embedded: {
        document: IDocument[];
    };
    page: IPagination;
    _links: IResponseLinks;
}

export interface IFolder {
    title: string;
    parentFolderId: number;
    createdBy: string;
    createdDate: string;
    modifiedBy: string;
    modifiedDate: string;
    path: string;
    siteId: number | null;
    _links: {
        self: ILink;
        folder: ILink;
    };
}

export interface IFolderData {
    title: string;
    parentFolderId: number;
    path: string;
    siteId: number | null;
}

export interface IFolderList {
    _embedded: {
        folder: IFolder[];
    };
    page: IPagination;
    _links: IResponseLinks;
}
