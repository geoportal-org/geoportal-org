export type QueryParams = {
    [index: string]: string;
};

export type FetcherData = {
    url: string;
    method?: "GET" | "POST" | "PUT" | "DELETE" | "PATCH";
    body?: any;
    useCredentials?: boolean;
    headers?: HeadersInit;
    query?: QueryParams;
};
