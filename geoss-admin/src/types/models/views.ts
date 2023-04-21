import { ILink, IPagination, IResponseLinks } from "@/types/models";

export interface IView {
    id: number;
    label: string;
    title: string;
    value: string;
    defaultOption: boolean;
    subOptions: ISubView[];
    created_by: string;
    created_on: string;
    modified_by: string;
    modified_on: string;
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
    created_by: string;
    created_on: string;
    modified_by: string;
    modified_on: string;
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
}

export interface ISubViewData {
    label: string;
    title: string;
    value: string;
}
