import { ILink, IPagination, IResponseLinks } from "@/types/models";

export interface ITutorialTag {
    id: number;
    name: string;
    show: boolean;
    type: "tip" | "new";
    placement: string;
    title: string; // to be changed for languages support
    description: string; // to be changed for languages support
    created_by: string;
    created_on: string;
    modified_by: string;
    modified_on: string;
    _links: {
        tag: ILink;
        self: ILink;
    };
}

export interface ITutorialTagList {
    _embedded: {
        tags: ITutorialTag[];
    };
    page: IPagination;
    _links: IResponseLinks;
}

export interface ITutorialTagData {
    name: string;
    show: boolean;
    type: "tip" | "new";
    placement: string;
    title: {
        ru: string;
        en: string;
        fr: string;
        pl: string;
        es: string;
        zh: string;
    };
    description: {
        ru: string;
        en: string;
        fr: string;
        pl: string;
        es: string;
        zh: string;
    };
}

// TBD tag placement && tag type
