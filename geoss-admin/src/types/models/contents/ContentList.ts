import { IContent, IPagination, IResponseLinks } from "@/types/models";

export interface IContentList {
    _embedded: {
        content: IContent[];
    };
    page: IPagination;
    _links: IResponseLinks;
}
