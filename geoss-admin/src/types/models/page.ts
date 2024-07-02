import { ILink, IPagination, IResponseLinks } from "@/types/models";
import { TranslatedData } from "@/types";

export interface IPage {
    title: TranslatedData;
    description: TranslatedData;
    contentId: number;
    slug: string;
    published: boolean;
    createdBy: string;
    createdOn: string;
    modifiedBy: string;
    modifiedOn: string;
    siteId: number | null;
    _links: {
        self: ILink;
        page: ILink;
    };
}

export interface IPageData {
    title: TranslatedData;
    description: TranslatedData;
    contentId: number;
    slug: string;
    published: boolean;
    siteId: number | null;
}

export interface IPageList {
    _embedded: {
        page: IPage[];
    };
    page: IPagination;
    _links: IResponseLinks;
}

export interface PagesInfo {
    size: number;
    totalElements: number;
    totalPages: number;
    number: number;
}
