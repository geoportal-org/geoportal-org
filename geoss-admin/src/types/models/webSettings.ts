import { ILink, IPagination, IResponseLinks } from "@/types/models";

export interface IWebSetting {
    id: number;
    set: string;
    key: string;
    value: string;
    createdBy: string;
    createdOn: string;
    modifiedBy: string;
    modifiedOn: string;
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
    siteId: number | null;
}
