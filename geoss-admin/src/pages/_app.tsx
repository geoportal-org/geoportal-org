import type { AppProps } from "next/app";
import Head from "next/head";
import { useRouter } from "next/router";
import { useMemo } from "react";
import { IntlProvider } from "react-intl";
import { ChakraProvider } from "@chakra-ui/react";
import { Fonts, Layout } from "@/components";
import { flattenMessages } from "@/utils/helpers";
import { contentMessages } from "@/content";
import geossTheme from "@/theme";
//import { SessionProvider } from "next-auth/react";
//import { Session } from "next-auth";

/*interface CustomAppProps<P = {}> {
    Component: AppProps["Component"] & { auth: boolean };
    pageProps: AppProps["pageProps"] & P;
}*/

const App = ({ Component, pageProps }: /*CustomAppProps<{ session: Session }>)*/ AppProps) => {
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
                    {/*<SessionProvider session={pageProps.session}>*/}
                    <Layout>
                        <Component {...pageProps} />
                        {/*Component.auth ? (
                                <AuthGuard>
                                    <Component {...pageProps} />
                                </AuthGuard>
                            ) : (
                                <Component {...pageProps} />
                            )*/}
                    </Layout>
                    {/*</SessionProvider>*/}
                </ChakraProvider>
            </IntlProvider>
        </>
    );
};

export default App;
