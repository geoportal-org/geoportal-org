import { ILink, IPagination, IResponseLinks } from "@/types/models";

export interface IApiSetting {
    id: number;
    set: string;
    key: string;
    value: string;
    createdBy: string;
    createdOn: string;
    modifiedBy: string;
    modifiedOn: string;
    _links: {
        apiSetting: ILink;
        self: ILink;
    };
}

export interface IApiSettingsList {
    _embedded: {
        apiSettings: IApiSetting[];
    };
    page: IPagination;
    _links: IResponseLinks;
}

export interface IApiSettingData {
    set: string;
    key: string;
    value: string;
    siteId: number | null;
}
