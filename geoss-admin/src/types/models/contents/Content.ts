import { ILink } from "@/types/models";

export interface IContent {
    content: string;
    createdBy: string;
    createdDate: string;
    modifiedBy: string;
    modifiedDate: string;
    published: boolean;
    title: string;
    _links: {
        content: ILink;
        self: ILink;
    };
}
