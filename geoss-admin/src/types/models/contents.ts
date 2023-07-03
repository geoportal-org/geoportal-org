import { ILink, IPagination, IResponseLinks } from "@/types/models";
import { TranslatedData } from "@/types";

export interface IContent {
    data: TranslatedData;
    createdBy: string;
    createdDate: string;
    modifiedBy: string;
    modifiedDate: string;
    published: boolean;
    title: TranslatedData;
    _links: {
        content: ILink;
        self: ILink;
    };
}

export interface IContentData {
    title: TranslatedData;
    data: TranslatedData;
    published: boolean;
}

export interface IContentList {
    _embedded: {
        content: IContent[];
    };
    page: IPagination;
    _links: IResponseLinks;
}
