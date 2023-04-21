import { ILink, IPagination, IResponseLinks } from "@/types/models";

export interface IApiSetting {
    id: number;
    set: string;
    key: string;
    value: string;
    created_by: string;
    created_on: string;
    modified_by: string;
    modified_on: string;
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
}
