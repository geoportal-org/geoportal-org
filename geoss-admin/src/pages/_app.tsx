import type { AppProps } from "next/app";
import Head from "next/head";
import { useRouter } from "next/router";
import { SessionProvider } from "next-auth/react";
import { useMemo } from "react";
import { IntlProvider } from "react-intl";
import { ChakraProvider } from "@chakra-ui/react";
import { Fonts, Layout } from "@/components";
import { flattenMessages } from "@/utils/helpers";
import { contentMessages } from "@/content";
import geossTheme from "@/theme";

const App = ({ Component, pageProps: { session, ...pageProps } }: AppProps) => {
    const { locale } = useRouter();
    const [shortLocale] = locale ? locale.split("-") : ["en"];

    const textContent = useMemo(
        () => flattenMessages(contentMessages[shortLocale as keyof typeof contentMessages]),
        [shortLocale]
    );

    return (
        <>
            <Head>
                <title>GEOSS Administrator</title>
                <meta name="description" content="GEOSS Administrator platform" />
                <meta name="viewport" content="width=device-width, initial-scale=1" />
                <link rel="icon" href="/favicon.ico" />
            </Head>
            <IntlProvider locale={shortLocale} messages={textContent} defaultLocale="en" onError={() => null}>
                <ChakraProvider theme={geossTheme}>
                    <Fonts />
                    <SessionProvider session={session}>
                        <Layout>
                            <Component {...pageProps} />
                        </Layout>
                    </SessionProvider>
                </ChakraProvider>
            </IntlProvider>
        </>
    );
};

export default App;
