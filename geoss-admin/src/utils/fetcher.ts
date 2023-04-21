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

    const errorResponse = await response.json();
    // return Promise.reject({ errorInfo: errorResponse, status: response.status });
    return Promise.reject(errorResponse);
};

const setQueryParams = (queryParams: QueryParams): string =>
    Object.keys(queryParams)
        .map((key) => [key, queryParams[key]].map(encodeURIComponent).join("="))
        .join("&");
