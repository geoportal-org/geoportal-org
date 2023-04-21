import { ILink, IPagination, IResponseLinks } from "@/types/models";

export interface IWebSetting {
    id: number;
    set: string;
    key: string;
    value: string;
    created_by: string;
    created_on: string;
    modified_by: string;
    modified_on: string;
    _links: {
        webSetting: ILink;
        self: ILink;
    };
}

export interface IWebSettingsList {
    _embedded: {
        webSettings: IWebSetting[];
    };
    page: IPagination;
    _links: IResponseLinks;
}

export interface IWebSettingData {
    set: string;
    key: string;
    value: string;
}
