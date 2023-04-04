import { Dispatch, SetStateAction } from "react";
import { IDocument, IDocumentList, IFolder, IFolderList } from "../models";

export type FileRepositoryTreeItem = {
    name: string;
    id: number;
    items?: FileRepositoryTreeItem[];
};

export type BreadcrumbItem = {
    folderId: number;
    folderTitle: string;
};

export type FileRepositoryBreadcrumProps = {
    breadcrumb: BreadcrumbItem[];
    handleBreadcrumbClick: (folderId: number, index: number) => void;
};

export type FileRepositoryProps = {
    folders: IFolderList;
    documents: IDocumentList;
};

export type FileRepositoryItemProps = {
    handleRepositoryItemClick: (item: IFolder | IDocument) => void;
    handleItemDeleteClick: (item: IFolder | IDocument) => void;
    handleItemEditClick: (item: IFolder | IDocument) => void;
    item: IFolder | IDocument;
};

export type FileRepositoryItemControlProps = {
    item: IFolder | IDocument;
    handleItemDeleteClick: (item: IFolder | IDocument) => void;
    handleItemEditClick: (item: IFolder | IDocument) => void;
};

export type FileRepositoryManageFolderProps = {
    currFolder: number;
    path: string;
    foldersList: IFolder[];
    setFoldersList: Dispatch<SetStateAction<IFolder[]>>;
    folderId?: number;
};

export type FileRepositoryFileInfoProps = {
    document: IDocument;
};
