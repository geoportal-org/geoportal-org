import { ILink, IPagination, IResponseLinks } from "@/types/models";

export interface ILayer {
    created_by: string;
    created_on: string;
    id: number;
    modified_by: string;
    modified_on: string;
    name: string;
    url: string;
    visible: boolean;
    _links: {
        layer: ILink;
        self: ILink;
    };
}

export interface ILayerList {
    _embedded: {
        layers: ILayer[];
    };
    page: IPagination;
    _links: IResponseLinks;
}

export interface ILayerData {
    name: string;
    url: string;
    visible: boolean;
}
