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
        headers: new Headers({ "content-type": "application/json", ...headers }),
        body: body ? JSON.stringify(body) : null,
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

    /*const responseBody = await response.json();
    return Promise.reject({
        status: response.status,
        ok: false,
        message: responseBody.message,
        body: responseBody,
    });*/
    return Promise.reject(response);
};

const setQueryParams = (queryParams: QueryParams): string =>
    Object.keys(queryParams)
        .map((key) => [key, queryParams[key]].map(encodeURIComponent).join("="))
        .join("&");
