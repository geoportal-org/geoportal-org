import { IPage, IPagination, IResponseLinks } from "@/types/models";

export interface IPageList {
    _embedded: {
        page: IPage[];
    };
    page: IPagination;
    _links: IResponseLinks;
}
