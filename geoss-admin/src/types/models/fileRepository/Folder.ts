import { ILink } from "@/types/models";

export interface IFolder {
    title: string;
    parentFolderId: number;
    createdBy: string;
    createdDate: string;
    modifiedBy: string;
    modifiedDate: string;
    path: string;
    _links: {
        self: ILink;
        folder: ILink;
    };
}
