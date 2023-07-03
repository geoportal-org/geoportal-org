import type { AppProps } from "next/app";

export interface CustomAppProps<P = {}> {
    Component: AppProps["Component"] & { nonAuth: boolean };
    pageProps: AppProps["pageProps"] & P;
}
