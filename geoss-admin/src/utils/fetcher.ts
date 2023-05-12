import { FetcherData, QueryParams } from "@/types";

export const fetcher = async ({
    url,
    method = "GET",
    body,
    useCredentials = false,
    headers = {},
    query,
}: FetcherData): Promise<any> => {
    const options: RequestInit = {
        method,
        ...(!(body instanceof FormData) && {
            headers: new Headers({
                "Content-Type": "application/json",
                Authorization: "Basic dXNlcjpxYXoxMjM=",
                ...headers,
            }),
        }),
        ...(body && !(body instanceof FormData) && { body: JSON.stringify(body) }),
        ...(body && body instanceof FormData && { body }),
        ...(useCredentials && { credentials: "include" }),
    };

    if (query) {
        url = `${url}?${setQueryParams(query)}`;
    }

    const response = await fetch(url, options);

    if (response.status === 204) {
        return response.text();
    }

    if (response.ok) {
        return response.json();
    }

    const contentType = response.headers.get("content-type");
    const errorResponse =
        contentType && contentType.indexOf("application/json") !== -1 ? await response.json() : await response.text();

    //return Promise.reject(errorResponse);
    return Promise.reject({ errorInfo: errorResponse, errorStatus: response.status });
};

const setQueryParams = (queryParams: QueryParams): string =>
    Object.keys(queryParams)
        .map((key) => [key, queryParams[key]].map(encodeURIComponent).join("="))
        .join("&");
