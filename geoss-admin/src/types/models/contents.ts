import { IDocument, ILink, IPagination, IResponseLinks } from "@/types/models";
import { TranslatedData } from "@/types";

export interface IContent {
    data: TranslatedData;
    createdBy: string;
    createdOn: string;
    modifiedBy: string;
    modifiedOn: string;
    published: boolean;
    title: TranslatedData;
    siteId: number | null;
    _links: {
        content: ILink;
        self: ILink;
    };
}

export interface IContentGrouped {
    label: string;
    options: IContent[] | IDocument[];
}

export interface IContentData {
    title: TranslatedData;
    data: TranslatedData;
    published: boolean;
    imageUrl: string;
    siteId: number | null;
}

export interface IContentList {
    _embedded: {
        content: IContent[];
    };
    page: IPagination;
    _links: IResponseLinks;
}
