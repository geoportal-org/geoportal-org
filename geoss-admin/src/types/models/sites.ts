import { ILink } from "./commons";

export interface CreateSiteBody {
    name: string;
    url: string;
    logoDocumentId: number;
}

export interface SiteData {
    id: number;
    createdBy: string;
    createdOn: string;
    logoDocumentId: number;
    modifiedBy: string;
    modifiedOn: string;
    name: string;
    url: string;
    _links: {
        self: ILink;
        folder: ILink;
    };
}
