import { ILink, IPagination, IResponseLinks } from "@/types/models";

export interface IView {
    id: number;
    label: string;
    title: string;
    value: string;
    defaultOption: boolean;
    subOptions: ISubView[];
    createdBy: string;
    createdOn: string;
    modifiedBy: string;
    modifiedOn: string;
    _links: {
        view: ILink;
        self: ILink;
    };
}

export interface ISubView {
    id: number;
    label: string;
    title: string;
    value: string;
    createdBy: string;
    createdOn: string;
    modifiedBy: string;
    modifiedOn: string;
}

export interface IViewList {
    _embedded: {
        views: IView[];
    };
    page: IPagination;
    _links: IResponseLinks;
}

export interface IViewData {
    label: string;
    title: string;
    value: string;
    defaultOption: boolean;
    subOptions: ISubViewData[];
    siteId: number | null;
}

export interface ISubViewData {
    label: string;
    title: string;
    value: string;
}
