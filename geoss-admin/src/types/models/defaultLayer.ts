import { ILink, IPagination, IResponseLinks } from "@/types/models";

export interface ILayer {
    createdBy: string;
    createdOn: string;
    id: number;
    modifiedBy: string;
    modifiedOn: string;
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
