export interface ILink {
    href: string;
}

export interface IPagination {
    size: number;
    totalElements: number;
    totalPages: number;
    number: number;
}

export interface IResponseLinks {
    profile: ILink;
    search: ILink;
    self: ILink;
}
