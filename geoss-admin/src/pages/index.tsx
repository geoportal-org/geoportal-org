import { GetServerSideProps, NextPage, InferGetServerSidePropsType } from "next";
import { Box, Flex } from "@chakra-ui/react";
import { MainContent, PrimaryButton } from "@/components";
import { getProviders, signIn, ClientSafeProvider, LiteralUnion } from "next-auth/react";
import { useEffect, useState } from "react";
import { Session, getServerSession } from "next-auth";
import { BuiltInProviderType } from "next-auth/providers";
import { authOptions } from "./api/auth/[...nextauth]";
import { pagesRoutes } from "@/data";

interface HomeProps {
    providers: Record<LiteralUnion<BuiltInProviderType, string>, ClientSafeProvider> | null;
    session: Session | null;
}

const Home: NextPage<HomeProps> = ({ providers, session }) => {
    console.log(providers);
    console.log(session);
    console.log(process.env.NEXT_PUBLIC_URL);

    const [providersList, setProvidersList] = useState<Record<
        LiteralUnion<BuiltInProviderType, string>,
        ClientSafeProvider
    > | null>(null);

    useEffect(() => {
        const getProvidersList = async () => {
            const providersInfo = await getProviders();
            setProvidersList(() => providersInfo);
        };
        getProvidersList();
    }, []);

    return (
        <MainContent titleId={"SIGN IN"}>
            <Flex>
                {providersList &&
                    Object.values(providersList).map((provider) => (
                        <Box key={provider.name}>
                            <PrimaryButton
                                onClick={() =>
                                    signIn(provider.id, {
                                        callbackUrl: `${process.env.NEXT_PUBLIC_URL}${pagesRoutes.website}`,
                                    })
                                }
                            >
                                Sign in with {provider.name}
                            </PrimaryButton>
                        </Box>
                    ))}
            </Flex>
        </MainContent>
    );
};

export default Home;

export const getServerSideProps: GetServerSideProps<HomeProps> = async (context) => {
    console.log("-- server side START --");
    console.log(context);
    const { req, res } = context;

    const session = await getServerSession(req, res, authOptions);
    const providers = await getProviders();

    console.log(session);
    console.log(providers);
    console.log(process.env.KEYCLOAK_CLIENT_ID);
    console.log(process.env.KEYCLOAK_CLIENT_SECRET);
    console.log(process.env.KEYCLOAK_BASE_URL);

    console.log("-- server side END --");

    if (session) {
        return {
            redirect: { destination: "/contents", permanent: true },
        };
    }

    return {
        props: {
            providers,
            session,
        },
    };
};
