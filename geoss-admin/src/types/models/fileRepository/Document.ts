import { ILink } from "@/types/models";

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
