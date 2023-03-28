import { IDocument, IPagination, IResponseLinks } from "@/types/models";

export interface IDocumentList {
    _embedded: {
        document: IDocument[];
    };
    page: IPagination;
    _links: IResponseLinks;
}
