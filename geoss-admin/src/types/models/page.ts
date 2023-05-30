import { ILink, IPagination, IResponseLinks } from "@/types/models";
import { TranslatedData } from "@/types";

export interface IPage {
    title: TranslatedData;
    description: TranslatedData;
    contentId: number;
    slug: string;
    published: boolean;
    createdBy: string;
    createdDate: string;
    modifiedBy: string;
    modifiedDate: string;
    _links: {
        self: ILink;
        page: ILink;
    };
}

export interface IPageData {
    title: string;
    description: string;
    contentId: number;
    slug: string;
    published: boolean;
}

export interface IPageList {
    _embedded: {
        page: IPage[];
    };
    page: IPagination;
    _links: IResponseLinks;
}
