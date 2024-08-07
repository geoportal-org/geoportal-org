export type QueryParams = {
    [index: string]: string | number;
};

export type FetcherData = {
    url: string;
    method?: "GET" | "POST" | "PUT" | "DELETE" | "PATCH" | "OPTIONS";
    body?: FormData | Object;
    useCredentials?: boolean;
    headers?: HeadersInit;
    query?: QueryParams;
};
