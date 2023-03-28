import { ILink } from "@/types/models";

export interface IPage {
    title: string;
    description: string;
    contentId: number;
    slug: string;
    published: boolean;
    createdBy: string;
    createdDate: string;
    modifiedBy: string;
    modifiedDate: string;
    _links: {
        self: ILink;
        page: ILink;
    };
}
