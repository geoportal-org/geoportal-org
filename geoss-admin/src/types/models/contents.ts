import { ILink, IPagination, IResponseLinks } from "@/types/models";

export interface IContent {
    content: string;
    createdBy: string;
    createdDate: string;
    modifiedBy: string;
    modifiedDate: string;
    published: boolean;
    title: string;
    _links: {
        content: ILink;
        self: ILink;
    };
}

export interface IContentData {
    title: string;
    data: string;
    published: boolean;
}

export interface IContentList {
    _embedded: {
        content: IContent[];
    };
    page: IPagination;
    _links: IResponseLinks;
}
