import { ILink, IPagination, IResponseLinks } from "@/types/models";

export interface ITutorialTag {
    id: number;
    name: string;
    show: boolean;
    type: TagType;
    placement: TagPlacement;
    title: string; // to be changed for languages support
    description: string; // to be changed for languages support
    createdBy: string;
    createdOn: string;
    modifiedBy: string;
    modifiedOn: string;
    _links: {
        tag: ILink;
        self: ILink;
    };
}

export interface ITutorialTagTranslated extends Omit<ITutorialTag, "title" | "description"> {
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
    type: TagType;
    placement: TagPlacement;
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

export enum TagType {
    TIP = "tip",
    NEW = "new",
}

export enum TagPlacement {
    LEFT_TOP = "left-top",
    LEFT_BOTTOM = "left-bottom",
    LEFT_CENTER = "left-center",
    RIGHT_TOP = "right-top",
    RIGHT_BOTTOM = "right-bottom",
    RIGHT_CENTER = "right-center",
    TOP_CENTER = "top-center",
    BOTTOM_CENTER = "bottom-center",
    CENTER_CENTER = "center-center",
}
