import { ILink, IPagination, IResponseLinks } from "@/types/models";

export interface IMenuItem {
    title: string;
    imageTitle: string;
    imageSource: string;
    url: string;
    priority: number;
    parentMenuId: number;
    levelId: number;
    createdBy: string;
    createdDate: string;
    modifiedBy: string;
    modifiedDate: string;
    _links: {
        menu: ILink;
        self: ILink;
    };
}

export interface IMenuList {
    _embedded: {
        menu: IMenuItem[];
    };
    page: IPagination;
    _links: IResponseLinks;
}

export interface IFoundMenuList {
    _embedded: {
        menu: IMenuItem[];
    };
    page: IPagination;
    _links: {
        self: ILink;
    };
}

export interface IMenuItemData {
    title: string;
    imageTitle: string;
    imageSource: string;
    url: string;
    priority: number;
    parentMenuId: number;
    levelId: number;
}
