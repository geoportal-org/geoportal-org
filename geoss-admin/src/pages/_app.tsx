import type { AppProps } from "next/app";
import Head from "next/head";
import { ChakraProvider } from "@chakra-ui/react";
import { Fonts } from "@/components";
import geossTheme from "@/theme";

const App = ({ Component, pageProps }: AppProps) => {
    return (
        <>
            <Head>
                <title>GEOSS Administrator</title>
                <meta name="description" content="GEOSS Administrator platform" />
                <meta name="viewport" content="width=device-width, initial-scale=1" />
                <link rel="icon" href="/favicon.ico" />
            </Head>
            <ChakraProvider theme={geossTheme}>
                <Fonts />
                <Component {...pageProps} />
            </ChakraProvider>
        </>
    );
};

export default App;
