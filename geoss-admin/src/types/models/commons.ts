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
    search?: ILink;
    self: ILink;
}

export interface IErrorObject {
    errors: string[];
    message: string;
    status: number;
    timestamp: string;
}

export interface IErrorInformation {
    entity: string;
    invalidValue: string;
    message: string;
    property: string;
}
